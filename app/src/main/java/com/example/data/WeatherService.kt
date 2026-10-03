package com.example.data

import com.example.model.AgriculturalWeather
import com.example.model.DailyForecast
import com.example.model.SprayAdvisoryStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class FarmLocation(
    val name: String,
    val state: String,
    val lat: Double,
    val lon: Double
)

object WeatherService {

    val popularFarmingRegions = listOf(
        FarmLocation("Karnal", "Haryana", 29.6857, 76.9905),
        FarmLocation("Ludhiana", "Punjab", 30.9010, 75.8573),
        FarmLocation("Pune", "Maharashtra", 18.5204, 73.8567),
        FarmLocation("Indore", "Madhya Pradesh", 22.7196, 75.8577),
        FarmLocation("Varanasi", "Uttar Pradesh", 25.3176, 82.9739),
        FarmLocation("Guntur", "Andhra Pradesh", 16.3067, 80.4365),
        FarmLocation("Rajkot", "Gujarat", 22.3039, 70.8022),
        FarmLocation("Thanjavur (Cauvery Delta)", "Tamil Nadu", 10.7870, 79.1378),
        FarmLocation("Kota", "Rajasthan", 25.2138, 75.8648),
        FarmLocation("Patna", "Bihar", 25.5941, 85.1376)
    )

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun fetchAgriculturalWeather(location: FarmLocation): AgriculturalWeather = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.open-meteo.com/v1/forecast?" +
                    "latitude=${location.lat}&longitude=${location.lon}" +
                    "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m" +
                    "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max" +
                    "&timezone=auto"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                if (!body.isNullOrEmpty()) {
                    return@withContext parseOpenMeteoJson(body, location)
                }
            }
        } catch (_: Exception) {
            // fallback gracefully
        }
        return@withContext getOfflineMockWeather(location)
    }

    private fun parseOpenMeteoJson(jsonStr: String, location: FarmLocation): AgriculturalWeather {
        val root = JSONObject(jsonStr)
        val current = root.getJSONObject("current")
        val temp = current.optDouble("temperature_2m", 28.0)
        val humidity = current.optInt("relative_humidity_2m", 65)
        val windSpeed = current.optDouble("wind_speed_10m", 8.5)
        val weatherCode = current.optInt("weather_code", 0)

        val (conditionText, icon) = decodeWeatherCode(weatherCode)

        // Parse Daily
        val daily = root.optJSONObject("daily")
        val forecastList = mutableListOf<DailyForecast>()
        var maxRainChance = 10

        if (daily != null) {
            val times = daily.optJSONArray("time")
            val codes = daily.optJSONArray("weather_code")
            val maxTemps = daily.optJSONArray("temperature_2m_max")
            val minTemps = daily.optJSONArray("temperature_2m_min")
            val rainProbs = daily.optJSONArray("precipitation_probability_max")

            val count = times?.length() ?: 0
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dayFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

            for (i in 0 until minOf(count, 5)) {
                val dateStr = times?.optString(i) ?: ""
                val code = codes?.optInt(i) ?: 0
                val maxT = maxTemps?.optDouble(i) ?: 32.0
                val minT = minTemps?.optDouble(i) ?: 22.0
                val rainP = rainProbs?.optInt(i) ?: 10
                if (i == 0) maxRainChance = rainP

                var dayName = "Day $i"
                try {
                    val parsed = sdf.parse(dateStr)
                    if (parsed != null) {
                        dayName = if (i == 0) "Today" else dayFormat.format(parsed)
                    }
                } catch (_: Exception) {}

                val (cText, cIcon) = decodeWeatherCode(code)
                forecastList.add(
                    DailyForecast(
                        dayName = dayName,
                        date = dateStr,
                        maxTemp = maxT,
                        minTemp = minT,
                        condition = cText,
                        rainProb = rainP,
                        iconEmoji = cIcon
                    )
                )
            }
        }

        // Determine spray status
        val (sprayStatus, sprayReason) = when {
            maxRainChance > 50 -> Pair(
                SprayAdvisoryStatus.UNFAVORABLE,
                "Rain probability is high ($maxRainChance%). Avoid pesticide/fungicide spray as it will wash away."
            )
            windSpeed > 15.0 -> Pair(
                SprayAdvisoryStatus.UNFAVORABLE,
                "Wind speed is ${String.format(Locale.US, "%.1f", windSpeed)} km/h. High spray drift hazard to non-target crops."
            )
            windSpeed > 10.0 -> Pair(
                SprayAdvisoryStatus.CAUTION,
                "Moderate breeze (${String.format(Locale.US, "%.1f", windSpeed)} km/h). Use low-drift nozzles with caution."
            )
            temp > 35.0 -> Pair(
                SprayAdvisoryStatus.CAUTION,
                "Temperature is high (${temp.toInt()}°C). Avoid noon spraying; chemical may evaporate quickly and scorch leaves."
            )
            else -> Pair(
                SprayAdvisoryStatus.FAVORABLE,
                "Excellent conditions for spraying! Calm wind (${String.format(Locale.US, "%.1f", windSpeed)} km/h) and minimal rain risk ($maxRainChance%)."
            )
        }

        val irrigationAdvice = when {
            maxRainChance > 60 -> "Rain anticipated. Postpone field irrigation to avoid waterlogging and root rot."
            temp > 34 && humidity < 40 -> "High evapotranspiration. Schedule early morning or late evening deep drip irrigation."
            else -> "Normal irrigation schedule recommended. Check root zone soil moisture before watering."
        }

        val diseaseRiskAlert = when {
            humidity > 80 && temp in 20.0..30.0 -> "HIGH FUNGAL OUTBREAK RISK! High humidity ($humidity%) and warm temps favor downy mildew, blight, and blast. Scout leaves closely."
            humidity > 70 -> "MODERATE RISK. Keep canopy aerated and clear excessive bottom foliage."
            else -> "LOW RISK. Dry weather suppresses fungal spore germination."
        }

        return AgriculturalWeather(
            locationName = "${location.name}, ${location.state}",
            currentTemp = temp,
            feelsLike = temp + (if (humidity > 70) 2.0 else -1.0),
            humidity = humidity,
            windSpeed = windSpeed,
            rainfallChance = maxRainChance,
            weatherCondition = conditionText,
            weatherIcon = icon,
            isRaining = weatherCode in 51..67 || weatherCode in 80..82,
            sprayStatus = sprayStatus,
            sprayReason = sprayReason,
            irrigationAdvice = irrigationAdvice,
            diseaseRiskAlert = diseaseRiskAlert,
            forecast = forecastList
        )
    }

    private fun decodeWeatherCode(code: Int): Pair<String, String> {
        return when (code) {
            0 -> Pair("Clear Sky", "☀️")
            1, 2 -> Pair("Partly Cloudy", "🌤️")
            3 -> Pair("Overcast", "☁️")
            45, 48 -> Pair("Foggy", "🌫️")
            51, 53, 55 -> Pair("Light Drizzle", "🌦️")
            61, 63 -> Pair("Moderate Rain", "🌧️")
            65 -> Pair("Heavy Rain", "⛈️")
            80, 81, 82 -> Pair("Rain Showers", "🌧️")
            95, 96, 99 -> Pair("Thunderstorm", "⚡")
            else -> Pair("Fair Weather", "🌤️")
        }
    }

    fun getOfflineMockWeather(location: FarmLocation): AgriculturalWeather {
        return AgriculturalWeather(
            locationName = "${location.name}, ${location.state}",
            currentTemp = 29.5,
            feelsLike = 31.0,
            humidity = 62,
            windSpeed = 7.5,
            rainfallChance = 15,
            weatherCondition = "Mostly Sunny",
            weatherIcon = "🌤️",
            isRaining = false,
            sprayStatus = SprayAdvisoryStatus.FAVORABLE,
            sprayReason = "Optimal spray window: Calm winds (< 10 km/h) and low rain probability.",
            irrigationAdvice = "Soil moisture is adequate. Irrigate in the evening if top 2 inches feel dry.",
            diseaseRiskAlert = "Low to moderate disease pressure. Inspect lower leaves for early spots.",
            forecast = listOf(
                DailyForecast("Today", "2026-09-29", 31.0, 21.0, "Sunny", 10, "☀️"),
                DailyForecast("Tomorrow", "2026-09-30", 30.5, 22.0, "Partly Cloudy", 15, "🌤️"),
                DailyForecast("Day 3", "2026-10-01", 32.0, 23.0, "Clear Sky", 5, "☀️"),
                DailyForecast("Day 4", "2026-10-02", 29.0, 20.0, "Light Shower", 40, "🌦️"),
                DailyForecast("Day 5", "2026-10-03", 28.5, 19.5, "Overcast", 25, "☁️")
            )
        )
    }
}
