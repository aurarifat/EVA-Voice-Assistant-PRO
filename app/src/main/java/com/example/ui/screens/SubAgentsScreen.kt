package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.appleBounceClick
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleTealDark
import com.example.ui.theme.AppleTheme

data class SubAgentInfo(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val tint: Color,
    val status: String
)

/**
 * 🍎 Maya AI Sub-Agents — Apple-Inspired Autonomous Micro-Services Directory.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubAgentsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val subAgents = listOf(
        SubAgentInfo(
            "coding",
            "Coding Specialist",
            "Specialized in writing Kotlin code, debugging syntax, generating Android composables, and automated testing.",
            Icons.Default.Code,
            AppleBlueDark,
            "ACTIVE"
        ),
        SubAgentInfo(
            "bg",
            "Background Orchestrator",
            "Coordinates long-running background tasks, timed reminders, notification watchers, and auto sync.",
            Icons.Default.Layers,
            ApplePurpleDark,
            "RUNNING"
        ),
        SubAgentInfo(
            "research",
            "Research Agent",
            "Deep search, news aggregation, document synthesis, and automated information gathering.",
            Icons.Default.Psychology,
            AppleGreenDark,
            "STANDBY"
        ),
        SubAgentInfo(
            "automation",
            "Device Automation Agent",
            "Multi-step phone operation, Accessibility node clicks, X/Y coordinate gestures, and app control.",
            Icons.Default.TouchApp,
            AppleOrangeDark,
            "READY"
        ),
        SubAgentInfo(
            "vision",
            "Vision Perception Agent",
            "Camera frame analysis, OCR text reading, screen element detection, and visual layout understanding.",
            Icons.Default.Visibility,
            AppleTealDark,
            "ACTIVE"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Specialized Sub-Agents",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp
                        ),
                        color = AppleTheme.colors.textPrimary
                    )
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "SPECIALIZED AUTONOMOUS WORKERS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppleTheme.colors.textMuted,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(subAgents.size) { index ->
                    val agent = subAgents[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(AppleTheme.colors.surface)
                            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(22.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(agent.tint.copy(alpha = 0.14f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = agent.icon,
                                            contentDescription = null,
                                            tint = agent.tint,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = agent.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = AppleTheme.colors.textPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(agent.tint.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = agent.status,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = agent.tint,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = agent.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppleTheme.colors.textSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    Toast.makeText(context, "${agent.title} engaged.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .appleBounceClick(pressedScale = 0.95f),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = agent.tint
                                )
                            ) {
                                Text(
                                    text = "Engage Sub-Agent",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
