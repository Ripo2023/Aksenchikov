package com.example.myapplicationnew.presentation.AuthScreen

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplicationnew.domain.AuthManager
import com.example.myapplicationnew.presentation.AuthScreen.models.AuthScreenState
import com.example.myapplicationnew.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//view model экрана авторизации
@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authManager: AuthManager,
    @ApplicationContext private val context: Context
) : ViewModel() {


    val state = MutableStateFlow<AuthScreenState>(AuthScreenState.EnterPhoneState)

    val isLoading = MutableStateFlow(false)

    private val handler = Handler(Looper.getMainLooper())

    fun sendCode(phone:String,activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            authManager.authRequest(
                phone,
                onCodeSend = {
                    state.update { AuthScreenState.EnterCodeState }
                },
                onError = {
                    handler.post { Toast.makeText(context,"Произошла ошибка",Toast.LENGTH_SHORT).show() }
                },
                activity = activity
            )
            handler.post {
                Toast.makeText(context,"Код для тестового пользователя firebase 111111",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun confirmCode(navController: NavController,code:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authManager.confirmCode(code)

                withContext(Dispatchers.Main) {
                    navController.navigate(Screen.MainScreen.route) {
                        launchSingleTop = true

                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }
            }catch (e:Exception) {
                handler.post { Toast.makeText(context,"Невернный код",Toast.LENGTH_SHORT).show() }
            }


        }
    }

    fun backToPhoneEnter() {
        state.update { AuthScreenState.EnterPhoneState }
    }

    init {
        handler.post {
            Toast.makeText(context,"Телефоны для тестового пользователя firebase +375336441254...+375336441256",Toast.LENGTH_LONG).show()
        }
    }
}