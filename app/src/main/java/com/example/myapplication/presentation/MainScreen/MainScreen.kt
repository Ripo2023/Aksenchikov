package com.example.myapplication.presentation.MainScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.IdRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.presentation.Screen
import com.example.myapplication.themeColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("ResourceType")
@Composable
fun MainScreen(
    mainScreenViewModel:MainScreenViewModel = hiltViewModel()
) {


    data class Promo(
        @IdRes val image:Int
    )

    val promos = remember {
        listOf(
            Promo(R.drawable.promo),
            Promo(R.drawable.promo),
        )
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(
                start = 15.dp,
                end = 15.dp
            ),
        backgroundColor = themeColors.background,
        topBar = { TopBar() }
    ) {
        LazyColumn(
           modifier = Modifier.padding(
               top = it.calculateTopPadding(),
               end = it.calculateEndPadding(LocalLayoutDirection.current),
               start = it.calculateStartPadding(LocalLayoutDirection.current),
               bottom = it.calculateBottomPadding()
           )
        ) {
            item {
                LazyRow(Modifier.fillMaxWidth()) {
                    items(promos) { promo ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .height(150.dp)
                                .padding(end = 5.dp,top = 10.dp)
                            ,
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            AsyncImage(
                                model = promo.image,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = R.drawable.logo,
            contentDescription = "",
            Modifier
                .size(60.dp)
                .padding(end = 10.dp)
        )

        Column {
            Text(
                text = "Адрес магазина кофе",
                color = themeColors.secondaryFontColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.W200
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = R.drawable.location,
                    contentDescription = "",
                    Modifier.size(20.dp)
                )
                
                Text(
                    text = "43 Marathon st.",
                    fontWeight = FontWeight.W800,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}
