package com.example.myapplicationnew.presentation.MainScreen.models

sealed class ProductListState {
    object Loading : ProductListState()

    data class ShowProduct(val list:List<ProductModel>) : ProductListState()

    object NoItems : ProductListState()
}
