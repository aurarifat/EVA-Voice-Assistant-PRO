package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agent.TaskExecutionEngine
import com.example.data.model.AgentStatus
import com.example.data.model.TaskStep
import com.example.service.MayaAccessibilityService
import com.example.ui.components.MayaOrb
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTheme

/**
 * 🍎 Maya AI Live Voice Task Engine — Apple-Inspired Execution Console.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveVoiceTaskScreen(
    taskEngine: TaskExecutionEngine,
    onBack: () -> Unit
) {
    val status by taskEngine.agentStatus.collectAsState()
    val activeSteps by taskEngine.activeSteps.collectAsState()
    val logs by taskEngine.executionLogs.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live Task Engine",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp
                            ),
                            color = AppleTheme.colors.textPrimary
                        )
                        Text(
                            text = status.title,
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
                actions = {
                    IconButton(
                        onClick = { taskEngine.stopTask() },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AppleRedDark.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Task",
                            tint = AppleRedDark,
                            modifier = Modifier.size(20.dp)
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Interactive Status Orb
            MayaOrb(
                status = status,
                size = 140.dp,
                onClick = {
                    if (status == AgentStatus.IDLE) {
                        taskEngine.startListeningForVoiceCommand()
                    } else {
                        taskEngine.stopTask()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Preset Voice Task Trigger (Apple Hero Card 24dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AUTOMATION FLOW DEMO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AppleTheme.colors.accent,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ApplePurpleDark.copy(alpha = 0.16f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "YouTube Hindi Songs",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ApplePurpleDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "«Open YouTube → Search top Hindi songs → Tap 2nd result at (X: 720, Y: 1380)»",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = AppleTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            taskEngine.startListeningForVoiceCommand()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppleBlueDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Run Voice Task Sequence",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step-by-Step Task Progress Header
            Text(
                text = "TASK EXECUTION STEPS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = AppleTheme.colors.textMuted,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (activeSteps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(AppleTheme.colors.surface)
                        .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No active task. Click 'Run Voice Task Sequence' or speak a command to start.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppleTheme.colors.textMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                activeSteps.forEach { step ->
                    TaskStepCard(step = step)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Gesture & System Navigation Controls Header
            Text(
                text = "GESTURE & SYSTEM NAVIGATION CONTROLS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = AppleTheme.colors.textMuted,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Touch Actions (Sections 08, 09, 10)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AppleTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.tapCoordinate(720f, 1380f)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Tap (720, 1380)",
                                color = AppleTheme.colors.accent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.scrollDown()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Scroll Down",
                                color = AppleTheme.colors.textPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.scrollUp()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Scroll Up",
                                color = AppleTheme.colors.textPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "System Navigation (Section 11)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AppleTheme.colors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.goBack()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Back",
                                color = ApplePurpleDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.goHome()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Home",
                                color = AppleGreenDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                MayaAccessibilityService.instance?.openRecentApps()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Recent Apps",
                                color = AppleTheme.colors.accent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Live Execution Log Stream Header
            Text(
                text = "LIVE AGENT LOG STREAM",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = AppleTheme.colors.textMuted,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppleTheme.colors.surface)
                    .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    reverseLayout = false
                ) {
                    items(logs) { logLine ->
                        Text(
                            text = logLine,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (logLine.contains("Successfully") || logLine.contains("completed")) {
                                AppleGreenDark
                            } else {
                                AppleTheme.colors.textSecondary
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskStepCard(step: TaskStep) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppleTheme.colors.surface)
            .border(
                0.5.dp,
                if (step.isExecuting) AppleBlueDark else AppleTheme.colors.cardBorder,
                RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            step.isCompleted -> AppleGreenDark.copy(alpha = 0.16f)
                            step.isExecuting -> AppleBlueDark.copy(alpha = 0.16f)
                            else -> AppleTheme.colors.surfaceSecondary
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (step.isExecuting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = AppleBlueDark,
                        strokeWidth = 2.dp
                    )
                } else if (step.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = AppleGreenDark,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "${step.stepNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AppleTheme.colors.textMuted
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (step.isExecuting) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = AppleTheme.colors.textPrimary
                )
                if (step.coordinateX != null && step.coordinateY != null) {
                    Text(
                        text = "Target Coordinate: (X: ${step.coordinateX}, Y: ${step.coordinateY})",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = AppleTheme.colors.accent
                    )
                } else if (step.targetElement != null) {
                    Text(
                        text = "Target Element: '${step.targetElement}'",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = ApplePurpleDark
                    )
                }
            }
        }
    }
}
