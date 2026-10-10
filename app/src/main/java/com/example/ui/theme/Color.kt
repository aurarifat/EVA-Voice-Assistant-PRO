package com.example.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ==========================================
// 🍎 APPLE DESIGN SYSTEM - COLOR PALETTE
// ==========================================

// iOS System Accent Colors (Light & Dark friendly)
val AppleBlue = Color(0xFF007AFF)
val AppleBlueDark = Color(0xFF0A84FF)
val ApplePurple = Color(0xFFAF52DE)
val ApplePurpleDark = Color(0xFFBF5AF2)
val AppleGreen = Color(0xFF34C759)
val AppleGreenDark = Color(0xFF30D158)
val AppleRed = Color(0xFFFF3B30)
val AppleRedDark = Color(0xFFFF453A)
val AppleOrange = Color(0xFFFF9500)
val AppleOrangeDark = Color(0xFFFF9F0A)
val AppleTeal = Color(0xFF30B0C7)
val AppleTealDark = Color(0xFF40C8E0)
val AppleIndigo = Color(0xFF5856D6)
val AppleIndigoDark = Color(0xFF5E5CE6)
val ApplePink = Color(0xFFFF2D55)
val ApplePinkDark = Color(0xFFFF375F)
val AppleYellow = Color(0xFFFFCC00)
val AppleYellowDark = Color(0xFFFFD60A)

// Apple Neutral Grayscale - Light Mode
val AppleLightBg = Color(0xFFF2F2F7)             // System Grouped Background
val AppleLightSurface = Color(0xFFFFFFFF)        // Secondary Grouped Background (Pure White)
val AppleLightSurfaceSecondary = Color(0xFFE5E5EA) // Tertiary Grouped Background
val AppleLightSurfaceTertiary = Color(0xFFF8F9FA)
val AppleLightTextPrimary = Color(0xFF000000)    // Label
val AppleLightTextSecondary = Color(0xFF3C3C43)  // Secondary Label
val AppleLightTextMuted = Color(0xFF8E8E93)      // Tertiary Label
val AppleLightBorder = Color(0x1F000000)         // Hairline separator (12% black)
val AppleLightCardBorder = Color(0x14000000)     // Subtle border (8% black)

// Apple Neutral Grayscale - Dark Mode
val AppleDarkBg = Color(0xFF000000)              // Pure OLED / Deep Black
val AppleDarkSurface = Color(0xFF1C1C1E)         // Secondary Grouped Background (Elevated Dark Gray)
val AppleDarkSurfaceSecondary = Color(0xFF2C2C2E)// Tertiary Grouped Background
val AppleDarkSurfaceTertiary = Color(0xFF3A3A3C) // Quaternary Grouped Background
val AppleDarkTextPrimary = Color(0xFFFFFFFF)     // Label (White)
val AppleDarkTextSecondary = Color(0xFFEBEBF5)   // Secondary Label (Muted White)
val AppleDarkTextMuted = Color(0xFF8E8E93)       // Tertiary Label (Muted Gray)
val AppleDarkBorder = Color(0x38545458)          // Hairline separator (22% white)
val AppleDarkCardBorder = Color(0x26FFFFFF)      // Subtle border (15% white)

// ==========================================
// BACKWARD COMPATIBLE & ELEVATED TOKENS
// ==========================================
val CyanNeon = AppleBlueDark
val VioletNeon = ApplePurpleDark
val EmeraldNeon = AppleGreenDark
val RoseNeon = AppleRedDark
val AmberNeon = AppleOrangeDark

val CyberDark900 = AppleDarkBg
val CyberDark800 = AppleDarkSurface
val CyberDark700 = AppleDarkSurfaceSecondary
val CyberDark600 = AppleDarkSurfaceTertiary

val TextPrimary = AppleDarkTextPrimary
val TextSecondary = AppleDarkTextSecondary
val TextMuted = AppleDarkTextMuted

val CyanNeonGlow = Color(0x330A84FF)
val VioletNeonGlow = Color(0x33BF5AF2)

// ==========================================
// DYNAMIC APPLE COLOR PALETTE HOLDER
// ==========================================
data class ApplePalette(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val surfaceTertiary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val cardBorder: Color,
    val accent: Color,
    val accentSecondary: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val textTertiary: Color = textMuted,
    val divider: Color = border
)

val AppleLightPalette = ApplePalette(
    isDark = false,
    background = AppleLightBg,
    surface = AppleLightSurface,
    surfaceSecondary = AppleLightSurfaceSecondary,
    surfaceTertiary = AppleLightSurfaceTertiary,
    textPrimary = AppleLightTextPrimary,
    textSecondary = AppleLightTextSecondary,
    textMuted = AppleLightTextMuted,
    border = AppleLightBorder,
    cardBorder = AppleLightCardBorder,
    accent = AppleBlue,
    accentSecondary = ApplePurple,
    success = AppleGreen,
    warning = AppleOrange,
    error = AppleRed
)

val AppleDarkPalette = ApplePalette(
    isDark = true,
    background = AppleDarkBg,
    surface = AppleDarkSurface,
    surfaceSecondary = AppleDarkSurfaceSecondary,
    surfaceTertiary = AppleDarkSurfaceTertiary,
    textPrimary = AppleDarkTextPrimary,
    textSecondary = AppleDarkTextSecondary,
    textMuted = AppleDarkTextMuted,
    border = AppleDarkBorder,
    cardBorder = AppleDarkCardBorder,
    accent = AppleBlueDark,
    accentSecondary = ApplePurpleDark,
    success = AppleGreenDark,
    warning = AppleOrangeDark,
    error = AppleRedDark
)

val LocalAppleColors = staticCompositionLocalOf { AppleDarkPalette }
