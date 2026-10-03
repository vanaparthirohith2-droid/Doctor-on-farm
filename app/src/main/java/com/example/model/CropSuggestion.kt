package com.example.model

enum class CropSeason(val titleEn: String, val titleHi: String, val period: String) {
    ALL("All Seasons", "सभी मौसम", "Year Round"),
    KHARIF("Kharif (Monsoon)", "खरीफ (मानसून)", "June - October"),
    RABI("Rabi (Winter)", "रबी (सर्दियां)", "October - March"),
    ZAID("Zaid (Summer)", "जायद (गर्मी)", "March - June")
}

data class CropSuggestion(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val category: String, // Cereals, Pulses, Vegetables, Cash Crop, Oilseeds, Fruits
    val season: CropSeason,
    val sowingMonths: String,
    val harvestingDurationDays: Int,
    val soilTypes: List<String>,
    val waterRequirement: String, // Low, Moderate, High, Irrigated
    val estimatedYield: String,
    val profitPotential: String, // High, Very High, Moderate
    val seedRate: String,
    val spacing: String,
    val keyTips: String,
    val companionCrops: List<String> = emptyList(),
    val pestResistantVarieties: List<String> = emptyList()
)
