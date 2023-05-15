package com.example.myapplicationnew.domain

import com.example.myapplicationnew.models.OrderModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

//репозиторий заказов
class OrderRepository @Inject constructor(
    private val authManager: AuthManager
) {
    @Serializable
     data class RemoteOrderModel(
        val productId:String,
        val volumeCof:Float,
     )

    val database by lazy {
        Firebase.database
    }

    suspend fun makeOrder(
        orderModel: OrderModel
    ) {
        val ref = database.getReference("Orders/${authManager.currentUser!!.uid}")

        ref.push().setValue(Json.encodeToString(RemoteOrderModel.serializer(),
            RemoteOrderModel(orderModel.productId,orderModel.volumeCof)
        ))
    }
}