package com.maher.focus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CalmLightColorScheme = lightColorScheme(
    primary = Color(0xFF4F6F64),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDEBE5),
    onPrimaryContainer = Color(0xFF14372D),
    secondary = Color(0xFF6B6F57),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE7E8D6),
    background = Color(0xFFF8FAF7),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE9EEE9),
    onBackground = Color(0xFF1D1F1C),
    onSurface = Color(0xFF1D1F1C),
    error = Color(0xFFB3261E)
)

@Composable
fun MaherFocusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CalmLightColorScheme,
        typography = Typography,
        content = content
    )
}
