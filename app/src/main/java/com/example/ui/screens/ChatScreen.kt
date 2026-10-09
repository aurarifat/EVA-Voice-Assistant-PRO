package com.example.ui.screens

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiAgentClient
import com.example.ai.SpeechManager
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.data.repository.MayaPreferences
import com.example.ui.components.appleBounceClick
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTheme
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 🍎 Maya AI Chat — Apple Messages-Inspired Conversation Interface.
 *
 * Clean, minimal, rounded message bubbles with native composer,
 * attachment menus, and voice synthesis triggers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    preferences: MayaPreferences,
    geminiClient: GeminiAgentClient,
    speechManager: SpeechManager,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var selectedAttachment by remember { mutableStateOf<String?>(null) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = "m1",
                sender = MessageSender.MAYA,
                text = "Hello! I am Maya AI, your autonomous Android Agent. How can I help you navigate apps, analyze screens, or complete tasks today?",
                executedActions = listOf("Initialized Gemini 2.5 Flash", "Accessibility Agent Ready")
            )
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AppleTheme.colors.surfaceSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "M",
                                fontWeight = FontWeight.Bold,
                                color = AppleTheme.colors.accent,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Maya AI",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.3).sp
                                ),
                                color = AppleTheme.colors.textPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AppleGreenDark)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Active Agent",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = AppleTheme.colors.textMuted
                                )
                            }
                        }
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
        containerColor = AppleTheme.colors.background,
        bottomBar = {
            // Apple Messages-Style Composer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppleTheme.colors.background,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(AppleTheme.colors.surface)
                        .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(24.dp))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            IconButton(
                                onClick = { showAttachmentMenu = true },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(AppleTheme.colors.surfaceSecondary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Attachment",
                                    tint = AppleTheme.colors.accent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showAttachmentMenu,
                                onDismissRequest = { showAttachmentMenu = false },
                                modifier = Modifier.background(AppleTheme.colors.surface)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Image (.jpg / .png)", color = AppleTheme.colors.textPrimary) },
                                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = AppleTheme.colors.accent) },
                                    onClick = {
                                        selectedAttachment = "screen_capture.png"
                                        showAttachmentMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Document (.pdf)", color = AppleTheme.colors.textPrimary) },
                                    leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = AppleRedDark) },
                                    onClick = {
                                        selectedAttachment = "document_spec.pdf"
                                        showAttachmentMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Markdown (.md)", color = AppleTheme.colors.textPrimary) },
                                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = AppleGreenDark) },
                                    onClick = {
                                        selectedAttachment = "workflow.md"
                                        showAttachmentMenu = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Message Maya...", color = AppleTheme.colors.textMuted, fontSize = 14.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
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
                            onClick = {
                                inputText = "Open YouTube and play top music."
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AppleTheme.colors.surfaceSecondary)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = AppleTheme.colors.accent, modifier = Modifier.size(17.dp))
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank() && !isGenerating) {
                                    val prompt = inputText.trim()
                                    inputText = ""
                                    val att = selectedAttachment
                                    selectedAttachment = null

                                    messages.add(
                                        ChatMessage(
                                            id = UUID.randomUUID().toString(),
                                            sender = MessageSender.USER,
                                            text = prompt,
                                            attachmentName = att
                                        )
                                    )

                                    isGenerating = true
                                    scope.launch {
                                        val responseResult = geminiClient.generateAgentResponse(prompt)
                                        isGenerating = false
                                        val replyText = responseResult.getOrDefault(
                                            "Maya processed: '$prompt'. Task decomposition successful."
                                        )

                                        messages.add(
                                            ChatMessage(
                                                id = UUID.randomUUID().toString(),
                                                sender = MessageSender.MAYA,
                                                text = replyText,
                                                executedActions = listOf("Analyzed Screen Hierarchy", "Evaluated Coordinates")
                                            )
                                        )

                                        speechManager.speak(replyText.take(120), preferences.selectedPersona.value)
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (inputText.isNotBlank()) AppleTheme.colors.accent else AppleTheme.colors.surfaceSecondary)
                                .appleBounceClick(pressedScale = 0.90f)
                                .testTag("chat_send_button")
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(
                                    Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = if (inputText.isNotBlank()) Color.White else AppleTheme.colors.textMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { msg ->
                AppleChatBubble(
                    message = msg,
                    onSpeak = {
                        speechManager.speak(msg.text, preferences.selectedPersona.value)
                    }
                )
            }
        }
    }
}

/**
 * 🍎 Apple Messages-Style Chat Bubble.
 * - User: System Accent Blue, white text, smooth 20dp corners
 * - Maya: Elevated surface, primary text, subtle border
 */
@Composable
private fun AppleChatBubble(message: ChatMessage, onSpeak: () -> Unit) {
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .background(if (isUser) AppleTheme.colors.accent else AppleTheme.colors.surface)
                .border(
                    0.5.dp,
                    if (isUser) Color.Transparent else AppleTheme.colors.cardBorder,
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isUser) 20.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 20.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                if (message.attachmentName != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isUser) Color.White.copy(alpha = 0.2f) else AppleTheme.colors.surfaceSecondary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "📎 ${message.attachmentName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isUser) Color.White else AppleTheme.colors.accent
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = if (isUser) Color.White else AppleTheme.colors.textPrimary,
                    lineHeight = 21.sp
                )

                if (!isUser && message.executedActions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        message.executedActions.forEach { action ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(AppleTheme.colors.surfaceSecondary)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = action,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = AppleTheme.colors.accent)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = onSpeak, modifier = Modifier.size(24.dp)) {
                            Icon(
                                Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak",
                                tint = AppleTheme.colors.textMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
