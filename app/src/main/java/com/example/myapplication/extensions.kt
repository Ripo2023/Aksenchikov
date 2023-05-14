package com.example.myapplication

import androidx.compose.runtime.Composable
import com.example.myapplication.theme.LocalTheme
import com.example.myapplication.theme.models.Gradient
import com.example.myapplication.theme.models.ThemeColors

val themeColors : ThemeColors
    @Composable get() = LocalTheme.current.colors

val themeGradients : Gradient
    @Composable get() = LocalTheme.current.gradient