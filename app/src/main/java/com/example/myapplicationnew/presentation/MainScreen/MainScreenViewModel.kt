package com.example.myapplicationnew.presentation.MainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.domain.CategoryRepository
import com.example.myapplicationnew.domain.ProductRepository
import com.example.myapplicationnew.presentation.MainScreen.models.CategoryLoadState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {


    val categoryState = MutableStateFlow<CategoryLoadState>(CategoryLoadState.Loading)

    val productListState = MutableStateFlow<ProductListState>(ProductListState.Loading)

    fun loadCategoryItems(categoryId:String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryState.update { if(it is CategoryLoadState.Loaded) {
                it.copy(selected = categoryId)
            } else it
            }
            productListState.update { ProductListState.Loading }
            productListState.update {

                val products = productRepository.getCategoryProduct(categoryId)
                if(products.isEmpty()) {
                    ProductListState.NoItems
                } else {
                    ProductListState.ShowProduct(products)
                }
            }
        }
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = categoryRepository.getCategory()
                categoryState.update { CategoryLoadState.Loaded(categoryList,categoryList[0].name) }

                loadCategoryItems(categoryList[0].id)
            }catch (e:Exception) {
                categoryState.update { CategoryLoadState.Error }
                Log.e("MyLog",e.stackTraceToString())
            }
        }
    }
}