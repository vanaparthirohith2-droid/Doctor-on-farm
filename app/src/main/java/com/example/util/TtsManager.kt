package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.model.AppLanguage
import java.util.Locale

class TtsManager(context: Context, onReady: () -> Unit = {}) {

    private var tts: TextToSpeech? = null
    var isSpeaking = false
        private set

    private var onDoneCallback: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        isSpeaking = true
                    }

                    override fun onDone(utteranceId: String?) {
                        isSpeaking = false
                        onDoneCallback?.invoke()
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        isSpeaking = false
                        onDoneCallback?.invoke()
                    }
                })
                onReady()
            }
        }
    }

    fun speak(text: String, language: AppLanguage, onDone: () -> Unit = {}) {
        this.onDoneCallback = onDone
        val locale = Locale.forLanguageTag(language.ttsLocaleTag)
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // fallback to English or default
            tts?.setLanguage(Locale.ENGLISH)
        }
        tts?.setSpeechRate(0.92f) // Slightly slower for clarity for farmers
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "KisanSevaTts_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
        isSpeaking = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
