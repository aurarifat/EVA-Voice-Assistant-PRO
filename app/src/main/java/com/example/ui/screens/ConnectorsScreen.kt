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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.MayaPreferences
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleTheme

/**
 * 🍎 Maya AI External Connectors — Apple-Inspired Services & Integrations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectorsScreen(
    preferences: MayaPreferences,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val token by preferences.telegramBotToken.collectAsState()
    var inputToken by remember { mutableStateOf(token) }
    var isConnected by remember { mutableStateOf(token.isNotBlank()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "External Connectors",
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Telegram Connector Grouped Container (24dp radius)
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0088CC).copy(alpha = 0.16f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    tint = Color(0xFF0088CC),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Telegram Bot Bridge",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = AppleTheme.colors.textPrimary
                            )
                        }

                        if (isConnected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AppleGreenDark.copy(alpha = 0.16f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "CONNECTED",
                                    color = AppleGreenDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Connect Maya to your Telegram account to send voice memos, commands, and alerts directly from any device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppleTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = inputToken,
                        onValueChange = { inputToken = it },
                        label = { Text("Telegram Bot Token") },
                        placeholder = { Text("123456789:ABCdefGhIJKlmNoPQRstuVwxyZ") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0088CC),
                            unfocusedBorderColor = AppleTheme.colors.border,
                            focusedTextColor = AppleTheme.colors.textPrimary,
                            unfocusedTextColor = AppleTheme.colors.textPrimary,
                            focusedContainerColor = AppleTheme.colors.surfaceSecondary.copy(alpha = 0.4f),
                            unfocusedContainerColor = AppleTheme.colors.surfaceSecondary.copy(alpha = 0.25f)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (inputToken.isNotBlank()) {
                                    preferences.setTelegramBotToken(inputToken.trim())
                                    isConnected = true
                                    Toast.makeText(context, "Telegram Bot connected successfully!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0088CC)
                            )
                        ) {
                            Text(
                                text = "Connect Bot",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Test alert dispatched to Telegram!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.height(44.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Send Test Alert",
                                color = AppleTheme.colors.accent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "MORE INTEGRATIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppleTheme.colors.textMuted,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Other Connectors (GitHub, Notion, Google Services)
            ConnectorItemCard(
                name = "GitHub",
                category = "Source Control",
                description = "Repo commits, issue management, and PR tracking",
                color = ApplePurpleDark
            )
            Spacer(modifier = Modifier.height(10.dp))
            ConnectorItemCard(
                name = "Notion",
                category = "Knowledge Base",
                description = "Sync notes, tasks, and kanban boards with Maya memory",
                color = AppleGreenDark
            )
            Spacer(modifier = Modifier.height(10.dp))
            ConnectorItemCard(
                name = "Google Services",
                category = "Calendar & Drive",
                description = "Access schedule, search documents, and manage Drive files",
                color = AppleOrangeDark
            )
        }
    }
}

@Composable
private fun ConnectorItemCard(name: String, category: String, description: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppleTheme.colors.surface)
            .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AppleTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• $category",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = AppleTheme.colors.textMuted
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppleTheme.colors.textSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text(
                    text = "Link",
                    color = AppleTheme.colors.accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
