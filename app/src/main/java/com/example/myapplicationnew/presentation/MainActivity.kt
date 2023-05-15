package com.example.myapplicationnew.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationnew.presentation.AuthScreen.AuthScreen
import com.example.myapplicationnew.presentation.MainScreen.MainScreen
import com.example.myapplicationnew.presentation.MapScreen.MapScreen
import com.example.myapplicationnew.presentation.OnboardScreen.OnboardScreen
import com.example.myapplicationnew.presentation.OrderScreen.OrderScreen
import com.example.myapplicationnew.presentation.SplashScreen.SplashScreen
import com.example.myapplicationnew.theme.ThemeType
import com.example.myapplicationnew.theme.setContentWithTheme
import com.example.myapplicationnew.themeColors
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        MapKitFactory.getInstance().onStart();
        setContentWithTheme(
        ) {
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
                    AuthScreen(navController = navController)
                }

                composable(Screen.MainScreen.route) {
                    MainScreen(navController)
                }

                composable(Screen.OrderScreen.route) {
                    OrderScreen()
                }

                composable(Screen.MapScreen.route) {
                    MapScreen()
                }


            }
        }
    }
}