package com.example

import android.app.Application
import com.example.agent.TaskExecutionEngine
import com.example.ai.GeminiAgentClient
import com.example.ai.SpeechManager
import com.example.ai.VoiceProcessor
import com.example.control.DeviceControlManager
import com.example.data.repository.MayaPreferences
import com.example.data.repository.MemoryAndTaskRepository
import com.example.security.SecurityAndActionHistoryManager
import com.example.telegram.TelegramBridgeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MayaApplication : Application() {

    lateinit var preferences: MayaPreferences
        private set

    lateinit var memoryRepository: MemoryAndTaskRepository
        private set

    lateinit var geminiClient: GeminiAgentClient
        private set

    lateinit var speechManager: SpeechManager
        private set

    lateinit var deviceControl: DeviceControlManager
        private set

    lateinit var securityManager: SecurityAndActionHistoryManager
        private set

    lateinit var voiceProcessor: VoiceProcessor
        private set

    lateinit var telegramBridge: TelegramBridgeManager
        private set

    lateinit var taskExecutionEngine: TaskExecutionEngine
        private set

    private val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        preferences = MayaPreferences(this)
        memoryRepository = MemoryAndTaskRepository()
        speechManager = SpeechManager(this)
        deviceControl = DeviceControlManager(this)
        securityManager = SecurityAndActionHistoryManager(this)
        voiceProcessor = VoiceProcessor()

        geminiClient = GeminiAgentClient(
            apiKeyProvider = { preferences.geminiApiKey.value },
            modelProvider = { preferences.geminiModel.value }
        )

        taskExecutionEngine = TaskExecutionEngine(
            context = this,
            preferences = preferences,
            geminiClient = geminiClient,
            speechManager = speechManager,
            deviceControl = deviceControl,
            securityManager = securityManager,
            voiceProcessor = voiceProcessor,
            coroutineScope = applicationScope
        )

        telegramBridge = TelegramBridgeManager(
            tokenProvider = { preferences.telegramBotToken.value },
            geminiClient = geminiClient,
            scope = applicationScope
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        speechManager.shutdown()
        telegramBridge.stopPolling()
    }
}
