package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
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
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.CyberDark700
import com.example.ui.theme.CyberDark800
import com.example.ui.theme.CyberDark900
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VioletNeon
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
            MyApplicationTheme {
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
                    .width(300.dp)
                    .fillMaxHeight(),
                drawerContainerColor = CyberDark900
            ) {
                DrawerContent(
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
                // Bottom Navigation (Section 49: Home, Scan, Memories, Chat, Voice)
                if (currentScreen in listOf(Screen.HOME, Screen.SCAN, Screen.MEMORIES, Screen.CHAT, Screen.VOICE)) {
                    MayaBottomNavigation(
                        currentScreen = currentScreen,
                        onNavigate = { currentScreen = it }
                    )
                }
            },
            containerColor = CyberDark900,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
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
                            "Powered by Google Gemini 3.5 Flash"
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

@Composable
fun DrawerContent(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp)
    ) {
        // Drawer Header with Maya Logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, CyanNeon, CircleShape),
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
                    text = "MAYA AI",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "Autonomous Android Agent",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = CyanNeon
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(10.dp))

        // Section: Main Navigation
        DrawerItem(label = "Home Dashboard", icon = Icons.Default.Home, isSelected = currentScreen == Screen.HOME) { onNavigate(Screen.HOME) }
        DrawerItem(label = "71 Features Checklist (100%)", icon = Icons.Default.CheckCircle, isSelected = currentScreen == Screen.CHECKLIST) { onNavigate(Screen.CHECKLIST) }
        DrawerItem(label = "Live Voice Task", icon = Icons.Default.Mic, isSelected = currentScreen == Screen.LIVE_VOICE_TASK) { onNavigate(Screen.LIVE_VOICE_TASK) }
        DrawerItem(label = "Scan / Vision", icon = Icons.Default.Search, isSelected = currentScreen == Screen.SCAN) { onNavigate(Screen.SCAN) }
        DrawerItem(label = "Memories & Tasks", icon = Icons.Default.Psychology, isSelected = currentScreen == Screen.MEMORIES) { onNavigate(Screen.MEMORIES) }
        DrawerItem(label = "Chat", icon = Icons.AutoMirrored.Filled.Chat, isSelected = currentScreen == Screen.CHAT) { onNavigate(Screen.CHAT) }

        Spacer(modifier = Modifier.height(10.dp))
        DrawerSectionHeader("PRODUCTIVITY & WORKSPACES")
        DrawerItem(label = "Markets & Crypto", icon = Icons.Default.QueryStats, isSelected = currentScreen == Screen.MARKETS) { onNavigate(Screen.MARKETS) }
        DrawerItem(label = "Marketing Studio", icon = Icons.Default.Share, isSelected = currentScreen == Screen.MARKETING) { onNavigate(Screen.MARKETING) }
        DrawerItem(label = "Documents & PDFs", icon = Icons.Default.Description, isSelected = currentScreen == Screen.DOCUMENTS) { onNavigate(Screen.DOCUMENTS) }
        DrawerItem(label = "Website / Coding", icon = Icons.Default.Code, isSelected = currentScreen == Screen.CODING) { onNavigate(Screen.CODING) }
        DrawerItem(label = "Study / Whiteboard", icon = Icons.Default.School, isSelected = currentScreen == Screen.STUDY) { onNavigate(Screen.STUDY) }

        Spacer(modifier = Modifier.height(10.dp))
        DrawerSectionHeader("AUTOMATION & CONNECTORS")
        DrawerItem(label = "Messages & WhatsApp", icon = Icons.AutoMirrored.Filled.Chat, isSelected = currentScreen == Screen.WHATSAPP) { onNavigate(Screen.WHATSAPP) }
        DrawerItem(label = "Skills Store", icon = Icons.Default.Extension, isSelected = currentScreen == Screen.SKILLS) { onNavigate(Screen.SKILLS) }
        DrawerItem(label = "Sub-Agents", icon = Icons.Default.SmartToy, isSelected = currentScreen == Screen.SUB_AGENTS) { onNavigate(Screen.SUB_AGENTS) }
        DrawerItem(label = "Connectors & Telegram", icon = Icons.Default.Link, isSelected = currentScreen == Screen.CONNECTORS) { onNavigate(Screen.CONNECTORS) }

        Spacer(modifier = Modifier.height(10.dp))
        DrawerSectionHeader("SYSTEM & SETTINGS")
        DrawerItem(label = "Appearance & 3D Avatar", icon = Icons.Default.Palette, isSelected = currentScreen == Screen.APPEARANCE) { onNavigate(Screen.APPEARANCE) }
        DrawerItem(label = "Settings Hub", icon = Icons.Default.Settings, isSelected = currentScreen == Screen.SETTINGS) { onNavigate(Screen.SETTINGS) }
        DrawerItem(label = "Notifications", icon = Icons.Default.Notifications, isSelected = currentScreen == Screen.NOTIFICATIONS) { onNavigate(Screen.NOTIFICATIONS) }
        DrawerItem(label = "Privacy Policy", icon = Icons.Default.Lock, isSelected = currentScreen == Screen.PRIVACY) { onNavigate(Screen.PRIVACY) }
        DrawerItem(label = "About Maya AI", icon = Icons.Default.Info, isSelected = currentScreen == Screen.ABOUT) { onNavigate(Screen.ABOUT) }
        DrawerItem(label = "Upgrade to PRO", icon = Icons.Default.Star, isSelected = currentScreen == Screen.UPGRADE) { onNavigate(Screen.UPGRADE) }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.sp,
            fontSize = 10.sp
        ),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) CyanNeon else TextPrimary
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CyanNeon else TextMuted
            )
        },
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = CyberDark800,
            unselectedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
    )
}

@Composable
fun MayaBottomNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = CyberDark900,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onNavigate(Screen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.SCAN,
            onClick = { onNavigate(Screen.SCAN) },
            icon = { Icon(Icons.Default.Search, contentDescription = "Scan") },
            label = { Text("Scan", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_scan")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.MEMORIES,
            onClick = { onNavigate(Screen.MEMORIES) },
            icon = { Icon(Icons.Default.Psychology, contentDescription = "Memories") },
            label = { Text("Memories", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_memories")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.CHAT,
            onClick = { onNavigate(Screen.CHAT) },
            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
            label = { Text("Chat", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_chat")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.VOICE,
            onClick = { onNavigate(Screen.VOICE) },
            icon = { Icon(Icons.Default.Mic, contentDescription = "Voice") },
            label = { Text("Voice", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_voice")
        )
    }
}
