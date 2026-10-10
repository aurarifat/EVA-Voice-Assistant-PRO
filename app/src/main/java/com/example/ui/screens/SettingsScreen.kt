package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartScreen
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

/**
 * 🍎 Maya AI Settings — Apple Settings-Inspired Grouped Interface.
 *
 * Implements modern iOS Settings hierarchy:
 * - Rounded grouped containers (20dp) with hairline indented separators
 * - Apple squircle icons with vibrant semantic backgrounds
 * - Native-style smooth switches
 * - Organized functional sections reflecting actual application states
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: MayaPreferences,
    speechManager: SpeechManager,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val themeMode by preferences.themeMode.collectAsState()
    val currentPersona by preferences.selectedPersona.collectAsState()
    val isLiveCorner by preferences.isLiveCornerEnabled.collectAsState()
    val isScreenControl by preferences.isScreenControlEnabled.collectAsState()
    val isCoordinateTap by preferences.isCoordinateTapEnabled.collectAsState()
    val isAccessibilityControl by preferences.isAccessibilityControlEnabled.collectAsState()
    val isAutoScroll by preferences.isAutoScrollEnabled.collectAsState()
    val isBackNav by preferences.isBackNavEnabled.collectAsState()
    val isRecentApps by preferences.isRecentAppsEnabled.collectAsState()
    val isBackgroundExec by preferences.isBackgroundExecutionEnabled.collectAsState()
    val isAgenticMode by preferences.isAgenticVoiceMode.collectAsState()
    val isReelsAutoScroll by preferences.isReelsAutoScrollEnabled.collectAsState()
    val isProactiveChat by preferences.isProactiveConversationsEnabled.collectAsState()
    val isAutoStartBoot by preferences.isAutoStartBootEnabled.collectAsState()
    val wakeWord by preferences.wakeWord.collectAsState()
    val apiKey by preferences.geminiApiKey.collectAsState()
    val currentModel by preferences.geminiModel.collectAsState()

    var customWakeWord by remember { mutableStateOf(wakeWord) }
    var apiKeyInput by remember { mutableStateOf(apiKey) }
    var showApiKeyInput by remember { mutableStateOf(false) }
    var showModelSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.4).sp
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // ==========================================
            // 1. APPEARANCE (Theme & Visual System)
            // ==========================================
            AppleSettingsGroup(title = "APPEARANCE") {
                AppleSettingsRow(
                    icon = Icons.Default.Palette,
                    iconBg = ApplePurpleDark,
                    title = "Theme Appearance",
                    subtitle = "System, Dark or Light Mode",
                    trailing = {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppleTheme.colors.surfaceSecondary)
                                .padding(2.dp)
                        ) {
                            AppleThemeSegment(
                                label = "Dark",
                                selected = themeMode == "dark",
                                onClick = { preferences.setThemeMode("dark") }
                            )
                            AppleThemeSegment(
                                label = "Light",
                                selected = themeMode == "light",
                                onClick = { preferences.setThemeMode("light") }
                            )
                        }
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.Layers,
                    iconBg = AppleBlueDark,
                    title = "Maya Orb Style",
                    subtitle = "Apple-inspired fluid luminous sphere",
                    trailing = {
                        Text("Active", fontSize = 13.sp, color = AppleTheme.colors.textMuted)
                    }
                )
            }

            // ==========================================
            // 2. VOICE & AI ENGINE
            // ==========================================
            AppleSettingsGroup(title = "VOICE & AI ENGINE") {
                AppleSettingsRow(
                    icon = Icons.Default.AutoAwesome,
                    iconBg = AppleBlueDark,
                    title = "Agentic Voice Mode",
                    subtitle = "Autonomous multi-step planning & execution",
                    trailing = {
                        AppleSwitch(
                            checked = isAgenticMode,
                            onCheckedChange = { preferences.setAgenticVoiceMode(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.Key,
                    iconBg = AppleOrangeDark,
                    title = "Gemini API Key",
                    subtitle = if (apiKey.isNotBlank()) "Connected & Secured" else "Not Configured",
                    onClick = { showApiKeyInput = !showApiKeyInput },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AppleTheme.colors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                if (showApiKeyInput) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = {
                                apiKeyInput = it
                                preferences.setGeminiApiKey(it)
                            },
                            placeholder = { Text("Enter Gemini API Key", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = AppleTheme.colors.surfaceSecondary,
                                unfocusedContainerColor = AppleTheme.colors.surfaceSecondary,
                                focusedBorderColor = AppleTheme.colors.accent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = AppleTheme.colors.textPrimary,
                                unfocusedTextColor = AppleTheme.colors.textPrimary
                            )
                        )
                    }
                }

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.AutoAwesome,
                    iconBg = ApplePurpleDark,
                    title = "Gemini Model",
                    subtitle = currentModel,
                    onClick = { showModelSelector = !showModelSelector },
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AppleTheme.colors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                if (showModelSelector) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        listOf(
                            "gemini-2.5-flash" to "Fast & Intelligent (Recommended)",
                            "gemini-2.5-pro" to "Complex Reasoning & Multimodal",
                            "gemini-1.5-flash" to "Legacy Lightweight Model",
                            "gemini-1.5-pro" to "Legacy Deep Context Model"
                        ).forEach { (modelId, desc) ->
                            val isSelected = currentModel == modelId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) AppleTheme.colors.surfaceSecondary else Color.Transparent)
                                    .clickable {
                                        preferences.setGeminiModel(modelId)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = modelId,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) AppleTheme.colors.accent else AppleTheme.colors.textPrimary,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 11.sp,
                                        color = AppleTheme.colors.textMuted
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = AppleTheme.colors.accent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                AppleGroupDivider()

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VOICE PERSONA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        ),
                        color = AppleTheme.colors.textMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    VoicePersona.values().forEach { persona ->
                        val isSelected = currentPersona == persona
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AppleTheme.colors.surfaceSecondary else Color.Transparent)
                                .clickable {
                                    preferences.setVoicePersona(persona)
                                    speechManager.speak("Hello, I am ${persona.displayName}.", persona)
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = persona.displayName,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) AppleTheme.colors.accent else AppleTheme.colors.textPrimary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = persona.tone,
                                    fontSize = 12.sp,
                                    color = AppleTheme.colors.textMuted
                                )
                            }
                            IconButton(
                                onClick = {
                                    speechManager.speak("Hello, this is ${persona.displayName} from Maya AI.", persona)
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Preview",
                                    tint = AppleTheme.colors.accent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 3. SCREEN CONTROL & ACCESSIBILITY
            // ==========================================
            AppleSettingsGroup(title = "SCREEN CONTROL & ACCESSIBILITY") {
                AppleSettingsRow(
                    icon = Icons.Default.AccessibilityNew,
                    iconBg = AppleGreenDark,
                    title = "System Accessibility Service",
                    subtitle = "Required for screen taps & navigation",
                    onClick = {
                        try {
                            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Open Android Settings > Accessibility", Toast.LENGTH_SHORT).show()
                        }
                    },
                    trailing = {
                        Text("Configure", fontSize = 13.sp, color = AppleTheme.colors.accent)
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.SmartScreen,
                    iconBg = AppleTealDark,
                    title = "Screen Understanding",
                    subtitle = "Inspect and reason about UI hierarchy",
                    trailing = {
                        AppleSwitch(
                            checked = isScreenControl,
                            onCheckedChange = { preferences.setScreenControl(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.TouchApp,
                    iconBg = AppleBlueDark,
                    title = "Coordinate Tap (X/Y)",
                    subtitle = "Touch non-standard Canvas or custom elements",
                    trailing = {
                        AppleSwitch(
                            checked = isCoordinateTap,
                            onCheckedChange = { preferences.setCoordinateTap(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.AccessibilityNew,
                    iconBg = AppleIndigoDark,
                    title = "Accessibility Gestures",
                    subtitle = "Scroll, swipe & text field entry",
                    trailing = {
                        AppleSwitch(
                            checked = isAccessibilityControl,
                            onCheckedChange = { preferences.setAccessibilityControl(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.Settings,
                    iconBg = AppleOrangeDark,
                    title = "System Back & Recent Apps",
                    subtitle = "Allow agent to navigate between apps",
                    trailing = {
                        AppleSwitch(
                            checked = isBackNav && isRecentApps,
                            onCheckedChange = {
                                preferences.setBackNav(it)
                                preferences.setRecentApps(it)
                            }
                        )
                    }
                )
            }

            // ==========================================
            // 4. LIVE VOICE & BACKGROUND
            // ==========================================
            AppleSettingsGroup(title = "LIVE VOICE & BACKGROUND") {
                AppleSettingsRow(
                    icon = Icons.Default.Mic,
                    iconBg = AppleTealDark,
                    title = "Floating Live Corner",
                    subtitle = "Small status overlay while using other apps",
                    trailing = {
                        AppleSwitch(
                            checked = isLiveCorner,
                            onCheckedChange = { preferences.setLiveCornerEnabled(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.BatteryChargingFull,
                    iconBg = AppleGreenDark,
                    title = "Background Execution",
                    subtitle = "Keep voice agent listening while minimized",
                    trailing = {
                        AppleSwitch(
                            checked = isBackgroundExec,
                            onCheckedChange = { preferences.setBackgroundExecution(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.RecordVoiceOver,
                    iconBg = ApplePurpleDark,
                    title = "Wake Word: $wakeWord",
                    subtitle = "Triggers voice task immediately",
                    trailing = {
                        Text("Configured", fontSize = 13.sp, color = AppleTheme.colors.textMuted)
                    }
                )
            }

            // ==========================================
            // 5. COMMUNICATION & SOCIAL
            // ==========================================
            AppleSettingsGroup(title = "COMMUNICATION & SOCIAL") {
                AppleSettingsRow(
                    icon = Icons.AutoMirrored.Filled.Chat,
                    iconBg = AppleGreenDark,
                    title = "WhatsApp Assistant",
                    subtitle = "Auto-reply & group summarizer",
                    trailing = {
                        AppleSwitch(
                            checked = preferences.isWhatsAppAutoReplyEnabled.collectAsState().value,
                            onCheckedChange = { preferences.setWhatsAppAutoReply(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.TouchApp,
                    iconBg = ApplePinkDark,
                    title = "Reels Auto-Scroll",
                    subtitle = "Hands-free video scrolling",
                    trailing = {
                        AppleSwitch(
                            checked = isReelsAutoScroll,
                            onCheckedChange = { preferences.setReelsAutoScroll(it) }
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.Email,
                    iconBg = AppleBlueDark,
                    title = "Email / SMTP Connector",
                    subtitle = "Draft and send emails via voice",
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AppleTheme.colors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            // ==========================================
            // 6. SAFETY & PRIVACY
            // ==========================================
            AppleSettingsGroup(title = "SAFETY & PRIVACY") {
                AppleSettingsRow(
                    icon = Icons.Default.Security,
                    iconBg = AppleRedDark,
                    title = "Voice Guardian",
                    subtitle = "Execute only when user voice matches profile",
                    trailing = {
                        AppleSwitch(
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                )

                AppleGroupDivider()

                AppleSettingsRow(
                    icon = Icons.Default.Security,
                    iconBg = AppleOrangeDark,
                    title = "Touch Guard",
                    subtitle = "Confirms sensitive calls, transfers & deletions",
                    trailing = {
                        AppleSwitch(
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                )
            }

            // ==========================================
            // 7. SYSTEM & ABOUT
            // ==========================================
            AppleSettingsGroup(title = "ABOUT") {
                AppleSettingsRow(
                    icon = Icons.Default.SystemUpdate,
                    iconBg = AppleIndigoDark,
                    title = "Maya AI Version",
                    subtitle = "v1.0.0 Production Release",
                    trailing = {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Maya AI is up to date.", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppleTheme.colors.surfaceSecondary,
                                contentColor = AppleTheme.colors.accent
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text("Check", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Apple Settings Grouped Box (Rounded 20dp container).
 */
