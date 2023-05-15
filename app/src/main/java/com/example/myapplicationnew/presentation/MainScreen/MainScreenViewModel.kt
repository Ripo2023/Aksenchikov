package com.example.myapplicationnew.presentation.MainScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.data.CategoryRepository
import com.example.myapplicationnew.domain.LastOrderInfoManager
import com.example.myapplicationnew.data.OrderRepository
import com.example.myapplicationnew.data.ProductRepository
import com.example.myapplicationnew.domain.models.LastOrderModel
import com.example.myapplicationnew.italianNameByProductId
import com.example.myapplicationnew.models.OrderModel
import com.example.myapplicationnew.presentation.MainScreen.models.CategoryLoadState
import com.example.myapplicationnew.presentation.MainScreen.models.MakeOrderDialogState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductListState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductModel
import com.example.myapplicationnew.presentation.MainScreen.models.SubModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val lastOrderInfoManager: LastOrderInfoManager,
    @ApplicationContext private val context:Context
) : ViewModel() {


    val categoryState = MutableStateFlow<CategoryLoadState>(CategoryLoadState.Loading)

    val productListState = MutableStateFlow<ProductListState>(ProductListState.Loading)

    val subs = MutableStateFlow(emptyList<SubModel>())

    val makeOrderDialogState = MutableStateFlow<MakeOrderDialogState>(
        MakeOrderDialogState.Hided
    )

    val selectedSubs = mutableStateListOf<String>()

    fun showMakeOrderDialogState(productModel: ProductModel) {
        makeOrderDialogState.update { MakeOrderDialogState.Showed(productModel) }
        viewModelScope.launch(Dispatchers.IO) {
            if(subs.value.isNotEmpty()) return@launch
            subs.update { productRepository.getSubs() }
        }
    }

    fun hideMakeOrderDialogState() {
        makeOrderDialogState.update { MakeOrderDialogState.Hided }
    }

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

    fun makeOrder(productId:String,volume:Float,productName:String) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.makeOrder(
                orderModel = OrderModel(productId = productId, volumeCof = volume)
            )
            hideMakeOrderDialogState()

            lastOrderInfoManager.setLastOrder(LastOrderModel(productName,1))

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Товар добавлен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showInfo(productId: String) {

        val originalName = italianNameByProductId[productId]
        Toast.makeText(context, buildString {
            append("Разработчик:Аксёнчиков А.В\n")
            if(originalName != null) append("Оригинальное название:$originalName")
        },
            Toast.LENGTH_SHORT).show()
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