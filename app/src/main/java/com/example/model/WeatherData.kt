package com.example.model

data class DailyForecast(
    val dayName: String,
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val condition: String,
    val rainProb: Int,
    val iconEmoji: String
)

data class AgriculturalWeather(
    val locationName: String,
    val currentTemp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double, // km/h
    val rainfallChance: Int, // %
    val weatherCondition: String,
    val weatherIcon: String,
    val isRaining: Boolean = false,
    // Agricultural specific advisories
    val sprayStatus: SprayAdvisoryStatus,
    val sprayReason: String,
    val irrigationAdvice: String,
    val diseaseRiskAlert: String,
    val forecast: List<DailyForecast> = emptyList()
)

enum class SprayAdvisoryStatus {
    FAVORABLE,    // Wind < 12 km/h, no rain in 6h, mild temp
    CAUTION,      // Moderate wind or mild chance of rain
    UNFAVORABLE   // High wind, imminent rain, or scorching heat
}
