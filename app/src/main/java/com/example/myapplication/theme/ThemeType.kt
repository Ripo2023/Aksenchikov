package com.example.myapplication.theme

import androidx.compose.ui.graphics.Color
import com.example.myapplication.theme.models.ThemeColors

object ThemeType {

    val white : Theme = object : Theme {
        override val colors: ThemeColors = ThemeColors(
            background = Color.White
        )

    }

    val dark : Theme = object : Theme {
        override val colors: ThemeColors = ThemeColors(
            background = Color.Black
        )

    }
}