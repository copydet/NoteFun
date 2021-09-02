package com.example.notefun2.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = Orange,
    secondary = Orange,
    onSecondary = Color.White,
    secondaryVariant = Orange,
    surface = DarkBlue,
    onSurface = Color.White,
    background = DarkBlue,
    onBackground = Color.White,
    error = Red800,
    onError = Color.White
)
/*


private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

 */

@Composable
fun NoteFun2Theme(//darkTheme: Boolean = isSystemInDarkTheme(),
                  content: @Composable() () -> Unit) {
    /**
     * code untuk mengubah lightTheme dan darkTheme

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }


    */

    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}