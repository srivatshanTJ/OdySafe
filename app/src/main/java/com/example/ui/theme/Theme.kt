package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OdysafeLightColorScheme = lightColorScheme(
    primary = OdysafeTeal,
    onPrimary = Color.White,
    primaryContainer = OdysafeMint,
    onPrimaryContainer = OdysafeNavy,
    secondary = OdysafeNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8EEF2),
    onSecondaryContainer = OdysafeNavy,
    tertiary = OdysafeSuccess,
    onTertiary = Color.White,
    tertiaryContainer = OdysafeSuccessContainer,
    onTertiaryContainer = OdysafeSuccess,
    error = OdysafeDanger,
    onError = Color.White,
    errorContainer = OdysafeDangerContainer,
    onErrorContainer = OdysafeDanger,
    background = OdysafeBg,
    onBackground = OdysafeTextPrimary,
    surface = OdysafeSurface,
    onSurface = OdysafeTextPrimary,
    surfaceVariant = Color(0xFFEFF3F5),
    onSurfaceVariant = OdysafeTextSecondary,
    outline = OdysafeBorder,
    outlineVariant = Color(0xFFD4DDE2)
)

private val OdysafeDarkColorScheme = darkColorScheme(
    primary = Color(0xFF4AC2D1),
    onPrimary = OdysafeNavyDark,
    primaryContainer = OdysafeTealDark,
    onPrimaryContainer = OdysafeMint,
    secondary = Color(0xFF86A5BD),
    onSecondary = OdysafeNavyDark,
    secondaryContainer = OdysafeDarkSurfaceVariant,
    onSecondaryContainer = Color(0xFFDFE9F0),
    tertiary = Color(0xFF4CD898),
    onTertiary = Color(0xFF063820),
    error = Color(0xFFFF8B8B),
    onError = Color(0xFF490000),
    background = OdysafeDarkBg,
    onBackground = OdysafeDarkTextPrimary,
    surface = OdysafeDarkSurface,
    onSurface = OdysafeDarkTextPrimary,
    surfaceVariant = OdysafeDarkSurfaceVariant,
    onSurfaceVariant = OdysafeDarkTextSecondary,
    outline = OdysafeDarkBorder,
    outlineVariant = Color(0xFF384C5D)
)

@Composable
fun OdysafeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) OdysafeDarkColorScheme else OdysafeLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
