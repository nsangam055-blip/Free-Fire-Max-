package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MintAccent,
    onPrimary = DarkCanvas,
    primaryContainer = DarkCanvas,
    onPrimaryContainer = Color.White,
    secondary = MintBorder,
    background = DarkCanvas,
    surface = DarkCanvas,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2B2E2A),
    onSurfaceVariant = MintBorder,
    outline = MintBorder
)

private val LightColorScheme = lightColorScheme(
    primary = MintAccent,
    onPrimary = DarkCanvas,
    primaryContainer = MintAccent,
    onPrimaryContainer = DarkCanvas,
    secondary = MutedTextLight,
    background = LightCanvas,
    surface = CardWhite,
    onBackground = DarkCanvas,
    onSurface = DarkCanvas,
    surfaceVariant = SubCardBackground,
    onSurfaceVariant = MutedTextLight,
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
