package com.example.myapplication.presentation.AuthScreen.models

//Состояние экрана
sealed class AuthScreenState {

    object EnterPhoneState : AuthScreenState()

    object EnterCodeState : AuthScreenState()
}
