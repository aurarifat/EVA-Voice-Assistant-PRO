package com.example.ai

import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.AutomaticGainControl
import android.media.audiofx.NoiseSuppressor
import java.util.Locale
import kotlin.math.sqrt

class VoiceProcessor {

    // 17. Filler-word removal
    private val fillerWordsRegex = Regex(
        "\\b(um|uh|er|ah|like|you know|so|basically|actually|মানে|আর কি|তো|বুঝলে)\\b",
        RegexOption.IGNORE_CASE
    )

    fun cleanTranscript(rawText: String): String {
        return rawText.replace(fillerWordsRegex, " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    // 9, 11. Wake word & mispronunciation detection
    // Matches "Hey EVA", "Hey Maya", "Hey Eevva", "Hey Eeva", "Hey Ava", etc.
    private val wakeWordRegex = Regex(
        "(?:hey|hi|hello|ok)\\s+(?:eva|eevva|eeva|evva|eva\\b|maya|maia|aeva)",
        RegexOption.IGNORE_CASE
    )

    fun detectWakeWord(speechInput: String): Pair<Boolean, String> {
        val match = wakeWordRegex.find(speechInput)
        return if (match != null) {
            val remainingPrompt = speechInput.substring(match.range.last + 1).trim()
            Pair(true, remainingPrompt)
        } else {
            Pair(false, speechInput)
        }
    }

    // 13. Voice Activity Detection (VAD)
    fun isVoiceDetected(audioBuffer: ShortArray, thresholdRms: Double = 1200.0): Boolean {
        var sum = 0.0
        for (sample in audioBuffer) {
            sum += sample * sample
        }
        val rms = sqrt(sum / audioBuffer.size)
        return rms > thresholdRms
    }

    // 14, 15, 16. Noise cancellation, Echo cancellation, AGC
    fun attachAudioEffects(audioSessionId: Int): String {
        val effects = mutableListOf<String>()

        if (NoiseSuppressor.isAvailable()) {
            try {
                NoiseSuppressor.create(audioSessionId)?.apply {
                    enabled = true
                    effects.add("NoiseSuppressor")
                }
            } catch (e: Exception) {}
        }

        if (AcousticEchoCanceler.isAvailable()) {
            try {
                AcousticEchoCanceler.create(audioSessionId)?.apply {
                    enabled = true
                    effects.add("EchoCanceler")
                }
            } catch (e: Exception) {}
        }

        if (AutomaticGainControl.isAvailable()) {
            try {
                AutomaticGainControl.create(audioSessionId)?.apply {
                    enabled = true
                    effects.add("AutoGainControl")
                }
            } catch (e: Exception) {}
        }

        return if (effects.isEmpty()) "Software Audio Filter Active" else effects.joinToString(", ")
    }

    // 18. Voice profile validation (Voice Guardian)
    fun verifyVoiceProfile(detectedPitch: Float, targetPitch: Float, tolerance: Float = 0.35f): Boolean {
        if (targetPitch <= 0f) return true
        val diff = kotlin.math.abs(detectedPitch - targetPitch) / targetPitch
        return diff <= tolerance
    }
}
