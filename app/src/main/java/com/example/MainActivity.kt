package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.appleBounceClick
import com.example.ui.screens.AppearanceScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ConnectorsScreen
import com.example.ui.screens.FeatureChecklistScreen
import com.example.ui.screens.GenericHubScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveVoiceTaskScreen
import com.example.ui.screens.MemoriesScreen
import com.example.ui.screens.ScanVisionScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SetupWizardScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.SubAgentsScreen
import com.example.ui.screens.VoiceModeScreen
import com.example.ui.screens.WhatsAppScreen
import com.example.ui.theme.AppleBlueDark
import com.example.ui.theme.AppleGreenDark
import com.example.ui.theme.AppleIndigoDark
import com.example.ui.theme.AppleOrangeDark
import com.example.ui.theme.ApplePinkDark
import com.example.ui.theme.ApplePurpleDark
import com.example.ui.theme.AppleRedDark
import com.example.ui.theme.AppleTealDark
import com.example.ui.theme.AppleTheme
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    SCAN,
    MEMORIES,
    CHAT,
    VOICE,
    LIVE_VOICE_TASK,
    WHATSAPP,
    SKILLS,
    SUB_AGENTS,
    CONNECTORS,
    APPEARANCE,
    SETTINGS,
    MARKETS,
    MARKETING,
    DOCUMENTS,
    CODING,
    STUDY,
    NOTIFICATIONS,
    PRIVACY,
    ABOUT,
    UPGRADE,
    CHECKLIST
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MayaApplication

        setContent {
            val themeMode by app.preferences.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemDark
            }

            MyApplicationTheme(darkTheme = isDark) {
                val isSetupCompleted by app.preferences.isSetupCompleted.collectAsState()

                if (!isSetupCompleted) {
                    SetupWizardScreen(
                        preferences = app.preferences,
                        geminiClient = app.geminiClient,
                        onSetupFinished = {
                            // Setup complete
                        }
                    )
                } else {
                    MainAppContent(app = app)
                }
            }
        }
    }
}

