package com.example.model

enum class AppLanguage(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val flag: String,
    val ttsLocaleTag: String
) {
    HINDI("hi", "Hindi", "हिन्दी", "🇮🇳", "hi-IN"),
    ENGLISH("en", "English", "English", "🌐", "en-US"),
    PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ", "🇮🇳", "pa-IN"),
    TELUGU("te", "Telugu", "తెలుగు", "🇮🇳", "te-IN"),
    TAMIL("ta", "Tamil", "தமிழ்", "🇮🇳", "ta-IN"),
    BENGALI("bn", "Bengali", "বাংলা", "🇮🇳", "bn-IN"),
    MARATHI("mr", "Marathi", "मराठी", "🇮🇳", "mr-IN"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી", "🇮🇳", "gu-IN"),
    SPANISH("es", "Spanish", "Español", "🇪🇸", "es-ES"),
    FRENCH("fr", "French", "Français", "🇫🇷", "fr-FR"),
    SWAHILI("sw", "Swahili", "Kiswahili", "🇰🇪", "sw-KE");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: HINDI
        }
    }
}
