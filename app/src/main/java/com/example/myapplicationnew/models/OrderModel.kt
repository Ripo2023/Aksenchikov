package com.example.myapplicationnew.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class OrderModel(
    val id:String = UUID.randomUUID().toString(),
    val productId:String,
    val volumeCof:Float,
)