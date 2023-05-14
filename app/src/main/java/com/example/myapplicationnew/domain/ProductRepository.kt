package com.example.myapplicationnew.domain

import com.example.myapplicationnew.presentation.MainScreen.models.ProductModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject


class ProductRepository @Inject constructor() {

    val database by lazy {
        Firebase.database
    }

    suspend fun getCategoryProduct(categoryId:String) : List<ProductModel> {
        @Serializable
        data class RemoteProductModel(
            val name:String,
            val price:String
        )
        return database.getReference(categoryId).get().asDeferred().await().children.map {
            val remoteModel = Json.decodeFromString(RemoteProductModel.serializer(),it.value as String)

            ProductModel(it.key as String,remoteModel.name,remoteModel.price,null)
        }
    }
}