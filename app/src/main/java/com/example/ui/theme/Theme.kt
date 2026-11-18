package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val YouTubeDarkColorScheme = darkColorScheme(
    primary = YouTubeRed,
    onPrimary = Color.White,
    primaryContainer = YouTubeRedDark,
    onPrimaryContainer = Color.White,
    secondary = VerifiedBlue,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = TextPrimary,
    tertiary = GoldBadge,
    background = OledBlack,
    onBackground = TextPrimary,
    surface = OledBlack,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkSurfaceBorder,
    outlineVariant = Color(0xFF272727)
)

private val YouTubeLightColorScheme = lightColorScheme(
    primary = YouTubeRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE5E5),
    onPrimaryContainer = YouTubeRedDark,
    secondary = VerifiedBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF2F2F2),
    onSecondaryContainer = Color(0xFF0F0F0F),
    tertiary = GoldBadge,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F0F0F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F0F0F),
    surfaceVariant = Color(0xFFF4F4F4),
    onSurfaceVariant = Color(0xFF606060),
    outline = Color(0xFFCCCCCC),
    outlineVariant = Color(0xFFE5E5E5)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) YouTubeDarkColorScheme else YouTubeLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
