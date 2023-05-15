package com.example.myapplicationnew.presentation.OrderScreen.models

import com.example.myapplicationnew.domain.ProductRepository

sealed class OrderItemsDialogState {

    object Hide : OrderItemsDialogState()

    data class Show(val order:OrderViewModel) : OrderItemsDialogState()
}