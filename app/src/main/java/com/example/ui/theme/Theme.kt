package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val AppleDarkColorScheme = darkColorScheme(
    primary = AppleBlueDark,
    onPrimary = Color.White,
    primaryContainer = AppleDarkSurfaceSecondary,
    onPrimaryContainer = AppleBlueDark,
    secondary = ApplePurpleDark,
    onSecondary = Color.White,
    secondaryContainer = AppleDarkSurfaceTertiary,
    onSecondaryContainer = ApplePurpleDark,
    tertiary = AppleGreenDark,
    onTertiary = Color.Black,
    background = AppleDarkBg,
    onBackground = AppleDarkTextPrimary,
    surface = AppleDarkSurface,
    onSurface = AppleDarkTextPrimary,
    surfaceVariant = AppleDarkSurfaceSecondary,
    onSurfaceVariant = AppleDarkTextSecondary,
    outline = AppleDarkBorder,
    outlineVariant = AppleDarkCardBorder,
    error = AppleRedDark,
    onError = Color.White
)

private val AppleLightColorScheme = lightColorScheme(
    primary = AppleBlue,
    onPrimary = Color.White,
    primaryContainer = AppleLightSurfaceSecondary,
    onPrimaryContainer = AppleBlue,
    secondary = ApplePurple,
    onSecondary = Color.White,
    secondaryContainer = AppleLightSurfaceTertiary,
    onSecondaryContainer = ApplePurple,
    tertiary = AppleGreen,
    onTertiary = Color.White,
    background = AppleLightBg,
    onBackground = AppleLightTextPrimary,
    surface = AppleLightSurface,
    onSurface = AppleLightTextPrimary,
    surfaceVariant = AppleLightSurfaceSecondary,
    onSurfaceVariant = AppleLightTextSecondary,
    outline = AppleLightBorder,
    outlineVariant = AppleLightCardBorder,
    error = AppleRed,
    onError = Color.White
)

object AppleTheme {
    val colors: ApplePalette
        @Composable
        @ReadOnlyComposable
        get() = LocalAppleColors.current
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val applePalette = if (darkTheme) AppleDarkPalette else AppleLightPalette
    val materialColorScheme = if (darkTheme) AppleDarkColorScheme else AppleLightColorScheme

    CompositionLocalProvider(LocalAppleColors provides applePalette) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}
