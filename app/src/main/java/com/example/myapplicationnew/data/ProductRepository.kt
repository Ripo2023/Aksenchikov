package com.example.myapplicationnew.data

import com.example.myapplicationnew.presentation.MainScreen.models.ProductModel
import com.example.myapplicationnew.presentation.MainScreen.models.SubModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject



//Репозиторий заказов
class ProductRepository @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    private val database by lazy {
        Firebase.database
    }

    val storage by lazy {
        Firebase.storage
    }


    @Serializable
    data class RemoteProductModel(
        val name:String,
        val price:String
    )
    suspend fun getCategoryProduct(categoryId:String) : List<ProductModel> {
        val ref  = database.getReference(categoryId)

        val productList = ref.get().asDeferred().await().children.map {
            val remoteModel = Json.decodeFromString(RemoteProductModel.serializer(),it.value as String)

            ProductModel(it.key as String,remoteModel.name,remoteModel.price,null)
        }

        return productList.map {
            val storageRef = storage.reference.child("Images/${it.id}.png")

            it.copy(imageUrl = storageRef.downloadUrl.asDeferred().await().toString())
        }

    }

    suspend fun getSubs() : List<SubModel> {

        @Serializable
        data class RemoteSubModel(
            val name:String
        )
        val ref = database.getReference("Sub")

        val sumList = ref.get().asDeferred().await().children.map {
            SubModel((it.key as String),
                Json.decodeFromString(RemoteSubModel.serializer(),(it.value as String)).name)
        }

        return sumList.map {
            val storageRef = storage.reference.child("Sub/${it.id}")

            it.copy(imageUri = storageRef.downloadUrl.asDeferred().await().toString())
        }
    }

    suspend fun getProductImageUrl(id:String) : String? {
        val storageRef = storage.reference.child("Images/${id}.png")

        return try {
            storageRef.downloadUrl.asDeferred().await().toString()
        }catch (e:Exception) { null }
    }

    suspend fun getName(id:String) : String? {
        val ref = database.getReference("Coffee")

        val idWithName = ref.get().await().children.map {
            it.key as String to Json.decodeFromString(RemoteProductModel.serializer(),it.value as String).name
        }

        return idWithName.firstOrNull() { it.first == id }?.second
    }

    suspend fun getPrice(id:String) : String? {
        val ref = database.getReference("Coffee")

        val idWithPrice = ref.get().await().children.map {
            it.key as String to Json.decodeFromString(RemoteProductModel.serializer(),it.value as String).price
        }

        return idWithPrice.firstOrNull() { it.first == id }?.second
    }
}