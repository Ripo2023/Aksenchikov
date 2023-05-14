package com.example.myapplicationnew.presentation.OnboardScreen.models

import androidx.annotation.IdRes

//Модель карточки с информацией
data class OnboardCardModel(
    @IdRes val image:Int,
    @IdRes val title:Int,
    @IdRes val text:Int
)