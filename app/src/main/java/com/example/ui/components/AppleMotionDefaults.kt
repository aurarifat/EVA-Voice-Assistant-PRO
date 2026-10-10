package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView

/**
 * 🍎 Apple iOS Standard Motion Curves & Specs.
 * Natural, responsive springs for touch down / up with zero lag.
 */
object AppleMotionDefaults {
    // Apple standard cubic-bezier curve (Ease-out quick start, smooth deceleration)
    val AppleStandardEasing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)
    val AppleDecelerateEasing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
    val AppleAccelerateEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    // Standard iOS animation durations in ms
    const val DURATION_FAST = 180
    const val DURATION_STANDARD = 280
    const val DURATION_SHEET = 350

    /**
     * Standard iOS Spring Spec for interactive elements (cards, sheets, buttons)
     */
    fun <T> appleSpring(
        dampingRatio: Float = Spring.DampingRatioLowBouncy,
        stiffness: Float = Spring.StiffnessMediumLow
    ) = spring<T>(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )

    /**
     * Subtle spring for page transitions
     */
    fun <T> subtleSpring() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
}

/**
 * 🍎 Haptic feedback types for premium tactile feel.
 */
enum class AppleHapticStyle {
    LIGHT_IMPACT,      // Subtle tap (navigation, tab switch, filter chip)
    MEDIUM_IMPACT,     // Standard button click (send, submit, toggle)
    SUCCESS,           // Action completion
    SELECTION          // Picker wheel / segment change
}

/**
 * Performs subtle haptic feedback using Android system APIs.
 */
fun performSubtleHaptic(context: Context, style: AppleHapticStyle = AppleHapticStyle.LIGHT_IMPACT) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            val vibrator = vibratorManager?.defaultVibrator
            if (vibrator?.hasVibrator() == true) {
                val effect = when (style) {
                    AppleHapticStyle.LIGHT_IMPACT -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    AppleHapticStyle.MEDIUM_IMPACT -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    AppleHapticStyle.SUCCESS -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    AppleHapticStyle.SELECTION -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                }
                vibrator.vibrate(effect)
                return
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator?.hasVibrator() == true) {
                val effect = when (style) {
                    AppleHapticStyle.LIGHT_IMPACT -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    AppleHapticStyle.MEDIUM_IMPACT -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    AppleHapticStyle.SUCCESS -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    AppleHapticStyle.SELECTION -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                }
                vibrator.vibrate(effect)
                return
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator?.hasVibrator() == true) {
                val duration = if (style == AppleHapticStyle.LIGHT_IMPACT) 8L else 14L
                val amplitude = if (style == AppleHapticStyle.LIGHT_IMPACT) 40 else 80
                vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                return
            }
        }
    } catch (_: Exception) {
        // Fallback gracefully without crash if vibration is disallowed
    }
}

/**
 * 🍎 Apple iOS Fluid Press Effect using Compose InteractionSource with subtle haptics.
 * Instantaneous, hardware-accelerated graphicsLayer scale animation on touch down / up,
 * combined with crisp subtle tactile haptic vibration.
 */
fun Modifier.appleBounceClick(
    pressedScale: Float = 0.96f,
    hapticStyle: AppleHapticStyle = AppleHapticStyle.LIGHT_IMPACT,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Trigger subtle haptic on press down
    LaunchedEffect(isPressed) {
        if (isPressed) {
            performSubtleHaptic(context, hapticStyle)
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = if (isPressed) Spring.DampingRatioNoBouncy else Spring.DampingRatioMediumBouncy,
            stiffness = if (isPressed) Spring.StiffnessHigh else Spring.StiffnessMediumLow
        ),
        label = "apple_bounce"
    )

    Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null, // Tactile scale bounce + haptic acts as primary Apple visual response
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClick()
                    }
                )
            } else Modifier
        )
}

/**
 * 🍎 Debounced click modifier that prevents rapid double-taps from triggering duplicate actions.
 */
fun Modifier.appleDebouncedClick(
    debounceTimeMs: Long = 400L,
    hapticStyle: AppleHapticStyle = AppleHapticStyle.MEDIUM_IMPACT,
    onClick: () -> Unit
): Modifier = composed {
    val context = LocalContext.current
    val lastClickTime = remember { mutableLongStateOf(0L) }
    this.clickable {
        val now = System.currentTimeMillis()
        if (now - lastClickTime.longValue > debounceTimeMs) {
            lastClickTime.longValue = now
            performSubtleHaptic(context, hapticStyle)
            onClick()
        }
    }
}
