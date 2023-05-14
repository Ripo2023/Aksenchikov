package com.example.myapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.AuthScreen.AuthScreen
import com.example.myapplication.presentation.MainScreen.MainScreen
import com.example.myapplication.presentation.OnboardScreen.OnboardScreen
import com.example.myapplication.presentation.SplashScreen.SplashScreen
import com.example.myapplication.theme.setContentWithTheme
import com.example.myapplication.themeColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentWithTheme() {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination =  Screen.SplashScreen.route,
                modifier = Modifier.background(themeColors.background)
            ) {

                composable(Screen.SplashScreen.route) {
                    SplashScreen(navController = navController)
                }

                composable(Screen.OnboardScreen.route) {
                    OnboardScreen(navController)
                }

                composable(Screen.AuthScreen.route) {
                    AuthScreen(navController = navController, activity = this@MainActivity)
                }

                composable(Screen.MainScreen.route) {
                    MainScreen()
                }
            }
        }
    }
}