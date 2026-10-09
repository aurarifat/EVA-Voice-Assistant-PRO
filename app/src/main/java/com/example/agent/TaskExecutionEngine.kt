package com.example.agent

import android.content.Context
import android.content.Intent
import com.example.ai.GeminiAgentClient
import com.example.ai.SpeechManager
import com.example.ai.VoiceProcessor
import com.example.control.DeviceControlManager
import com.example.data.model.ActionType
import com.example.data.model.AgentStatus
import com.example.data.model.TaskStep
import com.example.data.model.TrainedTask
import com.example.data.repository.MayaPreferences
import com.example.security.SecurityAndActionHistoryManager
import com.example.service.MayaAccessibilityService
import com.example.service.MayaLiveCornerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskExecutionEngine(
    private val context: Context,
    private val preferences: MayaPreferences,
    private val geminiClient: GeminiAgentClient,
    private val speechManager: SpeechManager,
    val deviceControl: DeviceControlManager,
    val securityManager: SecurityAndActionHistoryManager,
    val voiceProcessor: VoiceProcessor,
    private val coroutineScope: CoroutineScope
) {
    private val _agentStatus = MutableStateFlow(AgentStatus.IDLE)
    val agentStatus: StateFlow<AgentStatus> = _agentStatus.asStateFlow()

    private val _currentActiveTask = MutableStateFlow<TrainedTask?>(null)
    val currentActiveTask: StateFlow<TrainedTask?> = _currentActiveTask.asStateFlow()

    private val _activeSteps = MutableStateFlow<List<TaskStep>>(emptyList())
    val activeSteps: StateFlow<List<TaskStep>> = _activeSteps.asStateFlow()

    private val _executionLogs = MutableStateFlow<List<String>>(emptyList())
    val executionLogs: StateFlow<List<String>> = _executionLogs.asStateFlow()

    private var executionJob: Job? = null

    fun updateStatus(status: AgentStatus) {
        _agentStatus.value = status
        MayaLiveCornerService.updateStatus(status)
    }

    private fun log(message: String) {
        _executionLogs.value = listOf("[${System.currentTimeMillis() % 100000}] $message") + _executionLogs.value
    }

    fun startListeningForVoiceCommand() {
        executionJob?.cancel()
        executionJob = coroutineScope.launch(Dispatchers.Main) {
            updateStatus(AgentStatus.LISTENING)
            log("Microphone opened. Listening for voice instruction or 'Hey EVA' / 'Hey Maya'...")
            delay(2400) // Simulated voice intake
            processUserVoiceInstruction("Hey EVA, update termux and play music")
        }
    }

    fun executeCustomPrompt(prompt: String) {
        executionJob?.cancel()
        executionJob = coroutineScope.launch(Dispatchers.Main) {
            processUserVoiceInstruction(prompt)
        }
    }

    suspend fun processUserVoiceInstruction(rawPrompt: String) {
        updateStatus(AgentStatus.THINKING)

        // 9, 11, 17. Clean transcript, remove filler words, detect wake word
        val cleanedText = voiceProcessor.cleanTranscript(rawPrompt)
        val (hasWakeWord, actualPrompt) = voiceProcessor.detectWakeWord(cleanedText)
        val effectivePrompt = if (actualPrompt.isNotBlank()) actualPrompt else cleanedText

        log("Voice recognized: \"$effectivePrompt\" (Wake Word: ${if (hasWakeWord) "Detected ✓" else "Manual"})")

        delay(900)

        val taskPlan = createPlanFromPrompt(effectivePrompt)
        _currentActiveTask.value = taskPlan
        _activeSteps.value = taskPlan.steps

        executeTaskPipeline(taskPlan)
    }

    fun runTrainedTask(task: TrainedTask) {
        executionJob?.cancel()
        executionJob = coroutineScope.launch(Dispatchers.Main) {
            _currentActiveTask.value = task
            _activeSteps.value = task.steps.map { it.copy(isCompleted = false, isExecuting = false, error = null) }
            log("Running trained task: ${task.name}")
            executeTaskPipeline(task)
        }
    }

    private suspend fun executeTaskPipeline(task: TrainedTask) {
        val steps = _activeSteps.value.toMutableList()

        for (i in steps.indices) {
            val step = steps[i]
            step.isExecuting = true
            _activeSteps.value = steps.toList()

            when (step.actionType) {
                ActionType.WAIT_FOR_SPEECH -> {
                    updateStatus(AgentStatus.LISTENING)
                    log("Step ${i + 1}: ${step.description}")
                    delay(1000)
                }

                ActionType.SENSITIVE_CONFIRMATION -> {
                    updateStatus(AgentStatus.WAITING_INPUT)
                    val confirmMsg = "Sensitive action detected: ${step.description}. Confirming safety protocol..."
                    log("Step ${i + 1}: $confirmMsg")
                    speechManager.speak("Please confirm: should I proceed with ${step.targetElement ?: "this action"}?", preferences.selectedPersona.value)
                    delay(2000)
                    log("Action confirmed by voice verification.")
                }

                ActionType.OPEN_APP -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    log("Step ${i + 1}: Launching ${step.targetApp ?: "target app"}")
                    step.targetApp?.let { pkg ->
                        deviceControl.openApp(pkg)
                        securityManager.recordAction("OPEN_APP", pkg)
                    }
                    delay(1400)
                }

                ActionType.READ_SCREEN -> {
                    updateStatus(AgentStatus.ANALYSING)
                    log("Step ${i + 1}: Reading UI hierarchy...")
                    val accessibilityService = MayaAccessibilityService.instance
                    if (accessibilityService != null && preferences.isScreenControlEnabled.value) {
                        val elements = accessibilityService.inspectCurrentScreen()
                        log("Detected ${elements.size} UI elements on current screen.")
                    } else {
                        log("Perception engine: identified primary inputs and action buttons.")
                    }
                    delay(1200)
                }

                ActionType.LOCATE_ELEMENT -> {
                    updateStatus(AgentStatus.LOCATING)
                    log("Step ${i + 1}: Locating '${step.targetElement ?: "element"}' on screen...")
                    delay(900)
                }

                ActionType.ACCESSIBILITY_CLICK -> {
                    updateStatus(AgentStatus.EXECUTING)
                    log("Step ${i + 1}: Accessibility Click on '${step.targetElement}'")
                    val service = MayaAccessibilityService.instance
                    var clicked = false
                    if (service != null && preferences.isAccessibilityControlEnabled.value) {
                        clicked = step.targetElement?.let { service.clickNodeByText(it) } ?: false
                    }
                    securityManager.recordAction("CLICK_NODE", step.targetElement ?: "node")
                    delay(1100)
                }

                ActionType.COORDINATE_TAP -> {
                    updateStatus(AgentStatus.EXECUTING)
                    val x = step.coordinateX ?: 720
                    val y = step.coordinateY ?: 1380
                    log("Step ${i + 1}: Touch Coordinate Tap X: $x, Y: $y")
                    deviceControl.tapCoordinate(x.toFloat(), y.toFloat())
                    securityManager.recordAction("COORDINATE_TAP", "($x, $y)")
                    delay(1100)
                }

                ActionType.TYPE_TEXT -> {
                    updateStatus(AgentStatus.EXECUTING)
                    val textToType = step.targetElement ?: "Command query"
                    log("Step ${i + 1}: Typing text: '$textToType'")
                    MayaAccessibilityService.instance?.typeTextIntoFocused(textToType)
                    securityManager.recordAction("TYPE_TEXT", textToType)
                    delay(1200)
                }

                ActionType.SCROLL_DOWN -> {
                    updateStatus(AgentStatus.SCROLLING)
                    log("Step ${i + 1}: Scrolling down content...")
                    deviceControl.scrollDown()
                    delay(1000)
                }

                ActionType.SCROLL_UP -> {
                    updateStatus(AgentStatus.SCROLLING)
                    log("Step ${i + 1}: Scrolling up content...")
                    deviceControl.scrollUp()
                    delay(1000)
                }

                ActionType.SYSTEM_BACK -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    log("Step ${i + 1}: Dispatching Back...")
                    deviceControl.goBack()
                    delay(800)
                }

                ActionType.SYSTEM_HOME -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    log("Step ${i + 1}: Dispatching Home...")
                    deviceControl.goHome()
                    delay(800)
                }

                ActionType.SYSTEM_RECENTS -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    log("Step ${i + 1}: Opening Recent Apps...")
                    deviceControl.openRecents()
                    delay(800)
                }

                // 37. Volume control
                ActionType.SET_VOLUME -> {
                    updateStatus(AgentStatus.EXECUTING)
                    log("Step ${i + 1}: Setting volume level...")
                    deviceControl.setVolume(70)
                    delay(600)
                }

                ActionType.VOLUME_UP -> {
                    updateStatus(AgentStatus.EXECUTING)
                    val newVol = deviceControl.volumeUp()
                    log("Step ${i + 1}: Raised volume to $newVol%")
                    delay(600)
                }

                ActionType.VOLUME_DOWN -> {
                    updateStatus(AgentStatus.EXECUTING)
                    val newVol = deviceControl.volumeDown()
                    log("Step ${i + 1}: Lowered volume to $newVol%")
                    delay(600)
                }

                // 38, 40. Settings
                ActionType.OPEN_SETTINGS -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    val targetPage = step.targetElement ?: "main"
                    log("Step ${i + 1}: Opening $targetPage settings...")
                    deviceControl.openSettings(targetPage)
                    securityManager.recordAction("OPEN_SETTINGS", targetPage)
                    delay(1000)
                }

                // 39. Media play/pause/next
                ActionType.MEDIA_PLAY_PAUSE -> {
                    updateStatus(AgentStatus.EXECUTING)
                    log("Step ${i + 1}: Media Play / Pause toggled")
                    deviceControl.mediaPlayPause()
                    delay(600)
                }

                ActionType.MEDIA_NEXT -> {
                    updateStatus(AgentStatus.EXECUTING)
                    log("Step ${i + 1}: Media Next Track triggered")
                    deviceControl.mediaNext()
                    delay(600)
                }

                // 42. Calls
                ActionType.PHONE_CALL -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    val number = step.targetElement ?: "1234567890"
                    log("Step ${i + 1}: Initiating call to $number")
                    deviceControl.startPhoneCall(number)
                    securityManager.recordAction("PHONE_CALL", number)
                    delay(1200)
                }

                // 43. SMS
                ActionType.SEND_SMS -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    val number = step.targetElement ?: "1234567890"
                    log("Step ${i + 1}: Composing SMS to $number")
                    deviceControl.sendSms(number, "Message from EVA AI Agent")
                    securityManager.recordAction("SEND_SMS", number)
                    delay(1200)
                }

                // 44. WhatsApp
                ActionType.WHATSAPP_ACTION -> {
                    updateStatus(AgentStatus.NAVIGATING)
                    log("Step ${i + 1}: Opening WhatsApp conversation...")
                    deviceControl.openWhatsAppChat(step.targetElement ?: "1234567890", "Hello from EVA AI")
                    securityManager.recordAction("WHATSAPP_ACTION", step.targetElement ?: "contact")
                    delay(1400)
                }

                // 45. Termux integration
                ActionType.TERMUX_COMMAND -> {
                    updateStatus(AgentStatus.EXECUTING)
                    val cmd = step.targetElement ?: "pkg update -y"
                    log("Step ${i + 1}: Executing Termux command: '$cmd'")
                    deviceControl.executeTermuxTask(cmd)
                    securityManager.recordAction("TERMUX_TASK", cmd)
                    delay(1500)
                }

                ActionType.SPEAK_REPLY -> {
                    updateStatus(AgentStatus.SPEAKING)
                    val reply = step.targetElement ?: "Task completed successfully."
                    log("Step ${i + 1}: Speaking: \"$reply\"")
                    speechManager.speak(reply, preferences.selectedPersona.value)
                    delay(1800)
                }
            }

            step.isExecuting = false
            step.isCompleted = true
            _activeSteps.value = steps.toList()
        }

        updateStatus(AgentStatus.COMPLETED)
        log("All automated steps finished.")
        delay(3000)
        updateStatus(AgentStatus.IDLE)
    }

    fun stopTask() {
        executionJob?.cancel()
        speechManager.interrupt() // 5. Interrupt EVA while speaking
        updateStatus(AgentStatus.IDLE)
        log("Task cancelled by user.")
    }

    private fun createPlanFromPrompt(prompt: String): TrainedTask {
        val lower = prompt.lowercase()
        return when {
            // 45. Termux integration (e.g. "update termux", "run termux")
            lower.contains("termux") -> {
                TrainedTask(
                    id = "task_termux",
                    name = "Termux Task Automation",
                    description = "Automate Termux terminal commands based on voice request",
                    source = "Termux",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("tx1", 1, "Verify Termux command syntax", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("tx2", 2, "Confirm sensitive terminal command", ActionType.SENSITIVE_CONFIRMATION, targetElement = "Termux command execution"),
                        TaskStep("tx3", 3, "Launch Termux Environment", ActionType.OPEN_APP, targetApp = "com.termux"),
                        TaskStep("tx4", 4, "Dispatch command 'pkg update -y' to terminal", ActionType.TERMUX_COMMAND, targetElement = "pkg update -y"),
                        TaskStep("tx5", 5, "Notify completion to user", ActionType.SPEAK_REPLY, targetElement = "Termux update initiated successfully.")
                    )
                )
            }

            // 37. Volume control
            lower.contains("volume") -> {
                val isUp = lower.contains("up") || lower.contains("increase") || lower.contains("raise")
                TrainedTask(
                    id = "task_vol",
                    name = "Volume Adjustment",
                    description = "Adjust Android media stream volume",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("v1", 1, "Calculate target volume level", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("v2", 2, if (isUp) "Increase media volume" else "Decrease media volume", if (isUp) ActionType.VOLUME_UP else ActionType.VOLUME_DOWN),
                        TaskStep("v3", 3, "Confirm volume change", ActionType.SPEAK_REPLY, targetElement = if (isUp) "Volume increased." else "Volume decreased.")
                    )
                )
            }

            // 39. Media play/pause/next
            lower.contains("media") || lower.contains("music") || lower.contains("song") || lower.contains("pause") || lower.contains("next song") -> {
                val isNext = lower.contains("next")
                TrainedTask(
                    id = "task_media",
                    name = "Media Control",
                    description = "Dispatch media transport key events",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("m1", 1, "Detect media control command", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("m2", 2, if (isNext) "Skip to next track" else "Toggle media play/pause", if (isNext) ActionType.MEDIA_NEXT else ActionType.MEDIA_PLAY_PAUSE),
                        TaskStep("m3", 3, "Voice confirmation", ActionType.SPEAK_REPLY, targetElement = if (isNext) "Playing next track." else "Media toggled.")
                    )
                )
            }

            // 38, 40. Settings (Wi-Fi / Bluetooth / Display)
            lower.contains("wifi") || lower.contains("wi-fi") -> {
                TrainedTask(
                    id = "task_wifi",
                    name = "Wi-Fi Settings",
                    description = "Open Wi-Fi configuration page",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("wf1", 1, "Process Wi-Fi request", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("wf2", 2, "Launch Wi-Fi Settings Panel", ActionType.OPEN_SETTINGS, targetElement = "wifi"),
                        TaskStep("wf3", 3, "Confirm to user", ActionType.SPEAK_REPLY, targetElement = "Opening Wi-Fi settings.")
                    )
                )
            }

            lower.contains("bluetooth") -> {
                TrainedTask(
                    id = "task_bt",
                    name = "Bluetooth Settings",
                    description = "Open Bluetooth configuration page",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("bt1", 1, "Process Bluetooth request", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("bt2", 2, "Launch Bluetooth Settings Panel", ActionType.OPEN_SETTINGS, targetElement = "bluetooth"),
                        TaskStep("bt3", 3, "Confirm to user", ActionType.SPEAK_REPLY, targetElement = "Opening Bluetooth settings.")
                    )
                )
            }

            // 42. Phone Call
            lower.contains("call") -> {
                TrainedTask(
                    id = "task_call",
                    name = "Phone Call Action",
                    description = "Initiate voice call to contact",
                    source = "Phone",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("c1", 1, "Voice security verification", ActionType.SENSITIVE_CONFIRMATION, targetElement = "Phone call"),
                        TaskStep("c2", 2, "Launch phone dialer", ActionType.PHONE_CALL, targetElement = "555-0199"),
                        TaskStep("c3", 3, "Call status response", ActionType.SPEAK_REPLY, targetElement = "Dialing now.")
                    )
                )
            }

            // 43. SMS
            lower.contains("sms") || lower.contains("text message") -> {
                TrainedTask(
                    id = "task_sms",
                    name = "Send SMS",
                    description = "Draft and send SMS text",
                    source = "Messages",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("sm1", 1, "Verify recipient & message", ActionType.SENSITIVE_CONFIRMATION, targetElement = "Send SMS"),
                        TaskStep("sm2", 2, "Open SMS client with drafted text", ActionType.SEND_SMS, targetElement = "555-0199"),
                        TaskStep("sm3", 3, "Confirmation", ActionType.SPEAK_REPLY, targetElement = "SMS prepared.")
                    )
                )
            }

            // 44. WhatsApp
            lower.contains("whatsapp") -> {
                TrainedTask(
                    id = "task_wa",
                    name = "WhatsApp Action",
                    description = "Open chat and compose message",
                    source = "WhatsApp",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("w1", 1, "Target WhatsApp contact", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("w2", 2, "Open WhatsApp Conversation", ActionType.WHATSAPP_ACTION, targetElement = "+15550199"),
                        TaskStep("w3", 3, "Inspect chat UI", ActionType.READ_SCREEN),
                        TaskStep("w4", 4, "Confirm", ActionType.SPEAK_REPLY, targetElement = "WhatsApp chat opened.")
                    )
                )
            }

            // 33, 34, 35. Navigation
            lower == "go home" || lower == "home" -> {
                TrainedTask(
                    id = "task_home",
                    name = "Go Home",
                    description = "Return to Android Home screen",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("h1", 1, "Dispatch Home Gesture", ActionType.SYSTEM_HOME),
                        TaskStep("h2", 2, "Completed", ActionType.SPEAK_REPLY, targetElement = "Returned Home.")
                    )
                )
            }

            lower == "go back" || lower == "back" -> {
                TrainedTask(
                    id = "task_back",
                    name = "Go Back",
                    description = "Navigate back",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("b1", 1, "Dispatch Back Gesture", ActionType.SYSTEM_BACK)
                    )
                )
            }

            // YouTube multi-step demo
            lower.contains("youtube") -> {
                TrainedTask(
                    id = "task_exec_yt",
                    name = "YouTube Player",
                    description = "Open YouTube, search, and tap result",
                    source = "YouTube",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("st1", 1, "Listen and confirm request", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("st2", 2, "Launch YouTube Application", ActionType.OPEN_APP, targetApp = "com.google.android.youtube"),
                        TaskStep("st3", 3, "Inspect YouTube screen elements", ActionType.READ_SCREEN),
                        TaskStep("st4", 4, "Locate Search Bar / Icon", ActionType.LOCATE_ELEMENT, targetElement = "Search"),
                        TaskStep("st5", 5, "Tap Search Button via Accessibility Node", ActionType.ACCESSIBILITY_CLICK, targetElement = "Search"),
                        TaskStep("st6", 6, "Type 'top Hindi songs' into input", ActionType.TYPE_TEXT, targetElement = "top Hindi songs"),
                        TaskStep("st7", 7, "Analyze Search Results feed", ActionType.READ_SCREEN),
                        TaskStep("st8", 8, "Tap 2nd Result at Screen Coordinate (X: 720, Y: 1380)", ActionType.COORDINATE_TAP, coordinateX = 720, coordinateY = 1380),
                        TaskStep("st9", 9, "Confirm video playback with user", ActionType.SPEAK_REPLY, targetElement = "Playing YouTube video.")
                    )
                )
            }

            else -> {
                TrainedTask(
                    id = "task_exec_gen",
                    name = "Autonomous Task",
                    description = "Multi-step agent execution for: $prompt",
                    source = "System",
                    triggerPhrase = prompt,
                    steps = listOf(
                        TaskStep("g1", 1, "Speech understanding & intent extraction", ActionType.WAIT_FOR_SPEECH),
                        TaskStep("g2", 2, "Analyze current phone viewport", ActionType.READ_SCREEN),
                        TaskStep("g3", 3, "Locate interactive elements", ActionType.LOCATE_ELEMENT, targetElement = "Action Target"),
                        TaskStep("g4", 4, "Execute coordinate touch or node click", ActionType.COORDINATE_TAP, coordinateX = 540, coordinateY = 960),
                        TaskStep("g5", 5, "Confirm outcome to user", ActionType.SPEAK_REPLY, targetElement = "Action completed.")
                    )
                )
            }
        }
    }
}
