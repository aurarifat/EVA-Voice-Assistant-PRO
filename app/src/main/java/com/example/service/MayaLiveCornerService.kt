package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.example.R
import com.example.data.model.AgentStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MayaLiveCornerService : Service() {

    companion object {
        const val CHANNEL_ID = "maya_live_corner_channel"
        const val NOTIFICATION_ID = 1001

        private val _currentStatus = MutableStateFlow(AgentStatus.IDLE)
        val currentStatus: StateFlow<AgentStatus> = _currentStatus.asStateFlow()

        fun updateStatus(status: AgentStatus) {
            _currentStatus.value = status
        }
    }

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification(AgentStatus.IDLE))

        if (android.provider.Settings.canDrawOverlays(this)) {
            initFloatingCornerView()
        }

        serviceScope.launch {
            _currentStatus.collect { status ->
                updateFloatingViewStatus(status)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(NOTIFICATION_ID, buildNotification(status))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        floatingView?.let {
            windowManager?.removeView(it)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Maya AI Live Corner Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live corner execution status for Maya AI"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(status: AgentStatus): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Maya AI • ${status.title}")
            .setContentText(status.description)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun initFloatingCornerView() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 32
            y = 120
        }

        // Custom high-tech floating corner bubble
        val container = FrameLayout(this).apply {
            setPadding(24, 16, 24, 16)
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 60f
                setColor(0xEE0B1120.toInt())
                setStroke(3, 0xFF00F0FF.toInt())
            }
        }

        val label = TextView(this).apply {
            id = View.generateViewId()
            text = "● MAYA AI: READY"
            setTextColor(0xFF00F0FF.toInt())
            textSize = 12f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        container.addView(label)

        // Make floating corner draggable
        container.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX - (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager?.updateViewLayout(container, params)
                        return true
                    }
                }
                return false
            }
        })

        floatingView = container
        windowManager?.addView(container, params)
    }

    private fun updateFloatingViewStatus(status: AgentStatus) {
        val container = floatingView as? FrameLayout ?: return
        val label = container.getChildAt(0) as? TextView ?: return

        label.text = "● MAYA: ${status.title}"

        val accentColor = when (status) {
            AgentStatus.IDLE -> 0xFF00F0FF.toInt()
            AgentStatus.LISTENING -> 0xFF10B981.toInt()
            AgentStatus.THINKING -> 0xFFA855F7.toInt()
            AgentStatus.ANALYSING -> 0xFF38BDF8.toInt()
            AgentStatus.LOCATING -> 0xFFF59E0B.toInt()
            AgentStatus.EXECUTING -> 0xFFEC4899.toInt()
            AgentStatus.SCROLLING -> 0xFF6366F1.toInt()
            AgentStatus.NAVIGATING -> 0xFF14B8A6.toInt()
            AgentStatus.SPEAKING -> 0xFF22C55E.toInt()
            AgentStatus.WAITING_INPUT -> 0xFFEAB308.toInt()
            AgentStatus.COMPLETED -> 0xFF10B981.toInt()
            AgentStatus.ERROR -> 0xFFF43F5E.toInt()
        }

        label.setTextColor(accentColor)
        (container.background as? GradientDrawable)?.setStroke(3, accentColor)
    }
}
