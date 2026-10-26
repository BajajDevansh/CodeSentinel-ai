package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = DeepNavy,
    primaryContainer = Color(0xFF004D56),
    onPrimaryContainer = Color(0xFF9CF3FF),
    secondary = ElectricBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF00368C),
    onSecondaryContainer = Color(0xFFD6E3FF),
    tertiary = CodeGreen,
    onTertiary = DeepNavy,
    error = CriticalRed,
    onError = Color.White,
    background = DeepNavy,
    onBackground = TextPrimaryDark,
    surface = DarkNavyCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkNavySurface,
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007A8A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCEEF5),
    onPrimaryContainer = Color(0xFF001F24),
    secondary = Color(0xFF1D58D8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDAE2FF),
    onSecondaryContainer = Color(0xFF001946),
    tertiary = Color(0xFF008744),
    onTertiary = Color.White,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    background = LightSurface,
    onBackground = TextPrimaryLight,
    surface = LightCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize our signature developer cyber aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
