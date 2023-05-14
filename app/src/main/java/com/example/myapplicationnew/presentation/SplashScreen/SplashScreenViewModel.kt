package com.example.myapplicationnew.presentation.SplashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplicationnew.domain.AuthManager
import com.example.myapplicationnew.domain.OnboardScreenManager
import com.example.myapplicationnew.presentation.Screen
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
    private val onboardScreenManager: OnboardScreenManager,
    private val authManager: AuthManager
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
                    val nextScreen = if(authManager.currentUser != null) Screen.MainScreen.route
                        else Screen.AuthScreen.route

                        navController.navigate(nextScreen) {
                        launchSingleTop = true

                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }
}