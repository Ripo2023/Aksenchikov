package com.example.myapplicationnew.presentation.OrderScreen.models

import android.graphics.Bitmap

sealed class QrState {

    object Hide : QrState()

    data class Show(val image:Bitmap) : QrState()
}