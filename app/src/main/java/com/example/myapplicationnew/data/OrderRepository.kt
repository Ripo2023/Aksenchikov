package com.example.myapplicationnew.data

import com.example.myapplicationnew.domain.AuthManager
import com.example.myapplicationnew.models.OrderModel
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

//репозиторий заказов
@Singleton
class OrderRepository @Inject constructor(
    private val authManager: AuthManager,
    private val productRepository: ProductRepository
) {
    @Serializable
     data class RemoteOrderModel(
        val productId:String,
        val volumeCof:Float,
        val isReady:Boolean
     )

    val database by lazy {
        Firebase.database
    }



    suspend fun makeOrder(
        orderModel: OrderModel
    ) {
        val ref = database.getReference("Orders/${authManager.currentUser!!.uid}")

        ref.push().setValue(Json.encodeToString(
            RemoteOrderModel.serializer(),
            RemoteOrderModel(orderModel.productId,orderModel.volumeCof,orderModel.isReady)
        ))
    }

    suspend fun loadUserOrders() : List<OrderModel> {
        val ref = database.getReference("Orders/${authManager.currentUser!!.uid}")

        return ref.get().asDeferred().await().children.map {
            val remoteModel = Json.decodeFromString(RemoteOrderModel.serializer(),(it.value as String))

            OrderModel(it.key as String,remoteModel.productId, remoteModel.volumeCof,remoteModel.isReady)
        }
    }

    suspend fun getReadyOrders() : List<OrderViewModel> {
        val orders = loadUserOrders()

        return orders.map {
            OrderViewModel(it.id,productRepository.getProductImageUrl(it.productId),it.isReady,productRepository.getName(it.productId),it.volumeCof,it.productId)
        }.filter { it.isReady }
    }

    suspend fun getInProcessOrders() : List<OrderViewModel> {
        val orders = loadUserOrders()

        return orders.map {
            OrderViewModel(it.id,productRepository.getProductImageUrl(it.productId),it.isReady,productRepository.getName(it.productId),it.volumeCof,it.productId)
        }.filter { !it.isReady }
    }
}