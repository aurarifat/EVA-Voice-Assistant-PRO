package com.example.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ReceivedNotification(
    val id: String,
    val packageName: String,
    val title: String,
    val text: String,
    val postTime: Long = System.currentTimeMillis(),
    val isWhatsApp: Boolean = false
)

class MayaNotificationListenerService : NotificationListenerService() {

    companion object {
        var instance: MayaNotificationListenerService? = null
            private set

        private val _recentNotifications = MutableStateFlow<List<ReceivedNotification>>(emptyList())
        val recentNotifications: StateFlow<List<ReceivedNotification>> = _recentNotifications.asStateFlow()

        private val _lastWhatsAppMessage = MutableStateFlow<ReceivedNotification?>(null)
        val lastWhatsAppMessage: StateFlow<ReceivedNotification?> = _lastWhatsAppMessage.asStateFlow()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        instance = this
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        if (instance == this) instance = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        val extras = sbn.notification.extras ?: return
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val pkg = sbn.packageName ?: ""

        if (title.isBlank() && text.isBlank()) return

        val isWhatsApp = pkg.contains("whatsapp", ignoreCase = true)

        val notif = ReceivedNotification(
            id = sbn.key ?: System.currentTimeMillis().toString(),
            packageName = pkg,
            title = title,
            text = text,
            postTime = sbn.postTime,
            isWhatsApp = isWhatsApp
        )

        _recentNotifications.value = (listOf(notif) + _recentNotifications.value).take(50)

        if (isWhatsApp) {
            _lastWhatsAppMessage.value = notif
        }
    }
}
