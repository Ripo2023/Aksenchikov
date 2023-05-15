package com.example.myapplicationnew

import androidx.compose.runtime.Composable
import com.example.myapplicationnew.theme.LocalTheme
import com.example.myapplicationnew.theme.models.Gradient
import com.example.myapplicationnew.theme.models.ThemeColors

val themeColors : ThemeColors
    @Composable get() = LocalTheme.current.colors

val themeGradients : Gradient
    @Composable get() = LocalTheme.current.gradient


val italianNameByProductId : Map<String,String>
    get() = mapOf(
        "0" to "latte",
        "1" to "kapychino",
        "2" to "ecspresso",
        "3" to "raff",

    )