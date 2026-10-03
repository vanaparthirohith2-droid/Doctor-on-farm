package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary

enum class AppTab(val icon: ImageVector) {
    DOCTOR(Icons.Default.LocalFlorist),
    WEATHER(Icons.Default.Cloud),
    SEASONS(Icons.Default.CalendarMonth),
    HISTORY(Icons.Default.History);

    fun getLabel(language: AppLanguage): String {
        return when (this) {
            DOCTOR -> when (language) {
                AppLanguage.HINDI -> "फसल डॉक्टर"
                AppLanguage.PUNJABI -> "ਫਸਲ ਡਾਕਟਰ"
                AppLanguage.TELUGU -> "పంట డాక్టర్"
                AppLanguage.TAMIL -> "பயிர் மருத்துவர்"
                AppLanguage.BENGALI -> "ফসল ডাক্তার"
                AppLanguage.MARATHI -> "पीक डॉक्टर"
                AppLanguage.GUJARATI -> "પાક ડૉક્ટર"
                AppLanguage.SPANISH -> "Diagnóstico"
                else -> "Crop Doctor"
            }
            WEATHER -> when (language) {
                AppLanguage.HINDI -> "मौसम व स्प्रे"
                AppLanguage.PUNJABI -> "ਮੌਸਮ ਤੇ ਸਪਰੇਅ"
                AppLanguage.TELUGU -> "వాతావరణం"
                AppLanguage.TAMIL -> "வானிலை"
                AppLanguage.BENGALI -> "আবহাওয়া"
                AppLanguage.MARATHI -> "हवामान"
                AppLanguage.GUJARATI -> "હવામાન"
                AppLanguage.SPANISH -> "Clima"
                else -> "Weather"
            }
            SEASONS -> when (language) {
                AppLanguage.HINDI -> "मौसमी फसलें"
                AppLanguage.PUNJABI -> "ਮੌਸਮੀ ਫਸਲਾਂ"
                AppLanguage.TELUGU -> "సీజన్ పంటలు"
                AppLanguage.TAMIL -> "பருவ பயிர்கள்"
                AppLanguage.BENGALI -> "মৌসুমি ফসল"
                AppLanguage.MARATHI -> "हंगामी पिके"
                AppLanguage.GUJARATI -> "ઋતુગત પાક"
                AppLanguage.SPANISH -> "Cultivos"
                else -> "Seasons"
            }
            HISTORY -> when (language) {
                AppLanguage.HINDI -> "इतिहास"
                AppLanguage.PUNJABI -> "ਇਤਿਹਾਸ"
                AppLanguage.TELUGU -> "చరిత్ర"
                AppLanguage.TAMIL -> "வரலாறு"
                AppLanguage.BENGALI -> "ইতিহাস"
                AppLanguage.MARATHI -> "इतिहास"
                AppLanguage.GUJARATI -> "ઇતિહાસ"
                AppLanguage.SPANISH -> "Historial"
                else -> "History"
            }
        }
    }
}

@Composable
fun AppBottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    currentLanguage: AppLanguage
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        AppTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.getLabel(currentLanguage)
                    )
                },
                label = {
                    Text(
                        text = tab.getLabel(currentLanguage),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    indicatorColor = GreenContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
