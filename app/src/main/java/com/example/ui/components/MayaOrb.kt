package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
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
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyberDark700
import com.example.ui.theme.CyberDark800
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.RoseNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VioletNeon
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Maya AI Orb — Next-Generation Visual Feedback Component.
 *
 * Provides distinct animated visual states:
 * - LISTENING: Audio waveform ripples, sound harmonic rings, acoustic energy pulses
 * - THINKING: Swirling neural orbits, counter-rotating synaptic gear arcs, cosmic starlight nodes
 * - EXECUTING: Kinetic touch bursts, precision targeting reticles, accelerated particle velocity
 * - IDLE / SPEAKING / COMPLETED / ERROR: Fluid contextual color morphing and status messaging
 */
@Composable
fun MayaOrb(
    status: AgentStatus,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    showInteractiveModes: Boolean = false,
    onStatusSelect: ((AgentStatus) -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "maya_orb_infinite")

    // Rotation speeds adapt depending on status
    val rotationDuration = when (status) {
        AgentStatus.THINKING -> 3200
        AgentStatus.EXECUTING -> 2400
        AgentStatus.LISTENING -> 5500
        else -> 7000
    }

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = rotationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulsing rhythm adapts to cognitive load
    val pulseDuration = when (status) {
        AgentStatus.LISTENING -> 800
        AgentStatus.EXECUTING -> 600
        AgentStatus.THINKING -> 1200
        else -> 1600
    }

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = when (status) {
            AgentStatus.LISTENING -> 0.85f
            AgentStatus.EXECUTING -> 0.90f
            else -> 0.88f
        },
        targetValue = when (status) {
            AgentStatus.LISTENING -> 1.18f
            AgentStatus.EXECUTING -> 1.14f
            else -> 1.08f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = pulseDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Radial ripple wave for acoustic & kinetic feedback
    val rippleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (status == AgentStatus.LISTENING) 1400 else 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple_progress"
    )

    // Color palette morphing
    val targetPrimaryColor = when (status) {
        AgentStatus.IDLE -> CyanNeon
        AgentStatus.LISTENING -> EmeraldNeon
        AgentStatus.THINKING -> VioletNeon
        AgentStatus.ANALYSING -> Color(0xFF38BDF8)
        AgentStatus.LOCATING -> Color(0xFFF59E0B)
        AgentStatus.EXECUTING -> Color(0xFFFF2A85) // High-energy cyber magenta
        AgentStatus.SCROLLING -> Color(0xFF818CF8)
        AgentStatus.NAVIGATING -> Color(0xFF2DD4BF)
        AgentStatus.SPEAKING -> Color(0xFF4ADE80)
        AgentStatus.WAITING_INPUT -> Color(0xFFFACC15)
        AgentStatus.COMPLETED -> EmeraldNeon
        AgentStatus.ERROR -> RoseNeon
    }

    val primaryColor by animateColorAsState(
        targetValue = targetPrimaryColor,
        animationSpec = tween(durationMillis = 400),
        label = "primary_color"
    )

    val secondaryColor = when (status) {
        AgentStatus.IDLE -> VioletNeon
        AgentStatus.LISTENING -> CyanNeon
        AgentStatus.THINKING -> Color(0xFFF43F5E)
        AgentStatus.ANALYSING -> EmeraldNeon
        AgentStatus.LOCATING -> Color(0xFFEF4444)
        AgentStatus.EXECUTING -> CyanNeon
        AgentStatus.SCROLLING -> Color(0xFFC084FC)
        AgentStatus.NAVIGATING -> Color(0xFF38BDF8)
        AgentStatus.SPEAKING -> CyanNeon
        AgentStatus.WAITING_INPUT -> Color(0xFFFB923C)
        AgentStatus.COMPLETED -> CyanNeon
        AgentStatus.ERROR -> Color(0xFFDC2626)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .testTag("maya_ai_orb")
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)
                val baseRadius = this.size.minDimension / 2.6f

                // ==========================================
                // 1. OUTWARD AMBIENT AURA GLOW
                // ==========================================
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.40f * pulseScale),
                            secondaryColor.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        center = centerOffset,
                        radius = baseRadius * 1.55f * pulseScale
                    ),
                    radius = baseRadius * 1.55f * pulseScale,
                    center = centerOffset
                )

                // ==========================================
                // 2. STATE-SPECIFIC OUTER EFFECTS
                // ==========================================
                when (status) {
                    AgentStatus.LISTENING -> {
                        // ACOUSTIC SOUNDWAVE RINGS
                        for (ringIdx in 0..2) {
                            val ringPhase = (rippleProgress + ringIdx * 0.33f) % 1f
                            val ringRadius = baseRadius * (1f + ringPhase * 0.55f)
                            val ringAlpha = (1f - ringPhase) * 0.65f
                            drawCircle(
                                color = primaryColor.copy(alpha = ringAlpha),
                                radius = ringRadius,
                                center = centerOffset,
                                style = Stroke(
                                    width = (2.5f - ringPhase * 1.5f).dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), ringPhase * 30f)
                                )
                            )
                        }

                        // Harmonic Voice Frequency Pins
                        for (i in 0 until 12) {
                            val angle = (i * 30f) * (PI / 180.0)
                            val pinLen = (8f + 14f * sin((pulseScale * 8f) + i)).dp.toPx()
                            val innerR = baseRadius * 1.15f
                            val outerR = innerR + pinLen
                            val startP = Offset(
                                centerOffset.x + (innerR * cos(angle)).toFloat(),
                                centerOffset.y + (innerR * sin(angle)).toFloat()
                            )
                            val endP = Offset(
                                centerOffset.x + (outerR * cos(angle)).toFloat(),
                                centerOffset.y + (outerR * sin(angle)).toFloat()
                            )
                            drawLine(
                                color = primaryColor.copy(alpha = 0.75f),
                                start = startP,
                                end = endP,
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    AgentStatus.THINKING -> {
                        // SYNAPTIC NEURAL ENERGY ARCS
                        rotate(rotation * 1.8f, centerOffset) {
                            for (arc in 0 until 4) {
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(primaryColor, Color.Transparent, secondaryColor, primaryColor)
                                    ),
                                    startAngle = arc * 90f + 15f,
                                    sweepAngle = 55f,
                                    useCenter = false,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                                    size = Size(baseRadius * 2.3f, baseRadius * 2.3f),
                                    topLeft = Offset(centerOffset.x - baseRadius * 1.15f, centerOffset.y - baseRadius * 1.15f)
                                )
                            }
                        }

                        // Neural Thinking Orbit Nodes
                        rotate(-rotation * 0.9f, centerOffset) {
                            for (n in 0 until 6) {
                                val ang = (n * 60f) * (PI / 180.0)
                                val dist = baseRadius * 1.18f
                                val nx = centerOffset.x + (dist * cos(ang)).toFloat()
                                val ny = centerOffset.y + (dist * sin(ang)).toFloat()
                                drawCircle(
                                    color = Color.White,
                                    radius = 3.dp.toPx(),
                                    center = Offset(nx, ny)
                                )
                                drawCircle(
                                    color = secondaryColor.copy(alpha = 0.5f),
                                    radius = 6.dp.toPx(),
                                    center = Offset(nx, ny)
                                )
                            }
                        }
                    }

                    AgentStatus.EXECUTING -> {
                        // KINETIC TARGETING RETICLE & ACCELERATED ACTION BEAMS
                        rotate(rotation * 2.5f, centerOffset) {
                            // Precision crosshairs
                            val armLen = baseRadius * 1.35f
                            drawLine(
                                color = primaryColor.copy(alpha = 0.85f),
                                start = Offset(centerOffset.x - armLen, centerOffset.y),
                                end = Offset(centerOffset.x - baseRadius * 0.9f, centerOffset.y),
                                strokeWidth = 2.5.dp.toPx()
                            )
                            drawLine(
                                color = primaryColor.copy(alpha = 0.85f),
                                start = Offset(centerOffset.x + baseRadius * 0.9f, centerOffset.y),
                                end = Offset(centerOffset.x + armLen, centerOffset.y),
                                strokeWidth = 2.5.dp.toPx()
                            )
                            drawLine(
                                color = primaryColor.copy(alpha = 0.85f),
                                start = Offset(centerOffset.x, centerOffset.y - armLen),
                                end = Offset(centerOffset.x, centerOffset.y - baseRadius * 0.9f),
                                strokeWidth = 2.5.dp.toPx()
                            )
                            drawLine(
                                color = primaryColor.copy(alpha = 0.85f),
                                start = Offset(centerOffset.x, centerOffset.y + baseRadius * 0.9f),
                                end = Offset(centerOffset.x, centerOffset.y + armLen),
                                strokeWidth = 2.5.dp.toPx()
                            )
                        }

                        // Concentric kinetic pressure wave
                        val tapProgress = (rippleProgress * 1.5f) % 1f
                        drawCircle(
                            color = primaryColor.copy(alpha = (1f - tapProgress) * 0.8f),
                            radius = baseRadius * (0.8f + tapProgress * 0.5f),
                            center = centerOffset,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    else -> {
                        // Standard idle ripple ring
                        drawCircle(
                            color = primaryColor.copy(alpha = 0.35f),
                            radius = baseRadius * 1.15f * pulseScale,
                            center = centerOffset,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }

                // ==========================================
                // 3. MAIN ORBITAL RING ARCS
                // ==========================================
                rotate(rotation, centerOffset) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(primaryColor, secondaryColor, Color.Transparent, primaryColor)
                        ),
                        startAngle = 0f,
                        sweepAngle = 145f,
                        useCenter = false,
                        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round),
                        size = Size(baseRadius * 2.05f, baseRadius * 2.05f),
                        topLeft = Offset(centerOffset.x - baseRadius * 1.025f, centerOffset.y - baseRadius * 1.025f)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(secondaryColor, primaryColor, Color.Transparent, secondaryColor)
                        ),
                        startAngle = 180f,
                        sweepAngle = 145f,
                        useCenter = false,
                        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round),
                        size = Size(baseRadius * 2.05f, baseRadius * 2.05f),
                        topLeft = Offset(centerOffset.x - baseRadius * 1.025f, centerOffset.y - baseRadius * 1.025f)
                    )
                }

                // Counter-rotating inner orbital arc
                rotate(-rotation * 1.35f, centerOffset) {
                    drawArc(
                        color = primaryColor.copy(alpha = 0.75f),
                        startAngle = 40f,
                        sweepAngle = 95f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                        size = Size(baseRadius * 1.7f, baseRadius * 1.7f),
                        topLeft = Offset(centerOffset.x - baseRadius * 0.85f, centerOffset.y - baseRadius * 0.85f)
                    )
                    drawArc(
                        color = secondaryColor.copy(alpha = 0.75f),
                        startAngle = 220f,
                        sweepAngle = 95f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                        size = Size(baseRadius * 1.7f, baseRadius * 1.7f),
                        topLeft = Offset(centerOffset.x - baseRadius * 0.85f, centerOffset.y - baseRadius * 0.85f)
                    )
                }

                // ==========================================
                // 4. CORE HOLOGRAPHIC ENERGY MATRIX
                // ==========================================
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            primaryColor.copy(alpha = 0.90f),
                            secondaryColor.copy(alpha = 0.75f),
                            Color(0xFF060912)
                        ),
                        center = centerOffset,
                        radius = baseRadius * 0.82f
                    ),
                    radius = baseRadius * 0.80f * pulseScale,
                    center = centerOffset
                )

                // ==========================================
                // 5. INTERNAL REACTION PARTICLES
                // ==========================================
                val particleCount = if (status == AgentStatus.THINKING) 12 else 8
                for (i in 0 until particleCount) {
                    val angle = (rotation * (if (status == AgentStatus.THINKING) 1.2f else 0.8f) + i * (360f / particleCount)) * (PI / 180.0)
                    val particleDist = baseRadius * (0.35f + 0.15f * sin((rotation * 0.05f) + i))
                    val px = centerOffset.x + (particleDist * cos(angle)).toFloat()
                    val py = centerOffset.y + (particleDist * sin(angle)).toFloat()
                    drawCircle(
                        color = Color.White,
                        radius = (if (status == AgentStatus.EXECUTING) 3.5f else 2.5f).dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title with Neon Brand Style
        Text(
            text = "MAYA AI",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // State indicator badge with animated icon & label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(primaryColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = primaryColor
            )
        }

        // Optional Interactive State Selector Bar for testing/switching
        if (showInteractiveModes && onStatusSelect != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OrbStateChip("Listen", AgentStatus.LISTENING, status, Icons.Default.Mic, onStatusSelect)
                Spacer(modifier = Modifier.width(6.dp))
                OrbStateChip("Think", AgentStatus.THINKING, status, Icons.Default.Psychology, onStatusSelect)
                Spacer(modifier = Modifier.width(6.dp))
                OrbStateChip("Execute", AgentStatus.EXECUTING, status, Icons.Default.TouchApp, onStatusSelect)
            }
        }
    }
}

@Composable
private fun OrbStateChip(
    label: String,
    targetStatus: AgentStatus,
    currentStatus: AgentStatus,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onSelect: (AgentStatus) -> Unit
) {
    val isSelected = currentStatus == targetStatus
    val chipBg by animateColorAsState(
        targetValue = if (isSelected) CyberDark700 else CyberDark800,
        label = "chip_bg"
    )
    val chipBorder by animateColorAsState(
        targetValue = if (isSelected) CyanNeon else Color(0xFF1E293B),
        label = "chip_border"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(chipBg)
            .border(1.dp, chipBorder, RoundedCornerShape(12.dp))
            .clickable { onSelect(targetStatus) }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) CyanNeon else TextMuted,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isSelected) Color.White else TextMuted
        )
    }
}
