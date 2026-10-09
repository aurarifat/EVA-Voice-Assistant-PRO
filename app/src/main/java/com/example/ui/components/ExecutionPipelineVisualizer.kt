package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentStatus
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleIndigoDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePinkDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleTealDark
import com.example.ui.theme.AppleTheme

data class PipelineStage(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBg: Color,
    val activeStatuses: List<AgentStatus>
)

/**
 * 🍎 Apple-Inspired Agent Execution Pipeline Visualizer.
 * Clean, structured 24dp card showing the autonomous phone task flow.
 */
@Composable
fun ExecutionPipelineVisualizer(
    currentStatus: AgentStatus,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        PipelineStage("1. USER VOICE", "Listening to instruction", Icons.Default.Mic, AppleTealDark, listOf(AgentStatus.LISTENING)),
        PipelineStage("2. UNDERSTAND & PLAN", "Gemini AI task decomposition", Icons.Default.Psychology, ApplePurpleDark, listOf(AgentStatus.THINKING)),
        PipelineStage("3. OPEN APP", "Launch target application", Icons.Default.PhoneAndroid, AppleBlueDark, listOf(AgentStatus.NAVIGATING)),
        PipelineStage("4. READ SCREEN", "Screen Intelligence Engine UI tree", Icons.Default.Visibility, AppleIndigoDark, listOf(AgentStatus.ANALYSING)),
        PipelineStage("5. LOCATE ELEMENT", "Accessibility node or X/Y pixels", Icons.Default.CropFree, AppleOrangeDark, listOf(AgentStatus.LOCATING)),
        PipelineStage("6. TAP / SWIPE / TYPE", "Dispatch accessibility/coordinate action", Icons.Default.TouchApp, ApplePinkDark, listOf(AgentStatus.EXECUTING, AgentStatus.SCROLLING)),
        PipelineStage("7. TASK COMPLETE", "Finished end-to-end phone task", Icons.Default.CheckCircle, AppleGreenDark, listOf(AgentStatus.COMPLETED))
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (currentStatus != AgentStatus.IDLE) AppleGreenDark else AppleTheme.colors.accent)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AGENT PIPELINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.6.sp
                        ),
                        color = AppleTheme.colors.textMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(AppleTheme.colors.surfaceSecondary)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = currentStatus.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        color = AppleTheme.colors.accent
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            stages.forEachIndexed { index, stage ->
                val isActive = stage.activeStatuses.contains(currentStatus)
                val bgColor by animateColorAsState(
                    targetValue = if (isActive) AppleTheme.colors.surfaceSecondary else Color.Transparent,
                    label = "bg"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgColor)
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) stage.iconBg else stage.iconBg.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = stage.icon,
                            contentDescription = stage.title,
                            tint = if (isActive) Color.White else AppleTheme.colors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stage.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = if (isActive) AppleTheme.colors.textPrimary else AppleTheme.colors.textSecondary
                        )
                        Text(
                            text = stage.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = AppleTheme.colors.textMuted
                        )
                    }

                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AppleTheme.colors.accent)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }

                if (index < stages.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(1.5.dp)
                            .height(6.dp)
                            .background(if (isActive) AppleTheme.colors.accent else AppleTheme.colors.border)
                    )
                }
            }
        }
    }
}
