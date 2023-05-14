package com.example.myapplication.theme

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController

fun ComponentActivity.setContentWithTheme(
    content:@Composable () -> Unit
) {
    setContent {
        val theme = if(isSystemInDarkTheme()) ThemeType.dark else ThemeType.white
        val colors = theme.colors

        val systemUiController = rememberSystemUiController()
        systemUiController.setNavigationBarColor(colors.background)
        systemUiController.setSystemBarsColor(colors.background)

        CompositionLocalProvider(
            LocalTheme provides theme,
            content = content
        )
    }

}