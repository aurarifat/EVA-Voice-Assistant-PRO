package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.TaskExecutionEngine
import com.example.data.model.AgentStatus
import com.example.data.repository.MayaPreferences
import com.example.ui.components.MayaOrb
import com.example.ui.components.appleBounceClick
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTheme

/**
 * 🍎 Maya AI Voice Mode — Apple Siri / Audio-Inspired Fluid Voice Interface.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceModeScreen(
    preferences: MayaPreferences,
    taskEngine: TaskExecutionEngine,
    onBack: () -> Unit
) {
    val status by taskEngine.agentStatus.collectAsState()
    val isAgentic by preferences.isAgenticVoiceMode.collectAsState()
    val persona by preferences.selectedPersona.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live Voice Mode",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp
                            ),
                            color = AppleTheme.colors.textPrimary
                        )
                        Text(
                            text = "Persona: ${persona.displayName} • ${persona.tone.take(20)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppleTheme.colors.accent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppleTheme.colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppleTheme.colors.background
                )
            )
        },
        containerColor = AppleTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Switch (Apple Grouped Container)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isAgentic) "Advanced Agentic Mode" else "Natural Conversation Mode",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = AppleTheme.colors.textPrimary
                            )
                            if (isAgentic) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ApplePurpleDark.copy(alpha = 0.16f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        color = ApplePurpleDark,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isAgentic)
                                "Includes multi-step planning, screen perception, and device actions"
                            else
                                "Instant fluid vocal feedback and conversation",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppleTheme.colors.textMuted
                        )
                    }

                    Switch(
                        checked = isAgentic,
                        onCheckedChange = { preferences.setAgenticVoiceMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AppleTheme.colors.accent,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = AppleTheme.colors.surfaceSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Fluid Holographic Voice Orb
            MayaOrb(
                status = status,
                size = 230.dp,
                showInteractiveModes = true,
                onStatusSelect = { newStatus ->
                    taskEngine.updateStatus(newStatus)
                },
                onClick = {
                    if (status == AgentStatus.IDLE) {
                        taskEngine.startListeningForVoiceCommand()
                    } else {
                        taskEngine.stopTask()
                    }
                }
            )

            // Animated Audio Waveform (iOS Siri Audio reactive visualizer)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(44.dp)
            ) {
                for (i in 0 until 18) {
                    val barHeight by infiniteTransition.animateFloat(
                        initialValue = 6f,
                        targetValue = if (status == AgentStatus.LISTENING || status == AgentStatus.SPEAKING) {
                            (16 + (i % 6) * 5).toFloat()
                        } else {
                            5f
                        },
                        animationSpec = infiniteRepeatable(
                            animation = tween(380 + i * 35, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "wave_$i"
                    )

                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height(barHeight.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (status == AgentStatus.SPEAKING) AppleGreenDark else AppleTheme.colors.accent
                            )
                    )
                }
            }

            // Status Description Card (Apple Grouped Container)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = status.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
                        ),
                        color = when (status) {
                            AgentStatus.ERROR -> AppleRedDark
                            AgentStatus.COMPLETED -> AppleGreenDark
                            AgentStatus.SPEAKING -> AppleGreenDark
                            else -> AppleTheme.colors.accent
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = status.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppleTheme.colors.textSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Controls Bottom Row (Apple Style Pill / Rounded Button)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (status == AgentStatus.IDLE) {
                            taskEngine.startListeningForVoiceCommand()
                        } else {
                            taskEngine.stopTask()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .appleBounceClick(pressedScale = 0.96f)
                        .testTag("voice_screen_action_button"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (status == AgentStatus.IDLE) AppleBlueDark else AppleRedDark
                    )
                ) {
                    Icon(
                        imageVector = if (status == AgentStatus.IDLE) Icons.Default.Mic else Icons.Default.Stop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (status == AgentStatus.IDLE) "Start Listening" else "Stop Agent",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}
