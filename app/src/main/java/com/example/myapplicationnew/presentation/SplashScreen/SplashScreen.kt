package com.example.myapplicationnew.presentation.SplashScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplicationnew.R
import com.example.myapplicationnew.themeGradients

//Разметка SplashScreen
@Composable
fun SplashScreen(
    navController: NavController,
    splashScreenViewModel:SplashScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        splashScreenViewModel.onSplash(navController)
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeGradients.splashGradient),
        contentAlignment = Alignment.Center
    ) {
       Column(
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           AsyncImage(
               model = R.drawable.square,
               contentDescription = "",
               modifier = Modifier.size(150.dp)
           )

           Icon(
               painter = painterResource(
                   id = R.drawable.logo_coffee),
               contentDescription = "",
           )
       }
    }
}