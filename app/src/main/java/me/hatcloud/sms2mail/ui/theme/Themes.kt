package me.hatcloud.sms2mail.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightThemeColors = lightColors(
    primary = COLOR_3F51B5,
    primaryVariant = COLOR_FF4081,
    onPrimary = Color.White,
    secondary = COLOR_303F9F,
    secondaryVariant = Red200,
    onSecondary = Color.White,
    error = Red800
)

val DarkThemeColors = darkColors(
    primary = COLOR_303F9F,
    primaryVariant = COLOR_FF4081,
    onPrimary = Color.Black,
    secondary = Red300,
    onSecondary = Color.Black,
    error = Red200
)

@Composable
fun Sms2MailColors(darkTheme: Boolean = isSystemInDarkTheme()) = if (darkTheme) DarkThemeColors else LightThemeColors

@Composable
fun Sms2MailTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = Sms2MailColors(darkTheme = darkTheme),
        typography = Sms2MailTypography,
        shapes = Sms2MailShapes,
        content = content
    )
}