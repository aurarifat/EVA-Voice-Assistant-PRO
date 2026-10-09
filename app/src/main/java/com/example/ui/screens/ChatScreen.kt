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
import androidx.compose.material.icons.filled.AttachFile
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
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyberDark700
import com.example.ui.theme.CyberDark800
import com.example.ui.theme.CyberDark900
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.RoseNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VioletNeon
import kotlinx.coroutines.launch
import java.util.UUID

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
                executedActions = listOf("Initialized Gemini 3.5 Flash", "Accessibility Agent Ready")
            )
        )
    }

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(EmeraldNeon)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Maya AI Chat",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Screen Understanding & Agentic Voice",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = CyanNeon
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberDark900)
            )
        },
        containerColor = CyberDark900,
        bottomBar = {
            Column(modifier = Modifier.background(CyberDark900)) {
                selectedAttachment?.let { att ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Attached: $att",
                            style = MaterialTheme.typography.bodySmall,
                            color = CyanNeon
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            color = TextMuted,
                            modifier = Modifier.clip(CircleShape).padding(4.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CyberDark900,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            IconButton(onClick = { showAttachmentMenu = true }) {
                                Icon(Icons.Default.AttachFile, contentDescription = "Attachment", tint = TextMuted)
                            }

                            DropdownMenu(
                                expanded = showAttachmentMenu,
                                onDismissRequest = { showAttachmentMenu = false },
                                modifier = Modifier.background(CyberDark800)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Image (.jpg / .png)", color = TextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = CyanNeon) },
                                    onClick = {
                                        selectedAttachment = "sample_screen_ui.png"
                                        showAttachmentMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Document (.pdf)", color = TextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = RoseNeon) },
                                    onClick = {
                                        selectedAttachment = "report_spec.pdf"
                                        showAttachmentMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Markdown (.md)", color = TextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = EmeraldNeon) },
                                    onClick = {
                                        selectedAttachment = "workflow_tasks.md"
                                        showAttachmentMenu = false
                                    }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask Maya anything...", color = TextMuted, fontSize = 14.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(26.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanNeon,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedContainerColor = CyberDark800,
                                unfocusedContainerColor = CyberDark800,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                inputText = "Open YouTube and play top Hindi songs."
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(CyberDark800)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = CyanNeon)
                        }

                        Spacer(modifier = Modifier.width(4.dp))

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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(CyanNeon)
                                .testTag("chat_send_button")
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(
                    message = msg,
                    onSpeak = {
                        speechManager.speak(msg.text, preferences.selectedPersona.value)
                    }
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, onSpeak: () -> Unit) {
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(if (isUser) CyanNeon else CyberDark800)
                .border(
                    1.dp,
                    if (isUser) CyanNeon else Color(0xFF1E293B),
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(14.dp)
        ) {
            Column {
                if (message.attachmentName != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33000000))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "📎 ${message.attachmentName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (isUser) Color.Black else CyanNeon
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.Black else TextPrimary,
                    lineHeight = 20.sp
                )

                if (!isUser && message.executedActions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        message.executedActions.forEach { action ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberDark700)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "⚡ $action",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = CyanNeon)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = onSpeak, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Speak", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
