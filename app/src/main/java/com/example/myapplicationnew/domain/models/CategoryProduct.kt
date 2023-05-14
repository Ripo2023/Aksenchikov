package com.example.myapplicationnew.domain.models

import kotlinx.serialization.Serializable

//Модель категории продукта
@Serializable
data class CategoryProduct(
    val id:String,
    val name:String,
)