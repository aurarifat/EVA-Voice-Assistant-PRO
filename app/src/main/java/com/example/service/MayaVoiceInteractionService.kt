package com.example.service

import android.content.Context
import android.os.Bundle
import android.service.voice.VoiceInteractionService
import android.service.voice.VoiceInteractionSession
import android.service.voice.VoiceInteractionSessionService
import android.speech.RecognitionService

class MayaVoiceInteractionService : VoiceInteractionService() {
    override fun onReady() {
        super.onReady()
    }
}

class MayaVoiceInteractionSessionService : VoiceInteractionSessionService() {
    override fun onNewSession(args: Bundle?): VoiceInteractionSession {
        return MayaVoiceSession(this)
    }
}

class MayaVoiceSession(context: Context) : VoiceInteractionSession(context) {
    @Deprecated("Deprecated in Java", ReplaceWith("super.onHandleAssist(data, structure, content)"))
    @Suppress("DEPRECATION")
    override fun onHandleAssist(data: Bundle?, structure: android.app.assist.AssistStructure?, content: android.app.assist.AssistContent?) {
        super.onHandleAssist(data, structure, content)
    }
}

class MayaRecognitionService : RecognitionService() {
    override fun onStartListening(recognizerIntent: android.content.Intent?, listener: Callback?) {}
    override fun onCancel(listener: Callback?) {}
    override fun onStopListening(listener: Callback?) {}
}
