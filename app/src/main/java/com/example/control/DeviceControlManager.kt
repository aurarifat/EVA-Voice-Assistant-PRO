package com.example.control

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.view.KeyEvent
import com.example.service.MayaAccessibilityService

class DeviceControlManager(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // 31. Open apps by voice
    fun openApp(packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // 32. Open Settings pages
    fun openSettings(page: String = "main"): Boolean {
        return try {
            val action = when (page.lowercase()) {
                "wifi" -> Settings.ACTION_WIFI_SETTINGS
                "bluetooth" -> Settings.ACTION_BLUETOOTH_SETTINGS
                "display", "brightness" -> Settings.ACTION_DISPLAY_SETTINGS
                "sound", "volume" -> Settings.ACTION_SOUND_SETTINGS
                "accessibility" -> Settings.ACTION_ACCESSIBILITY_SETTINGS
                "apps" -> Settings.ACTION_APPLICATION_SETTINGS
                "battery" -> Settings.ACTION_BATTERY_SAVER_SETTINGS
                else -> Settings.ACTION_SETTINGS
            }
            val intent = Intent(action).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 33. Go Home
    fun goHome(): Boolean = MayaAccessibilityService.instance?.goHome() ?: false

    // 34. Back
    fun goBack(): Boolean = MayaAccessibilityService.instance?.goBack() ?: false

    // 35. Recent Apps
    fun openRecents(): Boolean = MayaAccessibilityService.instance?.openRecentApps() ?: false

    // 36. Scroll/tap through Accessibility
    fun tapCoordinate(x: Float, y: Float): Boolean =
        MayaAccessibilityService.instance?.tapCoordinate(x, y) ?: false

    fun scrollUp(): Boolean = MayaAccessibilityService.instance?.scrollUp() ?: false
    fun scrollDown(): Boolean = MayaAccessibilityService.instance?.scrollDown() ?: false

    // 37. Volume control
    fun setVolume(percent: Int): Int {
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val targetVol = (maxVol * (percent.coerceIn(0, 100) / 100f)).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVol, AudioManager.FLAG_SHOW_UI)
        return (targetVol * 100 / maxVol)
    }

    fun volumeUp(): Int {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        val curr = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return (curr * 100 / max)
    }

    fun volumeDown(): Int {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        val curr = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return (curr * 100 / max)
    }

    fun muteVolume(): Boolean {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI)
        return true
    }

    // 38. Brightness control shortcut
    fun openBrightnessSettings(): Boolean {
        return openSettings("brightness")
    }

    // 39. Media play/pause/next
    fun mediaPlayPause(): Boolean {
        return try {
            val down = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0)
            val up = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0)
            audioManager.dispatchMediaKeyEvent(down)
            audioManager.dispatchMediaKeyEvent(up)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun mediaNext(): Boolean {
        return try {
            val down = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0)
            val up = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0)
            audioManager.dispatchMediaKeyEvent(down)
            audioManager.dispatchMediaKeyEvent(up)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun mediaPrevious(): Boolean {
        return try {
            val down = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0)
            val up = KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0)
            audioManager.dispatchMediaKeyEvent(down)
            audioManager.dispatchMediaKeyEvent(up)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 40. Wi-Fi / Bluetooth controls
    fun openWifiSettings(): Boolean = openSettings("wifi")
    fun openBluetoothSettings(): Boolean = openSettings("bluetooth")

    // 42. Calls
    fun startPhoneCall(phoneNumber: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 43. SMS
    fun sendSms(phoneNumber: String, messageText: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber")).apply {
                putExtra("sms_body", messageText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 44. WhatsApp actions
    fun openWhatsAppChat(phoneNumber: String, initialMessage: String = ""): Boolean {
        return try {
            val cleanNumber = phoneNumber.replace("+", "").replace(" ", "").replace("-", "")
            val url = "https://api.whatsapp.com/send?phone=$cleanNumber&text=${Uri.encode(initialMessage)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                setPackage("com.whatsapp")
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            // Fallback to opening WhatsApp normally
            openApp("com.whatsapp")
        }
    }

    // 45. Termux integration
    fun executeTermuxTask(command: String = "pkg update -y"): Boolean {
        return try {
            // First attempt: Termux Run Command intent
            val termuxIntent = Intent("com.termux.RUN_COMMAND").apply {
                setPackage("com.termux")
                putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
                putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
                putExtra("com.termux.RUN_COMMAND_BACKGROUND", false)
            }
            context.sendBroadcast(termuxIntent)

            // Also launch Termux in foreground so user sees execution
            openApp("com.termux")
            true
        } catch (e: Exception) {
            // Fallback: Launch Termux and type command via accessibility
            openApp("com.termux")
        }
    }
}
