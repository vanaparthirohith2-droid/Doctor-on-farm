package com.example.model

data class TreatmentDetail(
    val medicineName: String,
    val dosage: String,
    val instructions: String,
    val safetyWarning: String = ""
)

data class DiseaseDiagnosis(
    val id: String = System.currentTimeMillis().toString(),
    val cropName: String,
    val cropScientificName: String = "",
    val diseaseName: String,
    val scientificName: String = "",
    val isHealthy: Boolean = false,
    val severityLevel: String = "Moderate", // Low, Moderate, High, Severe, Healthy
    val confidenceScore: Int = 92,
    val summary: String,
    val symptoms: List<String> = emptyList(),
    val causes: List<String> = emptyList(),
    val organicTreatments: List<String> = emptyList(),
    val chemicalTreatments: List<TreatmentDetail> = emptyList(),
    val preventiveMeasures: List<String> = emptyList(),
    val sprayWindowRecommendation: String = "",
    val languageCode: String = "hi",
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getFullNarrationText(): String {
        val sb = StringBuilder()
        if (isHealthy) {
            sb.append("फसल स्वस्थ है। ")
            sb.append(cropName).append("। ")
            sb.append(summary)
        } else {
            sb.append(cropName).append(" में ").append(diseaseName).append(" की पहचान हुई है। ")
            sb.append("गंभीरता: ").append(severityLevel).append("। ")
            sb.append(summary).append("। ")
            if (organicTreatments.isNotEmpty()) {
                sb.append("जैविक उपचार: ").append(organicTreatments.joinToString(", ")).append("। ")
            }
            if (chemicalTreatments.isNotEmpty()) {
                sb.append("रासायनिक दवा: ")
                chemicalTreatments.forEach {
                    sb.append(it.medicineName).append(" मात्रा ").append(it.dosage).append("। ")
                }
            }
        }
        return sb.toString()
    }
}
