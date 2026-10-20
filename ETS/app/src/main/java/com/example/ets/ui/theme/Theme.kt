package com.example.ets.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueContainer,
    onPrimaryContainer = OnPrimaryBlueContainer,
    secondary = AccentOrange,
    onSecondary = Color.White,
    secondaryContainer = AccentOrangeContainer,
    onSecondaryContainer = OnAccentOrangeContainer,
    tertiary = PrimaryBlueLight,
    onTertiary = Color.White,
    background = BackgroundLight, // Menggunakan abu-abu sangat muda (hampir putih) agar card lebih pop out
    onBackground = TextPrimary,
    surface = SurfaceLight, // Putih murni
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9), // Card dan komponen lebih soft
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8BBAE5),
    onPrimary = Color(0xFF003258),
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueContainer,
    secondary = Color(0xFFFFB59B),
    onSecondary = Color(0xFF5A1D00),
    background = Color(0xFF101418),
    onBackground = Color(0xFFE0E2E8),
    surface = Color(0xFF15191F),
    onSurface = Color(0xFFE0E2E8),
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF9CA3AF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

val LibraryShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun ETSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = LibraryShapes,
        content = content
    )
}