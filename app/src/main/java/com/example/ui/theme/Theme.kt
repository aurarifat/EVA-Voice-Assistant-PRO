package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = Color.Black,
    primaryContainer = CyberDark700,
    onPrimaryContainer = CyanNeon,
    secondary = VioletNeon,
    onSecondary = Color.White,
    secondaryContainer = CyberDark600,
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = EmeraldNeon,
    onTertiary = Color.Black,
    background = CyberDark900,
    onBackground = TextPrimary,
    surface = CyberDark800,
    onSurface = TextPrimary,
    surfaceVariant = CyberDark700,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B),
    error = RoseNeon,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
