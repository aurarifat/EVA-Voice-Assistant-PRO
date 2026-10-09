package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentStatus
import com.example.ui.theme.AppleBlue
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTealDark
import com.example.ui.theme.AppleTheme
import com.example.ui.theme.AppleYellowDark
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 🍎 Maya AI Orb — Apple-Inspired Fluid Intelligence Orb.
 *
 * Refined, calm, futuristic aesthetic reminiscent of Siri and Apple Intelligence.
 * Seamless, multidimensional fluid light with soft aura, caustic highlights,
 * and contextual micro-interactions.
 */
@Composable
fun MayaOrb(
    status: AgentStatus,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    showInteractiveModes: Boolean = false,
    onStatusSelect: ((AgentStatus) -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "apple_orb_infinite")

    // Rotation speeds adapt depending on status
    val rotationDuration = when (status) {
        AgentStatus.THINKING -> 3600
        AgentStatus.EXECUTING -> 2800
        AgentStatus.LISTENING -> 4800
        else -> 7200
    }

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = rotationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val counterRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (rotationDuration * 1.3).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "counter_rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    // Dynamic Apple-inspired semantic colors for current agent state
    val (primaryColor, secondaryColor, accentHighlight) = when (status) {
        AgentStatus.IDLE -> Triple(AppleBlueDark, ApplePurpleDark, AppleTealDark)
        AgentStatus.LISTENING -> Triple(AppleTealDark, AppleGreenDark, AppleBlueDark)
        AgentStatus.THINKING -> Triple(ApplePurpleDark, AppleBlueDark, AppleTealDark)
        AgentStatus.ANALYSING, AgentStatus.LOCATING -> Triple(AppleBlueDark, AppleOrangeDark, ApplePurpleDark)
        AgentStatus.EXECUTING, AgentStatus.SCROLLING, AgentStatus.NAVIGATING -> Triple(AppleBlueDark, AppleTealDark, AppleGreenDark)
        AgentStatus.SPEAKING -> Triple(AppleBlueDark, ApplePurpleDark, Color(0xFFFF375F))
        AgentStatus.WAITING_INPUT -> Triple(AppleOrangeDark, AppleYellowDark, AppleBlueDark)
        AgentStatus.COMPLETED -> Triple(AppleGreenDark, AppleTealDark, Color.White)
        AgentStatus.ERROR -> Triple(AppleRedDark, AppleOrangeDark, Color.White)
    }

    val animatedPrimary by animateColorAsState(targetValue = primaryColor, animationSpec = tween(500), label = "c_prim")
    val animatedSecondary by animateColorAsState(targetValue = secondaryColor, animationSpec = tween(500), label = "c_sec")
    val animatedHighlight by animateColorAsState(targetValue = accentHighlight, animationSpec = tween(500), label = "c_high")

    val interactiveScale by animateFloatAsState(
        targetValue = if (status == AgentStatus.EXECUTING) 1.06f else 1.0f,
        animationSpec = tween(300),
        label = "int_scale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Orb Container with soft clickable ripple
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .testTag("maya_ai_orb"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)
                val baseRadius = (this.size.minDimension / 2f) * 0.76f * pulseScale * interactiveScale

                // 1. Apple Ambient Outer Halo (Soft Gaussian falloff)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedPrimary.copy(alpha = 0.35f),
                            animatedSecondary.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        center = centerOffset,
                        radius = baseRadius * 1.35f
                    ),
                    radius = baseRadius * 1.35f,
                    center = centerOffset
                )

                // 2. Primary Caustic Fluid Gradient Body
                rotate(degrees = rotationAngle, pivot = centerOffset) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                animatedPrimary.copy(alpha = 0.90f),
                                animatedSecondary.copy(alpha = 0.85f),
                                animatedHighlight.copy(alpha = 0.80f),
                                animatedPrimary.copy(alpha = 0.90f)
                            ),
                            center = centerOffset
                        ),
                        radius = baseRadius,
                        center = centerOffset
                    )
                }

                // 3. Counter-rotating inner specular glow
                rotate(degrees = counterRotationAngle, pivot = centerOffset) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.40f),
                                animatedHighlight.copy(alpha = 0.25f),
                                Color.Transparent
                            ),
                            center = Offset(centerOffset.x - baseRadius * 0.25f, centerOffset.y - baseRadius * 0.25f),
                            radius = baseRadius * 0.85f
                        ),
                        radius = baseRadius * 0.95f,
                        center = centerOffset
                    )
                }

                // 4. State-dependent Apple micro-animations
                when (status) {
                    AgentStatus.LISTENING -> {
                        // Harmonic sine waveform ripple rings
                        for (i in 1..3) {
                            val ringRadius = baseRadius * (0.85f + i * 0.14f * ((pulseScale - 0.96f) / 0.08f))
                            drawCircle(
                                color = animatedHighlight.copy(alpha = 0.45f / i),
                                radius = ringRadius,
                                center = centerOffset,
                                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }

                    AgentStatus.THINKING -> {
                        // Apple-style fine orbital arcs
                        rotate(degrees = rotationAngle * 1.5f, pivot = centerOffset) {
                            drawArc(
                                brush = Brush.linearGradient(listOf(Color.White, animatedSecondary)),
                                startAngle = 0f,
                                sweepAngle = 140f,
                                useCenter = false,
                                topLeft = Offset(centerOffset.x - baseRadius * 1.08f, centerOffset.y - baseRadius * 1.08f),
                                size = androidx.compose.ui.geometry.Size(baseRadius * 2.16f, baseRadius * 2.16f),
                                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        rotate(degrees = counterRotationAngle * 1.2f, pivot = centerOffset) {
                            drawArc(
                                brush = Brush.linearGradient(listOf(animatedPrimary, Color.Transparent)),
                                startAngle = 180f,
                                sweepAngle = 120f,
                                useCenter = false,
                                topLeft = Offset(centerOffset.x - baseRadius * 1.15f, centerOffset.y - baseRadius * 1.15f),
                                size = androidx.compose.ui.geometry.Size(baseRadius * 2.3f, baseRadius * 2.3f),
                                style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }

                    AgentStatus.SPEAKING -> {
                        // Smooth audio amplitude visualizer bars at orb center
                        val barCount = 5
                        val barWidth = 4.dp.toPx()
                        val spacing = 7.dp.toPx()
                        val startX = centerOffset.x - (barCount * (barWidth + spacing)) / 2f
                        for (i in 0 until barCount) {
                            val barHeight = baseRadius * 0.4f * (0.4f + 0.6f * sin(wavePhase + i * 0.9f).toFloat().let { kotlin.math.abs(it) })
                            drawLine(
                                color = Color.White.copy(alpha = 0.9f),
                                start = Offset(startX + i * (barWidth + spacing), centerOffset.y - barHeight / 2f),
                                end = Offset(startX + i * (barWidth + spacing), centerOffset.y + barHeight / 2f),
                                strokeWidth = barWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    AgentStatus.EXECUTING -> {
                        // High-tech subtle focus ring
                        drawCircle(
                            color = Color.White.copy(alpha = 0.5f),
                            radius = baseRadius * 1.06f,
                            center = centerOffset,
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                    }

                    else -> {
                        // Subtle Apple inner luminous core
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
                                center = centerOffset,
                                radius = baseRadius * 0.45f
                            ),
                            radius = baseRadius * 0.45f,
                            center = centerOffset
                        )
                    }
                }

                // 5. Apple Top-left glass reflection sheen
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(centerOffset.x - baseRadius * 0.32f, centerOffset.y - baseRadius * 0.35f),
                        radius = baseRadius * 0.45f
                    ),
                    radius = baseRadius * 0.45f,
                    center = Offset(centerOffset.x - baseRadius * 0.32f, centerOffset.y - baseRadius * 0.35f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Apple-style Translucent Pill Status Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(AppleTheme.colors.surfaceSecondary.copy(alpha = 0.85f))
                .border(0.5.dp, AppleTheme.colors.border, RoundedCornerShape(50.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pulsing dot indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = status.title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = AppleTheme.colors.textPrimary
                )
            }
        }

        // Apple Segmented Control for State Simulation / Switching
        if (showInteractiveModes && onStatusSelect != null) {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surfaceSecondary)
                    .border(0.5.dp, AppleTheme.colors.border, RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppleSegmentChip("Listen", AgentStatus.LISTENING, status, Icons.Default.Mic, onStatusSelect)
                    AppleSegmentChip("Think", AgentStatus.THINKING, status, Icons.Default.Psychology, onStatusSelect)
                    AppleSegmentChip("Execute", AgentStatus.EXECUTING, status, Icons.Default.TouchApp, onStatusSelect)
                }
            }
        }
    }
}

@Composable
private fun AppleSegmentChip(
    label: String,
    targetStatus: AgentStatus,
    currentStatus: AgentStatus,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onSelect: (AgentStatus) -> Unit
) {
    val isSelected = currentStatus == targetStatus

    val chipBg by animateColorAsState(
        targetValue = if (isSelected) AppleTheme.colors.surface else Color.Transparent,
        animationSpec = tween(200),
        label = "segment_bg"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(chipBg)
            .clickable { onSelect(targetStatus) }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) AppleBlueDark else AppleTheme.colors.textMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium),
            color = if (isSelected) AppleTheme.colors.textPrimary else AppleTheme.colors.textMuted
        )
    }
}
