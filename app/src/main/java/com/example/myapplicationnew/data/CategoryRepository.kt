package com.example.myapplicationnew.data

import com.example.myapplicationnew.domain.models.CategoryProduct
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

//Репозиторий для категорий
class CategoryRepository @Inject constructor() {

    private val database by lazy {
        Firebase.database
    }

    suspend fun getCategory() : List<CategoryProduct> {
        val ref =  database.getReference("Category")

         return ref.get().asDeferred().await().children.map {
            CategoryProduct((it.key as String),it.value as String)
        }

    }
}