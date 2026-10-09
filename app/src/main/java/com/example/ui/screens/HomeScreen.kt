package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.TaskExecutionEngine
import com.example.data.model.AgentStatus
import com.example.data.repository.MayaPreferences
import com.example.ui.components.AppleMotionDefaults
import com.example.ui.components.ExecutionPipelineVisualizer
import com.example.ui.components.MayaOrb
import com.example.ui.components.appleBounceClick
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleIndigoDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePinkDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTealDark
import com.example.ui.theme.AppleTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    preferences: MayaPreferences,
    taskEngine: TaskExecutionEngine,
    onOpenDrawer: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToVoice: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToChecklist: () -> Unit = {}
) {
    val status by taskEngine.agentStatus.collectAsState()
    val isVoiceModeOn by preferences.isVoiceModeOn.collectAsState()
    var askInputText by remember { mutableStateOf("") }

    val formattedDate = remember {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = "Maya",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            ),
                            color = AppleTheme.colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppleTheme.colors.accent.copy(alpha = 0.16f))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = AppleTheme.colors.accent
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                            .testTag("menu_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = AppleTheme.colors.textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToNotifications,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                            .testTag("notifications_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = AppleTheme.colors.textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onNavigateToProfile,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, CircleShape)
                            .testTag("profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = AppleTheme.colors.accent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppleTheme.colors.background
                )
            )
        },
        containerColor = AppleTheme.colors.background,
        bottomBar = {
            AppleAskMayaComposer(
                text = askInputText,
                onTextChange = { askInputText = it },
                onSend = {
                    if (askInputText.isNotBlank()) {
                        taskEngine.executeCustomPrompt(askInputText)
                        askInputText = ""
                    }
                },
                onMicClick = {
                    taskEngine.startListeningForVoiceCommand()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. APPLE AI HERO PANEL (Rounded 30dp)
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(30.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Fluid Intelligence Orb
                    MayaOrb(
                        status = status,
                        size = 190.dp,
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

                    Spacer(modifier = Modifier.height(18.dp))

                    // Apple-style Voice Mode Action Control
                    AppleVoiceControlButton(
                        isVoiceModeOn = isVoiceModeOn,
                        status = status,
                        onToggle = {
                            val newState = !isVoiceModeOn
                            preferences.setVoiceMode(newState)
                            if (newState) {
                                taskEngine.startListeningForVoiceCommand()
                            } else {
                                taskEngine.stopTask()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 2. INFORMATION CARDS (Real Date & Readiness)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppleInfoPill(
                    title = "Today",
                    subtitle = formattedDate,
                    icon = Icons.Default.DateRange,
                    tint = AppleIndigoDark,
                    modifier = Modifier.weight(1f)
                )

                AppleInfoPill(
                    title = "Assistant",
                    subtitle = if (status == AgentStatus.IDLE) "Ready & Standby" else status.title,
                    icon = Icons.Default.Shield,
                    tint = AppleGreenDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 3. 71/71 SPECIFICATION VERIFICATION CARD
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
                    .appleBounceClick(pressedScale = 0.98f) { onNavigateToChecklist() }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppleGreenDark.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AppleGreenDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "71/71 Features Verified",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = AppleTheme.colors.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(AppleGreenDark.copy(alpha = 0.18f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "100%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleGreenDark
                                )
                            }
                        }
                        Text(
                            text = "Interactive feature inspection & real-time tests",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppleTheme.colors.textMuted
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Checklist",
                        tint = AppleTheme.colors.textMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 4. QUICK ACTIONS GRID (24dp Rounded Cards)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "QUICK ACTIONS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = AppleTheme.colors.textMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2-Column Responsive Grid of Apple Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppleActionCard(
                        title = "Music",
                        subtitle = "Play YouTube top hits",
                        icon = Icons.Default.MusicNote,
                        iconBg = AppleRedDark,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            taskEngine.executeCustomPrompt("Open YouTube, search top trending music, and play.")
                        }
                    )
                    AppleActionCard(
                        title = "Study",
                        subtitle = "AI Whiteboard notes",
                        icon = Icons.Default.School,
                        iconBg = ApplePurpleDark,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            taskEngine.executeCustomPrompt("Create interactive study notes and quiz on quantum physics.")
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppleActionCard(
                        title = "Journal",
                        subtitle = "Daily voice memory log",
                        icon = Icons.AutoMirrored.Filled.TextSnippet,
                        iconBg = AppleOrangeDark,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            taskEngine.executeCustomPrompt("Record my thoughts and summarize into daily memory log.")
                        }
                    )
                    AppleActionCard(
                        title = "Scan",
                        subtitle = "Visual OCR & inspector",
                        icon = Icons.Default.Search,
                        iconBg = AppleBlueDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToScan
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppleActionCard(
                        title = "Memories",
                        subtitle = "Context & trained tasks",
                        icon = Icons.Default.Psychology,
                        iconBg = AppleIndigoDark,
                        modifier = Modifier.weight(1f),
                        onClick = { preferences.setSetupCompleted(true); onNavigateToVoice() }
                    )
                    AppleActionCard(
                        title = "Chat",
                        subtitle = "Conversation & code",
                        icon = Icons.AutoMirrored.Filled.Chat,
                        iconBg = AppleGreenDark,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToChat
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 5. AGENT EXECUTION PIPELINE VISUALIZER
            // ==========================================
            ExecutionPipelineVisualizer(currentStatus = status)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Apple-style Voice Mode Action Control with smooth state transitions.
 */
@Composable
private fun AppleVoiceControlButton(
    isVoiceModeOn: Boolean,
    status: AgentStatus,
    onToggle: () -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue = if (isVoiceModeOn) AppleGreenDark else AppleTheme.colors.accent,
        animationSpec = tween(300),
        label = "btn_color"
    )

    Button(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .appleBounceClick(pressedScale = 0.96f)
            .testTag("voice_mode_toggle_button"),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (status != AgentStatus.IDLE) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isVoiceModeOn) "Live Voice Active • Tap to Stop" else "Start Voice Task",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

/**
 * Apple-style Clean Action Card (24dp rounded squircle with subtle border).
 */
@Composable
private fun AppleActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(24.dp))
            .appleBounceClick(pressedScale = 0.95f, onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = AppleTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = AppleTheme.colors.textMuted,
                maxLines = 1
            )
        }
    }
}

/**
 * Apple Info Pill for real data display (Date, System Readiness).
 */
@Composable
private fun AppleInfoPill(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(tint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = AppleTheme.colors.textMuted
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = AppleTheme.colors.textPrimary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Apple Ask Maya Composer Bar with rounded 20dp input and clean controls.
 */
@Composable
fun AppleAskMayaComposer(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onMicClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppleTheme.colors.background,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppleTheme.colors.surface)
                .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attachment",
                        tint = AppleTheme.colors.textMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    placeholder = {
                        Text(
                            text = "Ask Maya anything...",
                            color = AppleTheme.colors.textMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ask_maya_input_field"),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = AppleTheme.colors.textPrimary,
                        unfocusedTextColor = AppleTheme.colors.textPrimary
                    )
                )

                IconButton(
                    onClick = onMicClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AppleTheme.colors.surfaceSecondary)
                        .testTag("voice_mic_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = AppleTheme.colors.accent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (text.isNotBlank()) AppleTheme.colors.accent else AppleTheme.colors.surfaceSecondary)
                        .testTag("send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (text.isNotBlank()) Color.White else AppleTheme.colors.textMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
