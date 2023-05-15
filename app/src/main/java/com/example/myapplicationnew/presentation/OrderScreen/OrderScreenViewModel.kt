package com.example.myapplicationnew.presentation.OrderScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationnew.domain.OrderRepository
import com.example.myapplicationnew.models.OrderModel
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderScreenViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val readyOrders:MutableStateFlow<List<OrderViewModel>> = MutableStateFlow(emptyList())

    val inProcessOrders:MutableStateFlow<List<OrderViewModel>> = MutableStateFlow(emptyList())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            val ready = viewModelScope.async { orderRepository.getReadyOrders() }
            val isProcess = viewModelScope.async { orderRepository.getInProcessOrders() }

            readyOrders.update { ready.await() }
            inProcessOrders.update { isProcess.await() }
        }
    }
}