package com.example.myapplication.presentation.OnboardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.domain.OnboardScreenManager
import com.example.myapplication.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//ViewModel для OnboardScreen
@HiltViewModel
class OnboardScreenViewModel @Inject constructor(
    private val onboardScreenManager: OnboardScreenManager
) : ViewModel() {

    private var isLeaving:Boolean = false

    fun leaveFromScreen(navController: NavController) {
        if(isLeaving) return
        isLeaving = true

        viewModelScope.launch {
            onboardScreenManager.changeState(false)

            withContext(Dispatchers.Main) {
                navController.navigate(Screen.AuthScreen.route) {
                    launchSingleTop = true

                    popUpTo(Screen.OnboardScreen.route) { inclusive = true }
                }
            }
        }
    }
}