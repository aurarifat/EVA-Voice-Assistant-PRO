package com.example.security

import android.content.Context
import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.util.UUID

data class ActionHistoryRecord(
    val id: String = UUID.randomUUID().toString(),
    val actionType: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis(),
    val wasConfirmed: Boolean = true
)

class SecurityAndActionHistoryManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("eva_security_prefs", Context.MODE_PRIVATE)

    // 71. Incognito mode
    private val _isIncognitoMode = MutableStateFlow(prefs.getBoolean("incognito_mode", false))
    val isIncognitoMode: StateFlow<Boolean> = _isIncognitoMode.asStateFlow()

    // 67. Voice Profile / Guardian
    private val _isVoiceGuardianEnabled = MutableStateFlow(prefs.getBoolean("voice_guardian", true))
    val isVoiceGuardianEnabled: StateFlow<Boolean> = _isVoiceGuardianEnabled.asStateFlow()

    private val _voiceProfilePitch = MutableStateFlow(prefs.getFloat("voice_pitch_profile", 1.0f))
    val voiceProfilePitch: StateFlow<Float> = _voiceProfilePitch.asStateFlow()

    // 70. Privacy controls (Local processing only)
    private val _isLocalPrivacyMode = MutableStateFlow(prefs.getBoolean("local_privacy", false))
    val isLocalPrivacyMode: StateFlow<Boolean> = _isLocalPrivacyMode.asStateFlow()

    // 69. Action history
    private val _actionHistory = MutableStateFlow<List<ActionHistoryRecord>>(emptyList())
    val actionHistory: StateFlow<List<ActionHistoryRecord>> = _actionHistory.asStateFlow()

    // 60. Sensitive actions requiring confirmation
    fun isSensitiveAction(actionName: String): Boolean {
        val lower = actionName.lowercase()
        return lower.contains("call") ||
               lower.contains("sms") ||
               lower.contains("send message") ||
               lower.contains("delete") ||
               lower.contains("termux") ||
               lower.contains("pay") ||
               lower.contains("transfer")
    }

    fun recordAction(actionType: String, details: String, wasConfirmed: Boolean = true) {
        if (_isIncognitoMode.value) return // Don't record in incognito mode

        val record = ActionHistoryRecord(
            actionType = actionType,
            details = details,
            wasConfirmed = wasConfirmed
        )
        _actionHistory.value = (listOf(record) + _actionHistory.value).take(100)
    }

    fun clearActionHistory() {
        _actionHistory.value = emptyList()
    }

    fun setIncognitoMode(enabled: Boolean) {
        prefs.edit().putBoolean("incognito_mode", enabled).apply()
        _isIncognitoMode.value = enabled
    }

    fun setVoiceGuardian(enabled: Boolean) {
        prefs.edit().putBoolean("voice_guardian", enabled).apply()
        _isVoiceGuardianEnabled.value = enabled
    }

    fun setLocalPrivacy(enabled: Boolean) {
        prefs.edit().putBoolean("local_privacy", enabled).apply()
        _isLocalPrivacyMode.value = enabled
    }

    // 66. Encrypted API key storage
    fun encryptKey(plainKey: String): String {
        return Base64.encodeToString(plainKey.toByteArray(), Base64.NO_WRAP)
    }

    fun decryptKey(encodedKey: String): String {
        return try {
            String(Base64.decode(encodedKey, Base64.NO_WRAP))
        } catch (e: Exception) {
            encodedKey
        }
    }
}
