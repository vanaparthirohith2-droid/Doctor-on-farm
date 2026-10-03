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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SeasonalCropsData
import com.example.model.AppLanguage
import com.example.model.CropSeason
import com.example.model.CropSuggestion
import com.example.ui.theme.AmberHarvest
import com.example.ui.theme.AmberLight
import com.example.ui.theme.EarthBrown
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenLight
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryVariant
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.InfoBlueLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonalCropsScreen(
    currentLanguage: AppLanguage,
    innerPadding: PaddingValues
) {
    var selectedSeasonTab by remember { mutableIntStateOf(0) } // 0: All, 1: Kharif, 2: Rabi, 3: Zaid
    var searchQuery by remember { mutableStateOf("") }
    var selectedSoilFilter by remember { mutableStateOf("All") }
    var expandedCropId by remember { mutableStateOf<String?>(null) }

    val seasons = listOf(
        Pair("सभी मौसम", CropSeason.ALL),
        Pair("खरीफ (मानसून)", CropSeason.KHARIF),
        Pair("रबी (सर्दियां)", CropSeason.RABI),
        Pair("जायद (गर्मी)", CropSeason.ZAID)
    )

    val soilFilters = listOf("All", "Loam", "Black Soil", "Sandy Loam", "Clay", "Alluvial")

    val filteredCrops = remember(selectedSeasonTab, searchQuery, selectedSoilFilter) {
        val targetSeason = seasons[selectedSeasonTab].second
        SeasonalCropsData.crops.filter { crop ->
            val matchesSeason = targetSeason == CropSeason.ALL || crop.season == targetSeason
            val matchesSearch = searchQuery.isBlank() ||
                    crop.nameEn.contains(searchQuery, ignoreCase = true) ||
                    crop.nameHi.contains(searchQuery, ignoreCase = true) ||
                    crop.category.contains(searchQuery, ignoreCase = true)
            val matchesSoil = selectedSoilFilter == "All" ||
                    crop.soilTypes.any { it.contains(selectedSoilFilter, ignoreCase = true) }

            matchesSeason && matchesSearch && matchesSoil
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AmberHarvest, Color(0xFFE65100))
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
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Season Calendar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.HINDI -> "मौसम अनुसार श्रेष्ठ फसल सलाह"
                                AppLanguage.PUNJABI -> "ਮੌਸਮ ਅਨੁਸਾਰ ਵਧੀਆ ਫਸਲਾਂ"
                                AppLanguage.TELUGU -> "సీజన్ పంటల సలహాలు"
                                AppLanguage.TAMIL -> "பருவ கால பயிர் வழிகாட்டி"
                                AppLanguage.MARATHI -> "हंगामानुसार सर्वोत्तम पिके"
                                else -> "Seasonal Crop Planning & Advisory"
                            },
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kharif • Rabi • Zaid • High Profit Crops",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // SEASON TABS
        TabRow(
            selectedTabIndex = selectedSeasonTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = AmberHarvest
        ) {
            seasons.forEachIndexed { index, pair ->
                Tab(
                    selected = selectedSeasonTab == index,
                    onClick = { selectedSeasonTab = index },
                    text = {
                        Text(
                            text = pair.first,
                            fontSize = 12.sp,
                            fontWeight = if (selectedSeasonTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // SEARCH & FILTERS
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("फसल खोजें (Search Wheat, Rice, Cotton, Moong...)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Soil chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                soilFilters.forEach { soil ->
                    val isSelected = selectedSoilFilter == soil
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) AmberHarvest else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedSoilFilter = soil }
                    ) {
                        Text(
                            text = if (soil == "All") "सभी मिट्टियाँ" else soil,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // CROPS LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "सुझाई गई फसलें (${filteredCrops.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tap card to view full advisory",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(filteredCrops) { crop ->
                val isExpanded = expandedCropId == crop.id

                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expandedCropId = if (isExpanded) null else crop.id
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(GreenLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = crop.nameHi,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${crop.nameEn} • ${crop.category}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Season Badge
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = when (crop.season) {
                                    CropSeason.KHARIF -> GreenLight
                                    CropSeason.RABI -> InfoBlueLight
                                    CropSeason.ZAID -> AmberLight
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Text(
                                    text = crop.season.titleHi,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (crop.season) {
                                        CropSeason.KHARIF -> GreenPrimary
                                        CropSeason.RABI -> InfoBlue
                                        CropSeason.ZAID -> AmberHarvest
                                        else -> MaterialTheme.colorScheme.onSurface
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Key Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Duration
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${crop.harvestingDurationDays} दिन (Days)",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Yield
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = crop.estimatedYield,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GreenPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Profit & Water row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AmberLight
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = AmberHarvest,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "मुनाफा: ${crop.profitPotential}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberHarvest
                                    )
                                }
                            }

                            Text(
                                text = "बुवाई: ${crop.sowingMonths}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // EXPANDED DETAILS
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(14.dp))
                            androidx.compose.material3.HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Soils
                            Text(
                                text = "उपयुक्त मिट्टी: ${crop.soilTypes.joinToString(", ")}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Seed Rate & Spacing
                            Text(
                                text = "बीज दर: ${crop.seedRate} • दूरी: ${crop.spacing}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Key Agronomy Tips
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GreenLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = crop.keyTips,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            if (crop.pestResistantVarieties.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "उन्नत किस्में (Recommended Varieties): ${crop.pestResistantVarieties.joinToString(", ")}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GreenPrimaryVariant
                                )
                            }

                            if (crop.companionCrops.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "सह-फसल / मित्र फसल: ${crop.companionCrops.joinToString(", ")}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
