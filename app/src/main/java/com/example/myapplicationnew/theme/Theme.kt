package com.example.myapplicationnew.theme

import com.example.myapplicationnew.theme.models.Gradient
import com.example.myapplicationnew.theme.models.ThemeColors

interface Theme {
    val colors: ThemeColors
    val gradient:Gradient
}