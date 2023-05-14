package com.example.myapplication.presentation.SplashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.domain.OnboardScreenManager
import com.example.myapplication.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//ViewModel Splash экрана
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val onboardScreenManager: OnboardScreenManager
) : ViewModel() {


    fun onSplash(navController: NavController) {
        viewModelScope.launch(Dispatchers.Default) {
            delay(1000)
            val isNeedShowOnboardScreen = onboardScreenManager.isNeedShowOnboard.first()
            withContext(Dispatchers.Main) {

                if(isNeedShowOnboardScreen) {
                    navController.navigate(Screen.OnboardScreen.route) {
                        launchSingleTop = true

                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.AuthScreen.route) {
                        launchSingleTop = true

                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }
}