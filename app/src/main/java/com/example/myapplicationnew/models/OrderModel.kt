package com.example.myapplicationnew.models

import kotlinx.serialization.Serializable
import java.util.UUID

//Модель заказа
@Serializable
data class OrderModel(
    val id:String = UUID.randomUUID().toString(),
    val productId:String,
    val volumeCof:Float,
    val isReady:Boolean = false
)