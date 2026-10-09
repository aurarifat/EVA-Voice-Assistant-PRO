package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

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
 * 🍎 Apple iOS Fluid Press Effect using Compose InteractionSource.
 * Instantaneous, hardware-accelerated graphicsLayer scale animation on touch down / up.
 */
fun Modifier.appleBounceClick(
    pressedScale: Float = 0.96f,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

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
                    indication = null, // Tactile scale bounce acts as primary Apple visual response
                    onClick = onClick
                )
            } else Modifier
        )
}

/**
 * 🍎 Debounced click modifier that prevents rapid double-taps from triggering duplicate actions.
 */
fun Modifier.appleDebouncedClick(
    debounceTimeMs: Long = 400L,
    onClick: () -> Unit
): Modifier = composed {
    val lastClickTime = remember { mutableLongStateOf(0L) }
    this.clickable {
        val now = System.currentTimeMillis()
        if (now - lastClickTime.longValue > debounceTimeMs) {
            lastClickTime.longValue = now
            onClick()
        }
    }
}
