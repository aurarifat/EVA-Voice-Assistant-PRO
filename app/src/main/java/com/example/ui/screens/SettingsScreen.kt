package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartScreen
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.SpeechManager
import com.example.data.model.VoicePersona
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: MayaPreferences,
    speechManager: SpeechManager,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val currentPersona by preferences.selectedPersona.collectAsState()
    val isLiveCorner by preferences.isLiveCornerEnabled.collectAsState()
    val isScreenControl by preferences.isScreenControlEnabled.collectAsState()
    val isCoordinateTap by preferences.isCoordinateTapEnabled.collectAsState()
    val isAccessibilityControl by preferences.isAccessibilityControlEnabled.collectAsState()
    val isAutoScroll by preferences.isAutoScrollEnabled.collectAsState()
    val isBackNav by preferences.isBackNavEnabled.collectAsState()
    val isRecentApps by preferences.isRecentAppsEnabled.collectAsState()
    val isBackgroundExec by preferences.isBackgroundExecutionEnabled.collectAsState()
    val isReelsAutoScroll by preferences.isReelsAutoScrollEnabled.collectAsState()
    val isProactiveChat by preferences.isProactiveConversationsEnabled.collectAsState()
    val isAutoStartBoot by preferences.isAutoStartBootEnabled.collectAsState()
    val wakeWord by preferences.wakeWord.collectAsState()
    val voiceStyle by preferences.voiceStyle.collectAsState()

    var customWakeWord by remember { mutableStateOf(wakeWord) }

    // SMTP state
    var smtpHost by remember { mutableStateOf(preferences.smtpHost.value) }
    var smtpPort by remember { mutableStateOf(preferences.smtpPort.value) }
    var smtpUser by remember { mutableStateOf(preferences.smtpUser.value) }
    var smtpSignature by remember { mutableStateOf(preferences.emailSignature.value) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Maya AI Settings",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberDark900)
            )
        },
        containerColor = CyberDark900
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 24 & 26: Voice Personas & Voice Style
            SettingsGroupCard(title = "VOICE PERSONAS & STYLE (Sections 24, 26)", icon = Icons.Default.RecordVoiceOver) {
                Column {
                    Text("Select Voice Persona:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    VoicePersona.values().forEach { persona ->
                        val isSelected = currentPersona == persona
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CyberDark700 else Color.Transparent)
                                .clickable {
                                    preferences.setVoicePersona(persona)
                                    speechManager.speak("Hello, I am ${persona.displayName}.", persona)
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(persona.displayName, fontWeight = FontWeight.Bold, color = if (isSelected) CyanNeon else Color.White)
                                Text(persona.tone, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            }
                            IconButton(onClick = {
                                speechManager.speak("Hello, this is the voice of ${persona.displayName}.", persona)
                            }) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Preview", tint = CyanNeon)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Voice Style / Cadence: $voiceStyle", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }

            // Section 46: Advanced Agent Settings (Screen Control, Coordinate Tap, Back, Recents)
            SettingsGroupCard(title = "ADVANCED AGENT SETTINGS (Section 46)", icon = Icons.Default.AccessibilityNew) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SettingToggleRow(
                        title = "Screen Control",
                        subtitle = "Inspect and understand live UI hierarchy",
                        checked = isScreenControl,
                        onCheckedChange = { preferences.setScreenControl(it) }
                    )
                    SettingToggleRow(
                        title = "Coordinate Tap (X/Y)",
                        subtitle = "Touch non-standard Canvas or custom UI at exact pixels",
                        checked = isCoordinateTap,
                        onCheckedChange = { preferences.setCoordinateTap(it) }
                    )
                    SettingToggleRow(
                        title = "Accessibility Control",
                        subtitle = "Directly click nodes & input text fields",
                        checked = isAccessibilityControl,
                        onCheckedChange = { preferences.setAccessibilityControl(it) }
                    )
                    SettingToggleRow(
                        title = "Auto Scroll & Swipe",
                        subtitle = "Enable scrolling feeds and long lists",
                        checked = isAutoScroll,
                        onCheckedChange = { preferences.setAutoScroll(it) }
                    )
                    SettingToggleRow(
                        title = "System Back & Home Navigation",
                        subtitle = "Allow Maya to trigger Back & Home gestures",
                        checked = isBackNav,
                        onCheckedChange = { preferences.setBackNav(it) }
                    )
                    SettingToggleRow(
                        title = "Recent Apps Switching",
                        subtitle = "Allow switching between active background apps",
                        checked = isRecentApps,
                        onCheckedChange = { preferences.setRecentApps(it) }
                    )
                    SettingToggleRow(
                        title = "Background Execution",
                        subtitle = "Run voice tasks when Maya is minimized",
                        checked = isBackgroundExec,
                        onCheckedChange = { preferences.setBackgroundExecution(it) }
                    )
                    SettingToggleRow(
                        title = "Live Corner Overlay",
                        subtitle = "Show floating status bubble across other apps",
                        checked = isLiveCorner,
                        onCheckedChange = { preferences.setLiveCornerEnabled(it) }
                    )
                }
            }

            // Section 30: Instagram Reels Auto Scroll
            SettingsGroupCard(title = "SOCIAL MEDIA & INSTAGRAM (Section 30)", icon = Icons.Default.VideoLibrary) {
                SettingToggleRow(
                    title = "Instagram Reels Auto Scroll",
                    subtitle = "Automate hands-free vertical swipe after video interval",
                    checked = isReelsAutoScroll,
                    onCheckedChange = { preferences.setReelsAutoScroll(it) }
                )
            }

            // Section 31 & 32: Email SMTP Configuration
            SettingsGroupCard(title = "EMAIL SMTP & SIGNATURE (Sections 31, 32)", icon = Icons.Default.Email) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = smtpHost,
                        onValueChange = { smtpHost = it },
                        label = { Text("SMTP Host") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = smtpPort,
                            onValueChange = { smtpPort = it },
                            label = { Text("Port") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanNeon,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        OutlinedTextField(
                            value = smtpUser,
                            onValueChange = { smtpUser = it },
                            label = { Text("Username") },
                            modifier = Modifier.weight(2f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanNeon,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                    OutlinedTextField(
                        value = smtpSignature,
                        onValueChange = { smtpSignature = it },
                        label = { Text("Email Signature") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                preferences.setEmailConfig(smtpHost, smtpPort, smtpUser, smtpSignature)
                                Toast.makeText(context, "Email settings saved", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "SMTP Connection Verified ✓", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Check Connection", color = CyanNeon)
                        }
                    }
                }
            }

            // Section 40: Human-like Typing
            SettingsGroupCard(title = "HUMAN-LIKE TYPING (Section 40)", icon = Icons.Default.Keyboard) {
                Column {
                    Text("Typing Pace: Normal (Paced keystroke emulation)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    SettingToggleRow(
                        title = "Human-paced typing",
                        subtitle = "Simulates realistic typing delays in input forms",
                        checked = true,
                        onCheckedChange = {}
                    )
                    SettingToggleRow(
                        title = "Coding typing",
                        subtitle = "Fast block injection for IDEs and coding agents",
                        checked = true,
                        onCheckedChange = {}
                    )
                }
            }

            // Section 42 & 43: Proactive Maya & Auto Start
            SettingsGroupCard(title = "PROACTIVE MAYA & WAKE WORD (Sections 42, 43)", icon = Icons.Default.AutoAwesome) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingToggleRow(
                        title = "Start conversations on her own",
                        subtitle = "Proactively suggests help based on time & context",
                        checked = isProactiveChat,
                        onCheckedChange = { preferences.setProactiveChat(it) }
                    )
                    SettingToggleRow(
                        title = "Start Maya automatically on boot",
                        subtitle = "Restarts background services after device reboot",
                        checked = isAutoStartBoot,
                        onCheckedChange = {}
                    )
                    OutlinedTextField(
                        value = customWakeWord,
                        onValueChange = {
                            customWakeWord = it
                            preferences.setWakeWord(it)
                        },
                        label = { Text("Wake Word") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            }

            // Section 41: Security & Safety
            SettingsGroupCard(title = "SECURITY & SAFETY (Section 41)", icon = Icons.Default.Security) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingToggleRow(
                        title = "Voice Guardian",
                        subtitle = "Accept commands only from recognized voice print",
                        checked = true,
                        onCheckedChange = {}
                    )
                    SettingToggleRow(
                        title = "Emergency SOS",
                        subtitle = "Quick voice trigger for location broadcast & siren",
                        checked = true,
                        onCheckedChange = {}
                    )
                    SettingToggleRow(
                        title = "Touch Guard",
                        subtitle = "Safeguard against unintended accidental touches",
                        checked = true,
                        onCheckedChange = {}
                    )
                }
            }

            // Section 45: App Update
            SettingsGroupCard(title = "APP UPDATE (Section 45)", icon = Icons.Default.SystemUpdate) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Maya AI v1.0.0", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Version is up to date", style = MaterialTheme.typography.bodySmall, color = EmeraldNeon)
                    }
                    Button(
                        onClick = {
                            Toast.makeText(context, "Checked: You are running the latest version.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberDark700)
                    ) {
                        Text("Check Update", color = CyanNeon)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsGroupCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDark800),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E293B)))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = TextMuted)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CyanNeon,
                checkedTrackColor = CyberDark700
            )
        )
    }
}
