package com.example.myapplicationnew.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LastOrderModel(
    val name:String,
    val count:Int
)
