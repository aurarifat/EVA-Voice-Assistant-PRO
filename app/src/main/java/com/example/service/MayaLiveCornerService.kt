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

        // Apple Dynamic Island-style floating pill container
        val container = FrameLayout(this).apply {
            setPadding(32, 18, 32, 18)
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 100f
                setColor(0xF0000000.toInt()) // Apple Dynamic Island near-black translucent
                setStroke(2, 0x40FFFFFF.toInt()) // Subtle hairline border
            }
        }

        val label = TextView(this).apply {
            id = View.generateViewId()
            text = "● MAYA AI  READY"
            setTextColor(0xFF0A84FF.toInt()) // Apple System Blue
            textSize = 12f
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
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
            AgentStatus.IDLE -> 0xFF0A84FF.toInt() // Apple Blue
            AgentStatus.LISTENING -> 0xFF30D158.toInt() // Apple Green
            AgentStatus.THINKING -> 0xFFBF5AF2.toInt() // Apple Purple
            AgentStatus.ANALYSING -> 0xFF64D2FF.toInt() // Apple Cyan
            AgentStatus.LOCATING -> 0xFFFF9F0A.toInt() // Apple Orange
            AgentStatus.EXECUTING -> 0xFFFF375F.toInt() // Apple Pink
            AgentStatus.SCROLLING -> 0xFF5E5CE6.toInt() // Apple Indigo
            AgentStatus.NAVIGATING -> 0xFF40C8E0.toInt() // Apple Teal
            AgentStatus.SPEAKING -> 0xFF30D158.toInt() // Apple Green
            AgentStatus.WAITING_INPUT -> 0xFFFFD60A.toInt() // Apple Yellow
            AgentStatus.COMPLETED -> 0xFF30D158.toInt() // Apple Green
            AgentStatus.ERROR -> 0xFFFF453A.toInt() // Apple Red
        }

        label.setTextColor(accentColor)
        (container.background as? GradientDrawable)?.setStroke(2, accentColor)
    }
}
