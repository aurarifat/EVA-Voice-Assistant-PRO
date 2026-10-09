package com.example.ai

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.data.model.VoicePersona
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var customPitchMultiplier: Float = 1.0f
    private var customSpeedMultiplier: Float = 1.0f

    init {
        try {
            tts = TextToSpeech(context, this)
        } catch (e: Exception) {
            // TTS unavailable or initialization failure
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                val result = engine.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isInitialized = true
                }
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            }
        }
    }

    // 7. Multilingual support (Bangla, Hindi, English, Banglish)
    private fun detectLanguage(text: String): Locale {
        val hasBangla = text.any { it in '\u0980'..'\u09FF' }
        val hasHindi = text.any { it in '\u0900'..'\u097F' }

        return when {
            hasBangla -> Locale("bn", "BD")
            hasHindi -> Locale("hi", "IN")
            else -> Locale.US
        }
    }

    // 20. Voice speed/pitch control
    fun setCustomPitchAndSpeed(pitch: Float, speed: Float) {
        customPitchMultiplier = pitch.coerceIn(0.5f, 2.0f)
        customSpeedMultiplier = speed.coerceIn(0.5f, 2.0f)
    }

    // 19. Different voices
    fun speak(
        text: String,
        persona: VoicePersona = VoicePersona.MAYA,
        forcedLocale: Locale? = null
    ) {
        if (!isInitialized || tts == null) return

        tts?.let { engine ->
            val targetLocale = forcedLocale ?: detectLanguage(text)
            try {
                engine.language = targetLocale
            } catch (e: Exception) {
                engine.language = Locale.US
            }

            engine.setPitch(persona.pitch * customPitchMultiplier)
            engine.setSpeechRate(persona.speed * customSpeedMultiplier)
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "eva_speech_${System.currentTimeMillis()}")
            _isSpeaking.value = true
        }
    }

    // 5. Interrupt EVA while speaking
    fun interrupt() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun stop() {
        interrupt()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
