package com.example.myapplication.theme

import com.example.myapplication.theme.models.Gradient
import com.example.myapplication.theme.models.ThemeColors

interface Theme {
    val colors: ThemeColors
    val gradient:Gradient
}