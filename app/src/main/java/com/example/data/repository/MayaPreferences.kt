package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.model.CharacterStyle
import com.example.data.model.VoicePersona
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MayaPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("maya_ai_prefs", Context.MODE_PRIVATE)

    private val _isSetupCompleted = MutableStateFlow(prefs.getBoolean("setup_completed", false))
    val isSetupCompleted: StateFlow<Boolean> = _isSetupCompleted.asStateFlow()

    private val _geminiApiKey = MutableStateFlow(
        prefs.getString("gemini_api_key", null) ?: BuildConfig.GEMINI_API_KEY
    )
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _themeMode = MutableStateFlow(prefs.getString("theme_mode", "dark") ?: "dark")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _themeMode.value = mode
    }

    private val _isVoiceModeOn = MutableStateFlow(prefs.getBoolean("voice_mode_on", false))
    val isVoiceModeOn: StateFlow<Boolean> = _isVoiceModeOn.asStateFlow()

    private val _isLiveCornerEnabled = MutableStateFlow(prefs.getBoolean("live_corner_enabled", true))
    val isLiveCornerEnabled: StateFlow<Boolean> = _isLiveCornerEnabled.asStateFlow()

    private val _selectedPersona = MutableStateFlow(
        VoicePersona.values().find { it.id == prefs.getString("voice_persona", "maya") } ?: VoicePersona.MAYA
    )
    val selectedPersona: StateFlow<VoicePersona> = _selectedPersona.asStateFlow()

    private val _selectedCharacter = MutableStateFlow(
        CharacterStyle.values().find { it.id == prefs.getString("character_style", "maya_studio") } ?: CharacterStyle.MAYA_STUDIO
    )
    val selectedCharacter: StateFlow<CharacterStyle> = _selectedCharacter.asStateFlow()

    private val _isAgenticVoiceMode = MutableStateFlow(prefs.getBoolean("agentic_voice_mode", true))
    val isAgenticVoiceMode: StateFlow<Boolean> = _isAgenticVoiceMode.asStateFlow()

    private val _voiceStyle = MutableStateFlow(prefs.getString("voice_style", "Clear") ?: "Clear")
    val voiceStyle: StateFlow<String> = _voiceStyle.asStateFlow()

    // Screen control toggles
    private val _isScreenControlEnabled = MutableStateFlow(prefs.getBoolean("screen_control", true))
    val isScreenControlEnabled: StateFlow<Boolean> = _isScreenControlEnabled.asStateFlow()

    private val _isCoordinateTapEnabled = MutableStateFlow(prefs.getBoolean("coordinate_tap", true))
    val isCoordinateTapEnabled: StateFlow<Boolean> = _isCoordinateTapEnabled.asStateFlow()

    private val _isAccessibilityControlEnabled = MutableStateFlow(prefs.getBoolean("accessibility_control", true))
    val isAccessibilityControlEnabled: StateFlow<Boolean> = _isAccessibilityControlEnabled.asStateFlow()

    private val _isAutoScrollEnabled = MutableStateFlow(prefs.getBoolean("auto_scroll", true))
    val isAutoScrollEnabled: StateFlow<Boolean> = _isAutoScrollEnabled.asStateFlow()

    private val _isBackNavEnabled = MutableStateFlow(prefs.getBoolean("back_nav", true))
    val isBackNavEnabled: StateFlow<Boolean> = _isBackNavEnabled.asStateFlow()

    private val _isRecentAppsEnabled = MutableStateFlow(prefs.getBoolean("recent_apps", true))
    val isRecentAppsEnabled: StateFlow<Boolean> = _isRecentAppsEnabled.asStateFlow()

    private val _isBackgroundExecutionEnabled = MutableStateFlow(prefs.getBoolean("background_exec", true))
    val isBackgroundExecutionEnabled: StateFlow<Boolean> = _isBackgroundExecutionEnabled.asStateFlow()

    // WhatsApp & Social Media
    private val _isWhatsAppAutoReplyEnabled = MutableStateFlow(prefs.getBoolean("whatsapp_auto_reply", false))
    val isWhatsAppAutoReplyEnabled: StateFlow<Boolean> = _isWhatsAppAutoReplyEnabled.asStateFlow()

    private val _whatsAppReplyStyle = MutableStateFlow(prefs.getString("whatsapp_style", "Friendly & Professional") ?: "Friendly & Professional")
    val whatsAppReplyStyle: StateFlow<String> = _whatsAppReplyStyle.asStateFlow()

    private val _isReelsAutoScrollEnabled = MutableStateFlow(prefs.getBoolean("reels_auto_scroll", false))
    val isReelsAutoScrollEnabled: StateFlow<Boolean> = _isReelsAutoScrollEnabled.asStateFlow()

    // Typing
    private val _typingSpeed = MutableStateFlow(prefs.getString("typing_speed", "Normal") ?: "Normal")
    val typingSpeed: StateFlow<String> = _typingSpeed.asStateFlow()

    private val _isHumanPacedTyping = MutableStateFlow(prefs.getBoolean("human_paced_typing", true))
    val isHumanPacedTyping: StateFlow<Boolean> = _isHumanPacedTyping.asStateFlow()

    private val _isCodingTyping = MutableStateFlow(prefs.getBoolean("coding_typing", true))
    val isCodingTyping: StateFlow<Boolean> = _isCodingTyping.asStateFlow()

    // Proactive Maya
    private val _isProactiveConversationsEnabled = MutableStateFlow(prefs.getBoolean("proactive_chat", true))
    val isProactiveConversationsEnabled: StateFlow<Boolean> = _isProactiveConversationsEnabled.asStateFlow()

    private val _isAutoStartBootEnabled = MutableStateFlow(prefs.getBoolean("auto_start_boot", true))
    val isAutoStartBootEnabled: StateFlow<Boolean> = _isAutoStartBootEnabled.asStateFlow()

    private val _wakeWord = MutableStateFlow(prefs.getString("wake_word", "Hey Maya") ?: "Hey Maya")
    val wakeWord: StateFlow<String> = _wakeWord.asStateFlow()

    // SMTP Email
    private val _smtpHost = MutableStateFlow(prefs.getString("smtp_host", "smtp.gmail.com") ?: "smtp.gmail.com")
    val smtpHost: StateFlow<String> = _smtpHost.asStateFlow()

    private val _smtpPort = MutableStateFlow(prefs.getString("smtp_port", "587") ?: "587")
    val smtpPort: StateFlow<String> = _smtpPort.asStateFlow()

    private val _smtpUser = MutableStateFlow(prefs.getString("smtp_user", "") ?: "")
    val smtpUser: StateFlow<String> = _smtpUser.asStateFlow()

    private val _emailSignature = MutableStateFlow(prefs.getString("email_signature", "Regards,\nMaya User (via Maya AI)") ?: "Regards,\nMaya User (via Maya AI)")
    val emailSignature: StateFlow<String> = _emailSignature.asStateFlow()

    // Telegram Bot
    private val _telegramBotToken = MutableStateFlow(prefs.getString("telegram_token", "") ?: "")
    val telegramBotToken: StateFlow<String> = _telegramBotToken.asStateFlow()

    fun setSetupCompleted(completed: Boolean) {
        prefs.edit().putBoolean("setup_completed", completed).apply()
        _isSetupCompleted.value = completed
    }

    fun setGeminiApiKey(key: String) {
        prefs.edit().putString("gemini_api_key", key).apply()
        _geminiApiKey.value = key
    }

    fun setVoiceMode(enabled: Boolean) {
        prefs.edit().putBoolean("voice_mode_on", enabled).apply()
        _isVoiceModeOn.value = enabled
    }

    fun setLiveCornerEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("live_corner_enabled", enabled).apply()
        _isLiveCornerEnabled.value = enabled
    }

    fun setVoicePersona(persona: VoicePersona) {
        prefs.edit().putString("voice_persona", persona.id).apply()
        _selectedPersona.value = persona
    }

    fun setCharacterStyle(style: CharacterStyle) {
        prefs.edit().putString("character_style", style.id).apply()
        _selectedCharacter.value = style
    }

    fun setAgenticVoiceMode(agentic: Boolean) {
        prefs.edit().putBoolean("agentic_voice_mode", agentic).apply()
        _isAgenticVoiceMode.value = agentic
    }

    fun setVoiceStyle(style: String) {
        prefs.edit().putString("voice_style", style).apply()
        _voiceStyle.value = style
    }

    fun setScreenControl(enabled: Boolean) {
        prefs.edit().putBoolean("screen_control", enabled).apply()
        _isScreenControlEnabled.value = enabled
    }

    fun setCoordinateTap(enabled: Boolean) {
        prefs.edit().putBoolean("coordinate_tap", enabled).apply()
        _isCoordinateTapEnabled.value = enabled
    }

    fun setAccessibilityControl(enabled: Boolean) {
        prefs.edit().putBoolean("accessibility_control", enabled).apply()
        _isAccessibilityControlEnabled.value = enabled
    }

    fun setAutoScroll(enabled: Boolean) {
        prefs.edit().putBoolean("auto_scroll", enabled).apply()
        _isAutoScrollEnabled.value = enabled
    }

    fun setBackNav(enabled: Boolean) {
        prefs.edit().putBoolean("back_nav", enabled).apply()
        _isBackNavEnabled.value = enabled
    }

    fun setRecentApps(enabled: Boolean) {
        prefs.edit().putBoolean("recent_apps", enabled).apply()
        _isRecentAppsEnabled.value = enabled
    }

    fun setBackgroundExecution(enabled: Boolean) {
        prefs.edit().putBoolean("background_exec", enabled).apply()
        _isBackgroundExecutionEnabled.value = enabled
    }

    fun setWhatsAppAutoReply(enabled: Boolean, style: String = _whatsAppReplyStyle.value) {
        prefs.edit().putBoolean("whatsapp_auto_reply", enabled).putString("whatsapp_style", style).apply()
        _isWhatsAppAutoReplyEnabled.value = enabled
        _whatsAppReplyStyle.value = style
    }

    fun setReelsAutoScroll(enabled: Boolean) {
        prefs.edit().putBoolean("reels_auto_scroll", enabled).apply()
        _isReelsAutoScrollEnabled.value = enabled
    }

    fun setTypingSettings(speed: String, humanPaced: Boolean, codingTyping: Boolean) {
        prefs.edit().putString("typing_speed", speed).putBoolean("human_paced_typing", humanPaced).putBoolean("coding_typing", codingTyping).apply()
        _typingSpeed.value = speed
        _isHumanPacedTyping.value = humanPaced
        _isCodingTyping.value = codingTyping
    }

    fun setProactiveChat(enabled: Boolean) {
        prefs.edit().putBoolean("proactive_chat", enabled).apply()
        _isProactiveConversationsEnabled.value = enabled
    }

    fun setWakeWord(word: String) {
        prefs.edit().putString("wake_word", word).apply()
        _wakeWord.value = word
    }

    fun setEmailConfig(host: String, port: String, user: String, signature: String) {
        prefs.edit().putString("smtp_host", host).putString("smtp_port", port).putString("smtp_user", user).putString("email_signature", signature).apply()
        _smtpHost.value = host
        _smtpPort.value = port
        _smtpUser.value = user
        _emailSignature.value = signature
    }

    fun setTelegramBotToken(token: String) {
        prefs.edit().putString("telegram_token", token).apply()
        _telegramBotToken.value = token
    }
}
