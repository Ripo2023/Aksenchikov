package com.example.myapplication.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.myapplication.theme.models.Gradient
import com.example.myapplication.theme.models.ThemeColors

object ThemeType {

    val white : Theme = object : Theme {
        override val colors: ThemeColors = ThemeColors(
            background = Color.White,
            primary = Color(0xFFFF5712),
            primaryFontColor = Color(0xFF000000),
            secondaryFontColor = Color(0xFF7A7A7A)
        )
        override val gradient: Gradient = Gradient(
            splashGradient = Brush.horizontalGradient(
                listOf(Color(0xFFFECCC8),Color(0xFFFECCE6))
            )
        )

    }
}