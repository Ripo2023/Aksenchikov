package com.example.myapplicationnew.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.myapplicationnew.theme.models.Gradient
import com.example.myapplicationnew.theme.models.ThemeColors

object ThemeType {

    val white : Theme = object : Theme {
        override val colors: ThemeColors = ThemeColors(
            background = Color.White,
            primary = Color(0xFFFF5712),
            primaryFontColor = Color(0xFF000000),
            secondaryFontColor = Color(0xFF7A7A7A),
            label = Color.White
        )
        override val gradient: Gradient = Gradient(
            splashGradient = Brush.horizontalGradient(
                listOf(Color(0xFFFECCC8),Color(0xFFFECCE6))
            )
        )

    }

    val dark : Theme = object : Theme {
        override val colors: ThemeColors = ThemeColors(
            background = Color.Black,
            primary = Color(0xFFFF5712),
            primaryFontColor = Color(0xFFFFFAFA),
            secondaryFontColor = Color(0xFF7A7A7A),
            label = Color.White
        )
        override val gradient: Gradient = Gradient(
            splashGradient = Brush.horizontalGradient(
                listOf(Color(0xFFFECCC8),Color(0xFFFECCE6))
            )
        )

    }
}