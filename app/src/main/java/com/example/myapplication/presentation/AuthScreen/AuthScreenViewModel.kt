package com.example.myapplication.presentation.AuthScreen

import androidx.lifecycle.ViewModel
import com.example.myapplication.presentation.AuthScreen.models.AuthScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

//view model экрана авторизации
@HiltViewModel
class AuthScreenViewModel @Inject constructor() : ViewModel() {


    val state = MutableStateFlow<AuthScreenState>(AuthScreenState.EnterPhoneState)


    fun sendCode(phone:String) {
        state.update { AuthScreenState.EnterCodeState }
    }

    fun backToPhoneEnter() {
        state.update { AuthScreenState.EnterPhoneState }
    }
}