package com.example.myapplicationnew.presentation.OrderScreen.models

sealed class OrderItemsDialogState {

    object Hide : OrderItemsDialogState()

    data class Show(val order:OrderViewModel) : OrderItemsDialogState()
}