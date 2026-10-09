package com.example.data.model

enum class AgentStatus(
    val title: String,
    val description: String,
    val iconName: String
) {
    IDLE("READY", "Maya is standing by", "CheckCircle"),
    LISTENING("LISTENING", "Listening to your voice...", "Mic"),
    THINKING("THINKING", "Processing command and planning task...", "Psychology"),
    ANALYSING("ANALYSING SCREEN", "Reading screen UI elements...", "Visibility"),
    LOCATING("LOCATING", "Locating target button / field...", "Adjust"),
    EXECUTING("EXECUTING", "Performing touch / click action...", "TouchApp"),
    SCROLLING("SCROLLING", "Scrolling content...", "SwapVert"),
    NAVIGATING("NAVIGATING", "Performing Back / Home / App Switch...", "Navigation"),
    SPEAKING("SPEAKING", "Maya is speaking...", "VolumeUp"),
    WAITING_INPUT("WAITING FOR YOU", "Waiting for your next instruction...", "HourglassTop"),
    COMPLETED("TASK COMPLETED", "Task completed successfully!", "DoneAll"),
    ERROR("ACTION FAILED", "Encountered an obstacle", "ErrorOutline")
}

data class TaskStep(
    val id: String,
    val stepNumber: Int,
    val description: String,
    val actionType: ActionType,
    val targetApp: String? = null,
    val targetElement: String? = null,
    val coordinateX: Int? = null,
    val coordinateY: Int? = null,
    var isCompleted: Boolean = false,
    var isExecuting: Boolean = false,
    var error: String? = null
)

enum class ActionType {
    OPEN_APP,
    READ_SCREEN,
    LOCATE_ELEMENT,
    ACCESSIBILITY_CLICK,
    COORDINATE_TAP,
    TYPE_TEXT,
    SCROLL_DOWN,
    SCROLL_UP,
    SYSTEM_BACK,
    SYSTEM_HOME,
    SYSTEM_RECENTS,
    WAIT_FOR_SPEECH,
    SPEAK_REPLY,
    SET_VOLUME,
    VOLUME_UP,
    VOLUME_DOWN,
    OPEN_SETTINGS,
    MEDIA_PLAY_PAUSE,
    MEDIA_NEXT,
    PHONE_CALL,
    SEND_SMS,
    WHATSAPP_ACTION,
    TERMUX_COMMAND,
    SENSITIVE_CONFIRMATION
}

data class MemoryItem(
    val id: String,
    val title: String,
    val content: String,
    val category: String, // "Personal", "Work", "Preference", "Automation"
    val timestamp: Long = System.currentTimeMillis()
)

data class TrainedTask(
    val id: String,
    val name: String,
    val description: String,
    val source: String, // "Gemini", "ChatGPT", "Instagram", "WhatsApp", "System"
    val triggerPhrase: String,
    val steps: List<TaskStep>,
    val runCount: Int = 0
)

data class SkillItem(
    val id: String,
    val name: String,
    val category: String, // "WhatsApp", "YouTube", "Social Media", "Coding", "Web", "Productivity", "Study", "Automation"
    val description: String,
    val icon: String,
    val isInstalled: Boolean = true
)

enum class VoicePersona(val id: String, val displayName: String, val tone: String, val pitch: Float, val speed: Float) {
    MAYA("maya", "Maya", "Clear, empathetic & adaptive AI assistant", 1.15f, 1.0f),
    FRIDAY("friday", "Friday", "Warm, efficient tactical companion", 1.05f, 1.05f),
    VENOM("venom", "Venom", "Deep, commanding & futuristic", 0.75f, 0.95f),
    JARVIS("jarvis", "Jarvis", "Sophisticated, calm British tone", 0.95f, 1.0f)
}

enum class CharacterStyle(val id: String, val displayName: String, val tier: String, val colorHex: Long) {
    MAYA_STUDIO("maya_studio", "Maya Studio", "FREE", 0xFF00F0FF),
    MAYA_ANIME("maya_anime", "Maya Anime", "FREE", 0xFFA855F7),
    MAYA_VIOLET("maya_violet", "Maya Violet", "PRO", 0xFF8B5CF6),
    MAYA_GREEN("maya_green", "Maya Green", "PRO", 0xFF10B981),
    MAYA_SAGE("maya_sage", "Maya Sage", "LOCKED", 0xFF06B6D4)
}

data class ScreenElement(
    val id: String,
    val text: String,
    val viewId: String? = null,
    val isClickable: Boolean,
    val isEditable: Boolean,
    val boundsLeft: Int,
    val boundsTop: Int,
    val boundsRight: Int,
    val boundsBottom: Int,
    val centerX: Int = (boundsLeft + boundsRight) / 2,
    val centerY: Int = (boundsTop + boundsBottom) / 2,
    val className: String = "View"
)

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachmentName: String? = null,
    val executedActions: List<String> = emptyList()
)

enum class MessageSender {
    USER,
    MAYA,
    SYSTEM
}
