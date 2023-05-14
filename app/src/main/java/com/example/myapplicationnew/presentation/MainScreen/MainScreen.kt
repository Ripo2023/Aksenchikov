package com.example.myapplicationnew.presentation.MainScreen

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationnew.R
import com.example.myapplicationnew.presentation.MainScreen.models.CategoryLoadState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductListState
import com.example.myapplicationnew.themeColors

@OptIn(ExperimentalLayoutApi::class)
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

    val productListState by mainScreenViewModel.productListState.collectAsState()

    val categoryLoadState by mainScreenViewModel.categoryState.collectAsState()

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(end = 5.dp, top = 10.dp)
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

            item {

                when(categoryLoadState) {
                    CategoryLoadState.Error -> Text(text = "Ошибка загрузки категорий")
                    is CategoryLoadState.Loaded -> {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            items((categoryLoadState as CategoryLoadState.Loaded).categoryList) {
                                Card(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(50.dp)
                                        .padding(end = 10.dp,)
                                        .clickable { mainScreenViewModel.loadCategoryItems(it.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = Color.Black
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(50.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = it.name,
                                            color = Color.White,
                                            fontWeight = W900,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    CategoryLoadState.Loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            when(productListState) {

                ProductListState.Loading -> {
                    item { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
                }
                is ProductListState.ShowProduct -> {
                    item {
                        FlowRow(maxItemsInEachRow = 2,modifier = Modifier.fillMaxWidth()) {
                            (productListState as ProductListState.ShowProduct).list.forEach {
                                Card(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = Color.White
                                ) {
                                    Column() {
                                        SubcomposeAsyncImage(
                                            model = it.imageUrl,
                                            contentDescription = "",
                                            modifier = Modifier.size(70.dp)
                                        )

                                        Text(text = it.name)
                                    }
                                }
                            }
                        }
                    }
                }

                ProductListState.NoItems -> item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "Нет в наличии")
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