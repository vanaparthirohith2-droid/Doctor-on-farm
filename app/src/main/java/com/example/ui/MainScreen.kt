package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.data.OfflineAgriculturalDatabase
import com.example.model.AppLanguage
import com.example.model.DiseaseDiagnosis
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTab
import com.example.ui.screens.DiseaseDetectionScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SeasonalCropsScreen
import com.example.ui.screens.WeatherScreen
import com.example.util.TtsManager
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentLanguage by remember { mutableStateOf(AppLanguage.HINDI) }
    var selectedTab by remember { mutableStateOf(AppTab.DOCTOR) }

    // Saved scans list with initial pre-populated realistic scan
    val savedScans = remember {
        mutableStateListOf<DiseaseDiagnosis>(
            OfflineAgriculturalDatabase.getDiagnosisForSample("tomato_early_blight", AppLanguage.HINDI),
            OfflineAgriculturalDatabase.getDiagnosisForSample("wheat_yellow_rust", AppLanguage.HINDI)
        )
    }

    // TTS voice assistant manager
    val ttsManager = remember {
        TtsManager(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { newTab ->
                    ttsManager.stop()
                    selectedTab = newTab
                },
                currentLanguage = currentLanguage
            )
        }
    ) { innerPadding ->
        when (selectedTab) {
            AppTab.DOCTOR -> {
                DiseaseDetectionScreen(
                    currentLanguage = currentLanguage,
                    onLanguageChange = { newLang ->
                        currentLanguage = newLang
                    },
                    onSaveDiagnosis = { diag ->
                        if (!savedScans.any { it.id == diag.id }) {
                            savedScans.add(0, diag)
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("जांच परिणाम सुरक्षित कर लिया गया है (Saved)")
                        }
                    },
                    ttsManager = ttsManager,
                    innerPadding = innerPadding
                )
            }
            AppTab.WEATHER -> {
                WeatherScreen(
                    currentLanguage = currentLanguage,
                    innerPadding = innerPadding
                )
            }
            AppTab.SEASONS -> {
                SeasonalCropsScreen(
                    currentLanguage = currentLanguage,
                    innerPadding = innerPadding
                )
            }
            AppTab.HISTORY -> {
                HistoryScreen(
                    savedScans = savedScans,
                    onClearHistory = {
                        savedScans.clear()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("इतिहास साफ कर दिया गया (Cleared)")
                        }
                    },
                    currentLanguage = currentLanguage,
                    ttsManager = ttsManager,
                    innerPadding = innerPadding
                )
            }
        }
    }
}
