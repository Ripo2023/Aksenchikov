package com.example.myapplication.theme

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTheme  = staticCompositionLocalOf<Theme> {
    error("Theme not provided")
}