package com.example.myapplicationnew.presentation.OrderScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.domain.AuthManager
import com.example.myapplicationnew.domain.OrderRepository
import com.example.myapplicationnew.domain.ProductRepository
import com.example.myapplicationnew.domain.QrGeneratorUseCase
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderItemsDialogState
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import com.example.myapplicationnew.presentation.OrderScreen.models.QrState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderScreenViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val generatorQrUseCase: QrGeneratorUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    val readyOrders:MutableStateFlow<List<OrderViewModel>> = MutableStateFlow(emptyList())

    val inProcessOrders:MutableStateFlow<List<OrderViewModel>> = MutableStateFlow(emptyList())

    val orderDialogState = MutableStateFlow<OrderItemsDialogState>(OrderItemsDialogState.Hide)

    //отображения диалога заказов
    fun showOrderDialog(order:OrderViewModel) {
        orderDialogState.update { OrderItemsDialogState.Show(order) }
    }

    //скрытие диалога заказов
    fun hideOrderDialog() {
        orderDialogState.update { OrderItemsDialogState.Hide }
        hideQr()
    }

    val dialogItemPrice = mutableStateOf("0")

      fun loadImageForDialog(productId:String)  {
          viewModelScope.launch { dialogItemPrice.value =  productRepository.getPrice(productId) ?: "0" }
    }

    val qrState = MutableStateFlow<QrState>(QrState.Hide)

    fun showQr(order: OrderViewModel) {
        viewModelScope.launch {
           val qrCode =  generatorQrUseCase.generateQrCode("Номер:${authManager.currentUser?.phoneNumber ?: "+375336441254"}\n" +
                   "Заказ:${order.name}\n" +
                   "id:${order.id}")
            qrState.update { QrState.Show(qrCode) }
        }
    }

    fun hideQr() {
        qrState.update { QrState.Hide }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val ready = viewModelScope.async { orderRepository.getReadyOrders() }
            val isProcess = viewModelScope.async { orderRepository.getInProcessOrders() }

            readyOrders.update { ready.await() }
            inProcessOrders.update { isProcess.await() }
        }
    }
}