@Composable
private fun AppleSettingsGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                fontSize = 12.sp
            ),
            color = AppleTheme.colors.textMuted,
            modifier = Modifier.padding(start = 14.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppleTheme.colors.surface)
                .border(0.5.dp, AppleTheme.colors.cardBorder, RoundedCornerShape(20.dp))
        ) {
            Column(content = content)
        }
    }
}

/**
 * Standard Apple Settings Row item.
 */
@Composable
private fun AppleSettingsRow(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.appleBounceClick(pressedScale = 0.98f, onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Squircle Icon
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                color = AppleTheme.colors.textPrimary
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = AppleTheme.colors.textMuted,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

/**
 * Indented divider matching Apple Settings separator line.
 */
@Composable
private fun AppleGroupDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 58.dp),
        thickness = 0.5.dp,
        color = AppleTheme.colors.border
    )
}

/**
 * Apple-style green native toggle switch.
 */
@Composable
private fun AppleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = AppleGreenDark,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = AppleTheme.colors.surfaceSecondary,
            uncheckedBorderColor = Color.Transparent
        )
    )
}

/**
 * Segment chip for Theme selection.
 */
@Composable
private fun AppleThemeSegment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppleTheme.colors.surface else Color.Transparent,
        label = "thm_bg"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) AppleTheme.colors.textPrimary else AppleTheme.colors.textMuted
        )
    }
}