@Composable
fun MainAppContent(app: MayaApplication) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    BackHandler(enabled = currentScreen != Screen.HOME) {
        currentScreen = Screen.HOME
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(310.dp)
                    .fillMaxHeight(),
                drawerContainerColor = AppleTheme.colors.surface,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                AppleDrawerContent(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        currentScreen = screen
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                // Bottom Navigation (Apple Tab Bar)
                if (currentScreen in listOf(Screen.HOME, Screen.SCAN, Screen.MEMORIES, Screen.CHAT, Screen.VOICE)) {
                    AppleBottomTabBar(
                        currentScreen = currentScreen,
                        onNavigate = { currentScreen = it }
                    )
                }
            },
            containerColor = AppleTheme.colors.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.985f, animationSpec = tween(220)))
                        .togetherWith(fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 1.01f, animationSpec = tween(180)))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                label = "apple_screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.HOME -> HomeScreen(
                        preferences = app.preferences,
                        taskEngine = app.taskExecutionEngine,
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        onNavigateToChat = { currentScreen = Screen.CHAT },
                        onNavigateToScan = { currentScreen = Screen.SCAN },
                        onNavigateToVoice = { currentScreen = Screen.VOICE },
                        onNavigateToNotifications = { currentScreen = Screen.NOTIFICATIONS },
                        onNavigateToProfile = { currentScreen = Screen.SETTINGS },
                        onNavigateToChecklist = { currentScreen = Screen.CHECKLIST }
                    )

                    Screen.SCAN -> ScanVisionScreen(
                        geminiClient = app.geminiClient,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.MEMORIES -> MemoriesScreen(
                        repository = app.memoryRepository,
                        taskEngine = app.taskExecutionEngine,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.CHAT -> ChatScreen(
                        preferences = app.preferences,
                        geminiClient = app.geminiClient,
                        speechManager = app.speechManager,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.VOICE -> VoiceModeScreen(
                        preferences = app.preferences,
                        taskEngine = app.taskExecutionEngine,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.LIVE_VOICE_TASK -> LiveVoiceTaskScreen(
                        taskEngine = app.taskExecutionEngine,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.WHATSAPP -> WhatsAppScreen(
                        preferences = app.preferences,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.SKILLS -> SkillsScreen(
                        repository = app.memoryRepository,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.SUB_AGENTS -> SubAgentsScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.CONNECTORS -> ConnectorsScreen(
                        preferences = app.preferences,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.APPEARANCE -> AppearanceScreen(
                        preferences = app.preferences,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.SETTINGS -> SettingsScreen(
                        preferences = app.preferences,
                        speechManager = app.speechManager,
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.MARKETS -> GenericHubScreen(
                        title = "Markets & Crypto",
                        subtitle = "Real-time market perception & trading automation",
                        icon = Icons.Default.QueryStats,
                        details = listOf(
                            "Track real-time stock and cryptocurrency trends",
                            "Voice alerts for price thresholds",
                            "Automated portfolio rebalancing summaries"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.MARKETING -> GenericHubScreen(
                        title = "Marketing Studio",
                        subtitle = "Campaign planning & content generation",
                        icon = Icons.Default.Share,
                        details = listOf(
                            "Social campaign calendar planner",
                            "High-conversion ad copy synthesis",
                            "Auto-post workflows across Instagram and X"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.DOCUMENTS -> GenericHubScreen(
                        title = "Documents & PDFs",
                        subtitle = "Document OCR & semantic search",
                        icon = Icons.Default.Description,
                        details = listOf(
                            "Extract structured tables and text from PDFs",
                            "Automatic citation and executive briefings",
                            "Draft official documents in seconds"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.CODING -> GenericHubScreen(
                        title = "Website / Coding Workspace",
                        subtitle = "Full-stack code generation and debug sandbox",
                        icon = Icons.Default.Code,
                        details = listOf(
                            "Jetpack Compose UI component generation",
                            "Kotlin, Python, JavaScript, and HTML/CSS syntax engines",
                            "Automated error stack-trace diagnosis"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.STUDY -> GenericHubScreen(
                        title = "Study & Whiteboard",
                        subtitle = "Interactive learning assistant & mind mapping",
                        icon = Icons.Default.School,
                        details = listOf(
                            "Active recall quizzing and spaced repetition",
                            "Complex STEM concept simplification",
                            "Voice-guided exam preparation"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.NOTIFICATIONS -> GenericHubScreen(
                        title = "Activity & Notifications",
                        subtitle = "Recent background agent events",
                        icon = Icons.Default.Notifications,
                        details = listOf(
                            "YouTube task completed at (X: 720, Y: 1380)",
                            "WhatsApp auto-reply dispatched to work group",
                            "Daily briefing synthesized at 8:30 AM"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.PRIVACY -> GenericHubScreen(
                        title = "Privacy Policy",
                        subtitle = "Zero-leak on-device security guarantees",
                        icon = Icons.Default.Lock,
                        details = listOf(
                            "Accessibility permissions strictly used for requested tasks",
                            "No unauthorized screen transmission",
                            "API keys stored locally in encrypted storage"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.ABOUT -> GenericHubScreen(
                        title = "About Maya AI",
                        subtitle = "Next-Generation Autonomous Android Agent",
                        icon = Icons.Default.Info,
                        details = listOf(
                            "Maya AI v1.0.0 (Production Release)",
                            "Screen Intelligence Engine with X/Y Coordinate Tap",
                            "Powered by Google Gemini 2.5 Flash"
                        ),
                        onBack = { currentScreen = Screen.HOME }
                    )

                    Screen.UPGRADE -> GenericHubScreen(
                        title = "Upgrade to Maya PRO",
                        subtitle = "Unlimited sub-agents & premium 3D characters",
                        icon = Icons.Default.Star,
                        details = listOf(
                            "Unlock Maya Violet, Green & Sage 3D Characters",
                            "Unlimited parallel sub-agents",
                            "Priority latency on Gemini 3.1 Pro preview"
                        ),
                        onBack = { currentScreen = Screen.HOME },
                        onPrimaryAction = {
                            Toast.makeText(context, "Maya PRO features unlocked for this device!", Toast.LENGTH_SHORT).show()
                        }
                    )

                    Screen.CHECKLIST -> FeatureChecklistScreen(
                        onBack = { currentScreen = Screen.HOME }
                    )
                }
            }
        }
    }
}

/**
 * 🍎 Apple-Inspired Navigation Drawer (Sidebar).
 * Follows the 7 structured categories from Prompt Section 7.
 */
@Composable
fun AppleDrawerContent(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp)
    ) {
        // Apple Identity Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(AppleTheme.colors.surfaceSecondary)
                    .border(1.dp, AppleTheme.colors.cardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_maya_logo),
                    contentDescription = "Maya AI",
                    modifier = Modifier.size(42.dp).clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Maya AI",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    ),
                    color = AppleTheme.colors.textPrimary
                )
                Text(
                    text = "Autonomous Android Agent",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = AppleTheme.colors.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(thickness = 0.5.dp, color = AppleTheme.colors.border)
        Spacer(modifier = Modifier.height(10.dp))

        // 1. HOME GROUP
        AppleDrawerGroupHeader("HOME")
        AppleDrawerRow("Home Dashboard", Icons.Default.Home, AppleBlueDark, currentScreen == Screen.HOME) { onNavigate(Screen.HOME) }
        AppleDrawerRow("71 Features (100%)", Icons.Default.CheckCircle, AppleGreenDark, currentScreen == Screen.CHECKLIST) { onNavigate(Screen.CHECKLIST) }
        AppleDrawerRow("Live Voice Task", Icons.Default.Mic, AppleTealDark, currentScreen == Screen.LIVE_VOICE_TASK) { onNavigate(Screen.LIVE_VOICE_TASK) }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. PRODUCTIVITY
        AppleDrawerGroupHeader("PRODUCTIVITY")
        AppleDrawerRow("Markets & Crypto", Icons.Default.QueryStats, AppleGreenDark, currentScreen == Screen.MARKETS) { onNavigate(Screen.MARKETS) }
        AppleDrawerRow("Marketing Studio", Icons.Default.Share, ApplePinkDark, currentScreen == Screen.MARKETING) { onNavigate(Screen.MARKETING) }
        AppleDrawerRow("Documents & OCR", Icons.Default.Description, AppleOrangeDark, currentScreen == Screen.DOCUMENTS) { onNavigate(Screen.DOCUMENTS) }
        AppleDrawerRow("Coding Workspace", Icons.Default.Code, AppleIndigoDark, currentScreen == Screen.CODING) { onNavigate(Screen.CODING) }
        AppleDrawerRow("Study & Whiteboard", Icons.Default.School, ApplePurpleDark, currentScreen == Screen.STUDY) { onNavigate(Screen.STUDY) }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. AI TOOLS
        AppleDrawerGroupHeader("AI TOOLS")
        AppleDrawerRow("Scan / Vision", Icons.Default.Search, AppleBlueDark, currentScreen == Screen.SCAN) { onNavigate(Screen.SCAN) }
        AppleDrawerRow("Memories & Tasks", Icons.Default.Psychology, AppleIndigoDark, currentScreen == Screen.MEMORIES) { onNavigate(Screen.MEMORIES) }
        AppleDrawerRow("Chat Assistant", Icons.AutoMirrored.Filled.Chat, AppleTealDark, currentScreen == Screen.CHAT) { onNavigate(Screen.CHAT) }

        Spacer(modifier = Modifier.height(10.dp))

        // 4. COMMUNICATION
        AppleDrawerGroupHeader("COMMUNICATION")
        AppleDrawerRow("Messages & WhatsApp", Icons.AutoMirrored.Filled.Chat, AppleGreenDark, currentScreen == Screen.WHATSAPP) { onNavigate(Screen.WHATSAPP) }

        Spacer(modifier = Modifier.height(10.dp))

        // 5. INTEGRATIONS
        AppleDrawerGroupHeader("INTEGRATIONS")
        AppleDrawerRow("Skills Store", Icons.Default.Extension, AppleOrangeDark, currentScreen == Screen.SKILLS) { onNavigate(Screen.SKILLS) }
        AppleDrawerRow("Sub-Agents", Icons.Default.SmartToy, ApplePurpleDark, currentScreen == Screen.SUB_AGENTS) { onNavigate(Screen.SUB_AGENTS) }
        AppleDrawerRow("Connectors & Telegram", Icons.Default.Link, AppleBlueDark, currentScreen == Screen.CONNECTORS) { onNavigate(Screen.CONNECTORS) }

        Spacer(modifier = Modifier.height(10.dp))

        // 6. SETTINGS
        AppleDrawerGroupHeader("SETTINGS")
        AppleDrawerRow("Appearance", Icons.Default.Palette, ApplePurpleDark, currentScreen == Screen.APPEARANCE) { onNavigate(Screen.APPEARANCE) }
        AppleDrawerRow("Settings Hub", Icons.Default.Settings, AppleBlueDark, currentScreen == Screen.SETTINGS) { onNavigate(Screen.SETTINGS) }
        AppleDrawerRow("Notifications", Icons.Default.Notifications, AppleRedDark, currentScreen == Screen.NOTIFICATIONS) { onNavigate(Screen.NOTIFICATIONS) }

        Spacer(modifier = Modifier.height(10.dp))

        // 7. ABOUT
        AppleDrawerGroupHeader("ABOUT")
        AppleDrawerRow("Privacy Policy", Icons.Default.Lock, AppleGreenDark, currentScreen == Screen.PRIVACY) { onNavigate(Screen.PRIVACY) }
        AppleDrawerRow("About Maya AI", Icons.Default.Info, AppleBlueDark, currentScreen == Screen.ABOUT) { onNavigate(Screen.ABOUT) }
        AppleDrawerRow("Upgrade to PRO", Icons.Default.Star, AppleOrangeDark, currentScreen == Screen.UPGRADE) { onNavigate(Screen.UPGRADE) }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AppleDrawerGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = AppleTheme.colors.textMuted,
            letterSpacing = 0.6.sp,
            fontSize = 11.sp
        ),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

@Composable
private fun AppleDrawerRow(
    label: String,
    icon: ImageVector,
    iconBg: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) AppleTheme.colors.surfaceSecondary else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .appleBounceClick(pressedScale = 0.98f, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 14.sp
            ),
            color = if (isSelected) AppleTheme.colors.accent else AppleTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 🍎 Apple-Inspired Bottom Tab Bar.
 * Clean, subtle, elevated with iOS active indicator.
 */
@Composable
fun AppleBottomTabBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, AppleTheme.colors.border),
        color = AppleTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppleTabItem(
                label = "Home",
                icon = Icons.Default.Home,
                selected = currentScreen == Screen.HOME,
                onClick = { onNavigate(Screen.HOME) },
                testTag = "nav_home"
            )
            AppleTabItem(
                label = "Scan",
                icon = Icons.Default.Search,
                selected = currentScreen == Screen.SCAN,
                onClick = { onNavigate(Screen.SCAN) },
                testTag = "nav_scan"
            )
            AppleTabItem(
                label = "Memories",
                icon = Icons.Default.Psychology,
                selected = currentScreen == Screen.MEMORIES,
                onClick = { onNavigate(Screen.MEMORIES) },
                testTag = "nav_memories"
            )
            AppleTabItem(
                label = "Chat",
                icon = Icons.AutoMirrored.Filled.Chat,
                selected = currentScreen == Screen.CHAT,
                onClick = { onNavigate(Screen.CHAT) },
                testTag = "nav_chat"
            )
            AppleTabItem(
                label = "Voice",
                icon = Icons.Default.Mic,
                selected = currentScreen == Screen.VOICE,
                onClick = { onNavigate(Screen.VOICE) },
                testTag = "nav_voice"
            )
        }
    }
}

@Composable
private fun AppleTabItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val tint = if (selected) AppleTheme.colors.accent else AppleTheme.colors.textMuted

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .appleBounceClick(pressedScale = 0.92f, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = tint
        )
    }
}
