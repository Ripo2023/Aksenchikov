package com.example.myapplication.presentation.MainScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.Screen

@Composable
fun MainScreen(
    mainScreenViewModel:MainScreenViewModel = hiltViewModel()
) {
    Text(text = "Hi")
}