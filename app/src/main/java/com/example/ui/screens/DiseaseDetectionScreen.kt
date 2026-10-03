package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GeminiService
import com.example.data.OfflineAgriculturalDatabase
import com.example.data.SampleCropItem
import com.example.data.SampleCropsData
import com.example.model.AppLanguage
import com.example.model.DiseaseDiagnosis
import com.example.ui.components.CropCameraCaptureView
import com.example.ui.components.LanguagePill
import com.example.ui.components.LanguageSelectionDialog
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
import com.example.util.BitmapUtils
import com.example.util.TtsManager
import kotlinx.coroutines.launch

@Composable
fun DiseaseDetectionScreen(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onSaveDiagnosis: (DiseaseDiagnosis) -> Unit,
    ttsManager: TtsManager?,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCameraView by remember { mutableStateOf(false) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedSample by remember { mutableStateOf<SampleCropItem?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var currentDiagnosis by remember { mutableStateOf<DiseaseDiagnosis?>(null) }
    var isSpeakingAudio by remember { mutableStateOf(false) }
    var treatmentTab by remember { mutableIntStateOf(0) } // 0 = Organic, 1 = Chemical

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            selectedBitmap = bitmap
            selectedSample = null
            currentDiagnosis = null
            // Trigger auto analysis
            coroutineScope.launch {
                isAnalyzing = true
                currentDiagnosis = GeminiService.analyzeCropImage(bitmap, currentLanguage)
                isAnalyzing = false
            }
        }
    }

    // Gallery picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bitmap = BitmapUtils.uriToBitmap(context, uri)
            if (bitmap != null) {
                selectedBitmap = bitmap
                selectedSample = null
                currentDiagnosis = null
                coroutineScope.launch {
                    isAnalyzing = true
                    currentDiagnosis = GeminiService.analyzeCropImage(bitmap, currentLanguage, uri.toString())
                    isAnalyzing = false
                }
            }
        }
    }

    // Re-analyze when language changes if a sample or diagnosis is active
    LaunchedEffect(currentLanguage) {
        if (selectedSample != null) {
            currentDiagnosis = OfflineAgriculturalDatabase.getDiagnosisForSample(selectedSample!!.id, currentLanguage)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // TOP HEADER BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GreenPrimary, GreenPrimaryVariant)
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
                            imageVector = Icons.Default.LocalFlorist,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "KisanSeva Doctor",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.HINDI -> "फसल रोग पहचान व उपचार"
                                AppLanguage.PUNJABI -> "ਫਸਲ ਰੋਗ ਜਾਂਚ ਅਤੇ ਇਲਾਜ"
                                AppLanguage.TELUGU -> "పంట తెగుళ్ల గుర్తింపు"
                                AppLanguage.TAMIL -> "பயிர் நோய் அறிதல்"
                                AppLanguage.BENGALI -> "ফসলের রোগ নির্ণয় ও চিকিৎসা"
                                AppLanguage.MARATHI -> "पीक रोग ओळख व उपाय"
                                AppLanguage.GUJARATI -> "પાક રોગ નિદાન અને ઉપચાર"
                                AppLanguage.SPANISH -> "Diagnóstico y Tratamiento"
                                else -> "Crop Disease & Treatment"
                            },
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }

                LanguagePill(
                    currentLanguage = currentLanguage,
                    onClick = { showLanguageDialog = true }
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // ACTION CARDS: CAMERA & GALLERY
            Text(
                text = when (currentLanguage) {
                    AppLanguage.HINDI -> "रोगग्रस्त पत्ती या पौधे की फोटो लें"
                    AppLanguage.PUNJABI -> "ਰੋਗੀ ਪੱਤੇ ਜਾਂ ਪੌਦੇ ਦੀ ਫੋਟੋ ਲਵੋ"
                    AppLanguage.TELUGU -> "తెగులు సోకిన ఆకు ఫోటో తీయండి"
                    AppLanguage.TAMIL -> "பாதிக்கப்பட்ட இலையின் புகைப்படம் எடுக்கவும்"
                    AppLanguage.BENGALI -> "আক্রান্ত পাতার ছবি তুলুন"
                    AppLanguage.MARATHI -> "रोगट पाण्याचे किंवा झाडाचे छायाचित्र घ्या"
                    AppLanguage.GUJARATI -> "રોગગ્રસ્ત પાંદડાનો ફોટો લો"
                    AppLanguage.SPANISH -> "Tome una foto de la hoja enferma"
                    else -> "Capture or Upload Diseased Leaf"
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live Camera Button
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenLight),
                    border = BorderStroke(1.5.dp, GreenPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showCameraView = true }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Camera",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.HINDI -> "कैमरा (फोटो खींचें)"
                                AppLanguage.PUNJABI -> "ਕੈਮਰਾ (ਫੋਟੋ ਖਿੱਚੋ)"
                                AppLanguage.TELUGU -> "కెమెరా"
                                AppLanguage.TAMIL -> "கேமரா"
                                AppLanguage.BENGALI -> "ক্যামেরা"
                                AppLanguage.MARATHI -> "कॅमेरा"
                                AppLanguage.GUJARATI -> "કેમેરા"
                                else -> "Camera Scan"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = GreenPrimary
                        )
                        Text(
                            text = "Live Leaf Photo",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Gallery Button
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { galleryLauncher.launch("image/*") }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Gallery",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.HINDI -> "गैलरी से चुनें"
                                AppLanguage.PUNJABI -> "ਗੈਲਰੀ ਤੋਂ ਚੁਣੋ"
                                AppLanguage.TELUGU -> "గ్యాలరీ"
                                AppLanguage.TAMIL -> "கேலரி"
                                AppLanguage.BENGALI -> "গ্যালারি"
                                AppLanguage.MARATHI -> "गॅलरी"
                                AppLanguage.GUJARATI -> "ગેલેરી"
                                else -> "From Gallery"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Choose File",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // QUICK TEST SAMPLES ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentLanguage) {
                        AppLanguage.HINDI -> "तुरंत जांचें (नमूना पत्तियां)"
                        AppLanguage.PUNJABI -> "ਨਮੂਨਾ ਪੱਤੇ ਪਰਖੋ"
                        AppLanguage.TELUGU -> "నమూనా ఆకులు తనిఖీ చేయండి"
                        AppLanguage.TAMIL -> "மாதிரி இலைகள்"
                        AppLanguage.BENGALI -> "নমুনা পাতা পরীক্ষা"
                        AppLanguage.MARATHI -> "नमुना पाने तपासा"
                        AppLanguage.GUJARATI -> "નમૂના પાંદડા ચકાસો"
                        else -> "Quick Test Sample Leaves"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap to Diagnose",
                    fontSize = 11.sp,
                    color = GreenPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SampleCropsData.samples.forEach { sample ->
                    val isSelected = selectedSample?.id == sample.id
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) GreenLight else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .width(140.dp)
                            .clickable {
                                selectedSample = sample
                                selectedBitmap = null
                                isAnalyzing = true
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(250)
                                    currentDiagnosis = OfflineAgriculturalDatabase.getDiagnosisForSample(
                                        sample.id,
                                        currentLanguage
                                    )
                                    isAnalyzing = false
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(sample.colorHex).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Eco,
                                        contentDescription = sample.nameEn,
                                        tint = Color(sample.colorHex),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) sample.nameHi else sample.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.HINDI) sample.diseaseHi else sample.diseaseEn,
                                fontSize = 11.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SELECTED IMAGE PREVIEW & STATUS
            if (selectedBitmap != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            bitmap = selectedBitmap!!.asImageBitmap(),
                            contentDescription = "Selected Crop Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        IconButton(
                            onClick = {
                                selectedBitmap = null
                                currentDiagnosis = null
                                ttsManager?.stop()
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // LOADING PROGRESS STATE
            if (isAnalyzing) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = GreenPrimary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.HINDI -> "पत्ती व रोग के लक्षणों का विश्लेषण हो रहा है..."
                                    AppLanguage.PUNJABI -> "ਪੱਤੇ ਅਤੇ ਬਿਮਾਰੀ ਦੇ ਲੱਛਣਾਂ ਦੀ ਜਾਂਚ ਹੋ ਰਹੀ ਹੈ..."
                                    AppLanguage.TELUGU -> "పంట తెగులు విశ్లేషించబడుతోంది..."
                                    AppLanguage.TAMIL -> "பயிர் நோய் பகுப்பாய்வு செய்யப்படுகிறது..."
                                    AppLanguage.BENGALI -> "পাতার রোগ বিশ্লেষণ করা হচ্ছে..."
                                    AppLanguage.MARATHI -> "पानावरील रोगाचे विश्लेषण चालू आहे..."
                                    AppLanguage.GUJARATI -> "પાંદડાના રોગનું વિશ્લેષણ થઈ રહ્યું છે..."
                                    else -> "Analyzing plant pathology in ${currentLanguage.nativeName}..."
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = GreenPrimary
                            )
                            Text(
                                text = "Preparing organic & chemical dosage guidance...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // DIAGNOSIS RESULT DETAILS
            currentDiagnosis?.let { diag ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        // Result Header Card
                        ElevatedCard(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (diag.isHealthy) GreenLight else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Status Badge
                                    val (badgeBg, badgeText, badgeColor) = when {
                                        diag.isHealthy -> Triple(GreenContainer, "स्वस्थ / Healthy", GreenPrimary)
                                        diag.severityLevel.equals("Critical", true) || diag.severityLevel.equals("Severe", true) ->
                                            Triple(AlertRedLight, "गंभीर / ${diag.severityLevel}", AlertRed)
                                        diag.severityLevel.equals("High", true) ->
                                            Triple(AlertRedLight, "उच्च / High", AlertRed)
                                        else -> Triple(AmberLight, "मध्यम / ${diag.severityLevel}", AmberHarvest)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = badgeBg
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (diag.isHealthy) Icons.Default.CheckCircle else Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = badgeColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = badgeText,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = badgeColor
                                            )
                                        }
                                    }

                                    // Confidence Score
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = InfoBlueLight
                                    ) {
                                        Text(
                                            text = "विश्वास: ${diag.confidenceScore}%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InfoBlue,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Disease & Crop Name
                                Text(
                                    text = diag.diseaseName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (diag.isHealthy) GreenPrimary else AlertRed
                                )
                                Text(
                                    text = "${diag.cropName} • ${diag.scientificName}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Farmer Summary
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = diag.summary,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // AUDIO NARRATION BUTTON & SAVE BUTTON
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (isSpeakingAudio) {
                                                ttsManager?.stop()
                                                isSpeakingAudio = false
                                            } else {
                                                isSpeakingAudio = true
                                                val narration = diag.getFullNarrationText()
                                                ttsManager?.speak(narration, currentLanguage) {
                                                    isSpeakingAudio = false
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSpeakingAudio) AlertRed else GreenPrimary
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = if (isSpeakingAudio) Icons.Default.Stop else Icons.Default.VolumeUp,
                                            contentDescription = "Listen",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isSpeakingAudio) "रोकें (Stop)" else "सुनें (Listen Voice)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            onSaveDiagnosis(diag)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.BookmarkBorder,
                                            contentDescription = "Save",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "सुरक्षित करें", fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // SYMPTOMS SECTION
                        if (diag.symptoms.isNotEmpty()) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "पहचाने गए मुख्य लक्षण (Visible Symptoms)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    diag.symptoms.forEach { symptom ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 3.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(text = "• ", color = AmberHarvest, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = symptom,
                                                fontSize = 13.sp,
                                                lineHeight = 18.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // TREATMENT SECTION WITH TABS: ORGANIC vs CHEMICAL
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "उपचार व समाधान (Treatment & Dosage)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = GreenPrimary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                TabRow(
                                    selectedTabIndex = treatmentTab,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                ) {
                                    Tab(
                                        selected = treatmentTab == 0,
                                        onClick = { treatmentTab = 0 },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Eco,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = if (treatmentTab == 0) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "जैविक / देशी उपचार",
                                                    fontWeight = if (treatmentTab == 0) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    )
                                    Tab(
                                        selected = treatmentTab == 1,
                                        onClick = { treatmentTab = 1 },
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Medication,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = if (treatmentTab == 1) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "रासायनिक दवा व मात्रा",
                                                    fontWeight = if (treatmentTab == 1) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                if (treatmentTab == 0) {
                                    // Organic Treatments
                                    if (diag.organicTreatments.isEmpty()) {
                                        Text(
                                            text = "सामान्य जैविक पोषण व जीवामृत का प्रयोग करें।",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    } else {
                                        diag.organicTreatments.forEachIndexed { idx, org ->
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = GreenLight,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(12.dp),
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .clip(CircleShape)
                                                            .background(GreenPrimary),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = "${idx + 1}",
                                                            color = Color.White,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = org,
                                                        fontSize = 13.sp,
                                                        lineHeight = 19.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Chemical Treatments & Dosages
                                    if (diag.chemicalTreatments.isEmpty()) {
                                        Text(
                                            text = "कोई रासायनिक दवा आवश्यक नहीं है।",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    } else {
                                        diag.chemicalTreatments.forEach { chem ->
                                            Card(
                                                shape = RoundedCornerShape(12.dp),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = chem.medicineName,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 14.sp,
                                                            color = GreenPrimary
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = AmberLight
                                                    ) {
                                                        Text(
                                                            text = "मात्रा (Dosage): ${chem.dosage}",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 12.sp,
                                                            color = AmberHarvest,
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(
                                                        text = chem.instructions,
                                                        fontSize = 13.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    if (chem.safetyWarning.isNotEmpty()) {
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                imageVector = Icons.Default.Shield,
                                                                contentDescription = "Warning",
                                                                tint = AlertRed,
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = chem.safetyWarning,
                                                                fontSize = 11.sp,
                                                                color = AlertRed
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // SPRAY RECOMMENDATION ADVISORY
                        if (diag.sprayWindowRecommendation.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = InfoBlueLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Spray Advice",
                                        tint = InfoBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "छिड़काव का सही समय (Spray Window)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = InfoBlue
                                        )
                                        Text(
                                            text = diag.sprayWindowRecommendation,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // PREVENTIVE MEASURES
                        if (diag.preventiveMeasures.isNotEmpty()) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "आगे के बचाव के उपाय (Prevention Tips)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    diag.preventiveMeasures.forEach { prev ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(text = "✓ ", color = GreenPrimary, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = prev,
                                                fontSize = 12.sp,
                                                lineHeight = 17.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }

    // Language selection dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            selectedLanguage = currentLanguage,
            onLanguageSelected = onLanguageChange,
            onDismiss = { showLanguageDialog = false }
        )
    }

    // CameraX Live Viewfinder & Capture Overlay
    if (showCameraView) {
        CropCameraCaptureView(
            currentLanguage = currentLanguage,
            onPhotoCaptured = { bitmap ->
                showCameraView = false
                selectedBitmap = bitmap
                selectedSample = null
                currentDiagnosis = null
                // Initiate disease detection flow with captured photo
                coroutineScope.launch {
                    isAnalyzing = true
                    currentDiagnosis = GeminiService.analyzeCropImage(bitmap, currentLanguage)
                    isAnalyzing = false
                }
            },
            onDismiss = {
                showCameraView = false
            }
        )
    }
}
