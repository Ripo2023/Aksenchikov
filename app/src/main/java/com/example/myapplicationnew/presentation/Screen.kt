package com.example.myapplicationnew.presentation

sealed class Screen(val route:String) {

    object SplashScreen : Screen("SplashScreen")

    object OnboardScreen : Screen("OnboardScreen")

    object AuthScreen : Screen("AuthScreen")

    object MainScreen : Screen("MainScreen")
}

