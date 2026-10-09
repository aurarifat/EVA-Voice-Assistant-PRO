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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyberDark700
import com.example.ui.theme.CyberDark800
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VioletNeon

data class PipelineStage(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val activeStatuses: List<AgentStatus>
)

@Composable
fun ExecutionPipelineVisualizer(
    currentStatus: AgentStatus,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        PipelineStage("1. USER VOICE", "Listening to instruction", Icons.Default.Mic, listOf(AgentStatus.LISTENING)),
        PipelineStage("2. UNDERSTAND & PLAN", "Gemini AI Task decomposition", Icons.Default.Psychology, listOf(AgentStatus.THINKING)),
        PipelineStage("3. OPEN APP", "Launch target Android application", Icons.Default.PhoneAndroid, listOf(AgentStatus.NAVIGATING)),
        PipelineStage("4. READ SCREEN", "Screen Intelligence Engine UI tree", Icons.Default.Visibility, listOf(AgentStatus.ANALYSING)),
        PipelineStage("5. LOCATE ELEMENT", "Accessibility Node or X/Y Coords", Icons.Default.CropFree, listOf(AgentStatus.LOCATING)),
        PipelineStage("6. TAP / SWIPE / TYPE", "Dispatch accessibility/coordinate action", Icons.Default.TouchApp, listOf(AgentStatus.EXECUTING, AgentStatus.SCROLLING)),
        PipelineStage("7. TASK COMPLETE", "Finished end-to-end phone flow", Icons.Default.CheckCircle, listOf(AgentStatus.COMPLETED))
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDark800),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E293B)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (currentStatus != AgentStatus.IDLE) EmeraldNeon else CyanNeon)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AGENT EXECUTION PIPELINE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White
                    )
                }

                Text(
                    text = currentStatus.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = CyanNeon
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            stages.forEachIndexed { index, stage ->
                val isActive = stage.activeStatuses.contains(currentStatus)
                val bgColor by animateColorAsState(
                    targetValue = if (isActive) CyberDark700 else Color.Transparent,
                    label = "bg"
                )
                val borderColor by animateColorAsState(
                    targetValue = if (isActive) CyanNeon else Color.Transparent,
                    label = "border"
                )
                val iconColor by animateColorAsState(
                    targetValue = if (isActive) CyanNeon else TextMuted,
                    label = "icon"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isActive) CyanNeon.copy(alpha = 0.2f) else Color(0xFF172033)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = stage.icon,
                            contentDescription = stage.title,
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stage.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isActive) Color.White else TextPrimary
                        )
                        Text(
                            text = stage.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }

                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyanNeon)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = Color.Black
                            )
                        }
                    }
                }

                if (index < stages.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(start = 26.dp)
                            .width(2.dp)
                            .height(10.dp)
                            .background(if (isActive) CyanNeon else Color(0xFF1E293B))
                    )
                }
            }
        }
    }
}
