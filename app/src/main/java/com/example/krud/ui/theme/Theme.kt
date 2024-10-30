package com.example.krud.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = TextColorLight
)

private val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextColorDark,
    surface = TextFieldDarkBackground,
    onSurface = TextFieldLightText
)

@Composable
fun KrudTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
