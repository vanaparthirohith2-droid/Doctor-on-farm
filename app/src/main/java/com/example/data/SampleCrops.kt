package com.example.data

data class SampleCropItem(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val diseaseEn: String,
    val diseaseHi: String,
    val cropType: String,
    val colorHex: Long,
    val description: String
)

object SampleCropsData {
    val samples = listOf(
        SampleCropItem(
            id = "tomato_early_blight",
            nameEn = "Tomato",
            nameHi = "टमाटर",
            diseaseEn = "Early Blight (Alternaria solani)",
            diseaseHi = "अगेती झुलसा (Early Blight)",
            cropType = "Vegetable",
            colorHex = 0xFFE53935,
            description = "Concentric dark brown rings on lower leaves, yellowing margins"
        ),
        SampleCropItem(
            id = "rice_blast",
            nameEn = "Rice / Paddy",
            nameHi = "धान / चावल",
            diseaseEn = "Rice Blast (Magnaporthe oryzae)",
            diseaseHi = "धान का झोंका रोग (Rice Blast)",
            cropType = "Cereal",
            colorHex = 0xFF7CB342,
            description = "Spindle-shaped diamond spots with grey centers and brown borders"
        ),
        SampleCropItem(
            id = "wheat_yellow_rust",
            nameEn = "Wheat",
            nameHi = "गेहूं",
            diseaseEn = "Yellow Stripe Rust (Puccinia striiformis)",
            diseaseHi = "पीला रतुआ (Yellow Rust)",
            cropType = "Cereal",
            colorHex = 0xFFFDD835,
            description = "Yellow-orange powdery pustules in distinct parallel stripes along leaf veins"
        ),
        SampleCropItem(
            id = "potato_late_blight",
            nameEn = "Potato",
            nameHi = "आलू",
            diseaseEn = "Late Blight (Phytophthora infestans)",
            diseaseHi = "पछेती झुलसा (Late Blight)",
            cropType = "Tuber",
            colorHex = 0xFF6D4C41,
            description = "Water-soaked dark lesions with white fungal growth on leaf undersides"
        ),
        SampleCropItem(
            id = "cotton_leaf_curl",
            nameEn = "Cotton",
            nameHi = "कपास",
            diseaseEn = "Cotton Leaf Curl Virus (CLCuV)",
            diseaseHi = "कपास मरोड़िया रोग (Leaf Curl Virus)",
            cropType = "Cash Crop",
            colorHex = 0xFF26A69A,
            description = "Upward/downward leaf curling, vein thickening and enations"
        ),
        SampleCropItem(
            id = "maize_fall_armyworm",
            nameEn = "Maize / Corn",
            nameHi = "मक्का",
            diseaseEn = "Fall Armyworm & Corn Smut",
            diseaseHi = "मक्का कण्डवा एवं फॉल आर्मीवर्म",
            cropType = "Cereal",
            colorHex = 0xFFFFB300,
            description = "Ragged feeding holes, sawdust frass and grey swollen galls"
        ),
        SampleCropItem(
            id = "chilli_leaf_curl",
            nameEn = "Chilli / Pepper",
            nameHi = "मिर्च",
            diseaseEn = "Chilli Thrips & Leaf Curl",
            diseaseHi = "मिर्च चुर्रा-मुर्रा (थ्रिप्स व माइट्स)",
            cropType = "Spices",
            colorHex = 0xFFD81B60,
            description = "Upward boat-shaped curling, stunted growth caused by sap sucking pests"
        ),
        SampleCropItem(
            id = "healthy_crop_leaf",
            nameEn = "Healthy Green Leaf",
            nameHi = "स्वस्थ फसल की पत्ती",
            diseaseEn = "Healthy - No Disease Detected",
            diseaseHi = "स्वस्थ - कोई रोग नहीं",
            cropType = "All Crops",
            colorHex = 0xFF43A047,
            description = "Vibrant green, uniform texture, no spots, lesions or curling"
        )
    )
}
