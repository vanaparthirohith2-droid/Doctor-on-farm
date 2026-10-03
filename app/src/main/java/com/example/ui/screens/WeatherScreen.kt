package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FarmLocation
import com.example.data.WeatherService
import com.example.model.AgriculturalWeather
import com.example.model.AppLanguage
import com.example.model.SprayAdvisoryStatus
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertRedLight
import com.example.ui.theme.AmberHarvest
import com.example.ui.theme.AmberLight
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenLight
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryVariant
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.InfoBlueLight
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WeatherScreen(
    currentLanguage: AppLanguage,
    innerPadding: PaddingValues
) {
    var selectedLocation by remember { mutableStateOf(WeatherService.popularFarmingRegions[0]) }
    var weatherData by remember { mutableStateOf<AgriculturalWeather?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    fun loadWeather(loc: FarmLocation) {
        coroutineScope.launch {
            isLoading = true
            weatherData = WeatherService.fetchAgriculturalWeather(loc)
            isLoading = false
        }
    }

    LaunchedEffect(selectedLocation) {
        loadWeather(selectedLocation)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // TOP HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(InfoBlue, Color(0xFF0D47A1))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "Weather",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.HINDI -> "कृषि मौसम एवं स्प्रे सलाह"
                                AppLanguage.PUNJABI -> "ਖੇਤੀਬਾੜੀ ਮੌਸਮ ਸਲਾਹ"
                                AppLanguage.TELUGU -> "వ్యవసాయ వాతావరణం"
                                AppLanguage.TAMIL -> "விவசாய வானிலை"
                                AppLanguage.BENGALI -> "কৃষি আবহাওয়া পরামর্শ"
                                AppLanguage.MARATHI -> "कृषी हवामान अंदाज"
                                AppLanguage.GUJARATI -> "ખેતીવાડી હવામાન"
                                else -> "Agricultural Weather & Spray"
                            },
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${selectedLocation.name}, ${selectedLocation.state}",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = { loadWeather(selectedLocation) },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // REGION CHIP SELECTOR
            Text(
                text = when (currentLanguage) {
                    AppLanguage.HINDI -> "प्रमुख कृषि क्षेत्र चुनें"
                    AppLanguage.PUNJABI -> "ਖੇਤੀ ਖੇਤਰ ਚੁਣੋ"
                    AppLanguage.TELUGU -> "ప్రాంతాన్ని ఎంచుకోండి"
                    AppLanguage.MARATHI -> "कृषी विभाग निवडा"
                    else -> "Select Farming District / Region"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeatherService.popularFarmingRegions.forEach { region ->
                    val isSelected = selectedLocation == region
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) InfoBlue else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedLocation = region }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${region.name} (${region.state})",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = InfoBlue)
                }
            } else if (weatherData != null) {
                val data = weatherData!!

                // CURRENT WEATHER HERO CARD
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${data.currentTemp.toInt()}°C",
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${data.weatherCondition} • Feels like ${data.feelsLike.toInt()}°C",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = data.weatherIcon,
                                fontSize = 54.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // METRICS GRID
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Humidity
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = InfoBlueLight,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Opacity,
                                        contentDescription = "Humidity",
                                        tint = InfoBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "${data.humidity}%", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = "नमी / Humidity", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Wind Speed
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenLight,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Air,
                                        contentDescription = "Wind",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${String.format(Locale.US, "%.1f", data.windSpeed)} km/h",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(text = "हवा / Wind", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Rain chance
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AmberLight,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Rain",
                                        tint = AmberHarvest,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "${data.rainfallChance}%", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = "बारिश / Rain", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AGRICULTURAL SPRAY WINDOW ADVISORY
                val (sprayCardColor, sprayIconColor, sprayBadgeText) = when (data.sprayStatus) {
                    SprayAdvisoryStatus.FAVORABLE -> Triple(GreenLight, GreenPrimary, "छिड़काव के लिए उपयुक्त (Safe to Spray)")
                    SprayAdvisoryStatus.CAUTION -> Triple(AmberLight, AmberHarvest, "सावधानी से छिड़काव करें (Caution Spray)")
                    SprayAdvisoryStatus.UNFAVORABLE -> Triple(AlertRedLight, AlertRed, "छिड़काव न करें (Do NOT Spray)")
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = sprayCardColor),
                    border = BorderStroke(1.5.dp, sprayIconColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (data.sprayStatus == SprayAdvisoryStatus.FAVORABLE) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = sprayIconColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = sprayBadgeText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = sprayIconColor
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = data.sprayReason,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // IRRIGATION & DISEASE RISK ADVISORY
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Irrigation
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(InfoBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = InfoBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "सिंचाई सलाह (Irrigation Advice)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = data.irrigationAdvice,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Disease risk
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AmberLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = AmberHarvest,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "रोग व फफूंद जोखिम चेतावनी (Disease Risk Index)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AmberHarvest
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = data.diseaseRiskAlert,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5-DAY FORECAST
                Text(
                    text = "आगामी 5 दिनों का मौसम (5-Day Farming Forecast)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                data.forecast.forEach { day ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = day.iconEmoji, fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = day.dayName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = day.condition,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (day.rainProb > 40) AlertRedLight else InfoBlueLight
                                ) {
                                    Text(
                                        text = "💧 ${day.rainProb}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (day.rainProb > 40) AlertRed else InfoBlue,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${day.maxTemp.toInt()}° / ${day.minTemp.toInt()}°",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
