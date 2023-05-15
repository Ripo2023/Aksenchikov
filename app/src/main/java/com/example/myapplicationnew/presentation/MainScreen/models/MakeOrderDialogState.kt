package com.example.myapplicationnew.presentation.MainScreen.models

sealed class MakeOrderDialogState {
    object Hided : MakeOrderDialogState()

    data class Showed(val product:ProductModel) : MakeOrderDialogState()
}
