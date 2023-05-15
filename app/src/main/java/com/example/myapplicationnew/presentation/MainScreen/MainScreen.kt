package com.example.myapplicationnew.presentation.MainScreen

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationnew.R
import com.example.myapplicationnew.presentation.MainScreen.models.CategoryLoadState
import com.example.myapplicationnew.presentation.MainScreen.models.MakeOrderDialogState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductListState
import com.example.myapplicationnew.presentation.MainScreen.models.ProductModel
import com.example.myapplicationnew.presentation.Screen
import com.example.myapplicationnew.themeColors

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("ResourceType")
@Composable
fun MainScreen(
    navController: NavController,
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

    val makeOrderDialogState by mainScreenViewModel.makeOrderDialogState.collectAsState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(
                start = 15.dp,
                end = 15.dp
            ),
        backgroundColor = themeColors.background,
        topBar = { TopBar(navController) }
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
                                val isSelected = (categoryLoadState as CategoryLoadState.Loaded).selected == it.id

                                val color = animateColorAsState(
                                    targetValue = if(isSelected) Color.Black else Color.White,
                                    animationSpec = tween(1000)
                                )

                                val textColor = animateColorAsState(
                                    targetValue = if(isSelected) Color.White else Color.Gray,
                                    animationSpec = tween(1000)
                                )
                                Card(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(50.dp)
                                        .padding(end = 10.dp,)
                                        .clickable { mainScreenViewModel.loadCategoryItems(it.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = color.value
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
                                            color = textColor.value,
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
                        FlowRow(
                            maxItemsInEachRow = 2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (productListState as ProductListState.ShowProduct).list.forEach {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = Color.White
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        SubcomposeAsyncImage(
                                            model = it.imageUrl,
                                            contentDescription = "",
                                            modifier = Modifier.size(70.dp),
                                            loading = {
                                                CircularProgressIndicator()
                                            }
                                        )

                                        Text(text = it.name)
                                        Text(text = "От ${it.price}")

                                        OutlinedButton(
                                            onClick = { mainScreenViewModel.showMakeOrderDialogState(it) },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = themeColors.primary
                                            )
                                        ) {
                                            Text(text = stringResource(R.string.Select))
                                        }
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

    if(makeOrderDialogState is MakeOrderDialogState.Showed) {
        MakeOrderDialog(product = (makeOrderDialogState as MakeOrderDialogState.Showed).product,mainScreenViewModel)
    }
}

@Composable
fun TopBar(navController: NavController) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = R.drawable.logo,
            contentDescription = "",
            Modifier
                .size(60.dp)
                .padding(end = 10.dp)
                .clickable { navController.navigate(Screen.OrderScreen.route) { launchSingleTop = true } }
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

@Composable
fun MakeOrderDialog(
    product:ProductModel,
    mainScreenViewModel: MainScreenViewModel
) {

    val volumes = remember {
        listOf(1f to 200,1.5f to 300,2f to 400)
    }

    var currentSelectedVolume by rememberSaveable() {
        mutableStateOf(volumes[0])
    }

    var currentSelectedSubs = remember {
        mainScreenViewModel.selectedSubs
    }

    val subs = mainScreenViewModel.subs.collectAsState()

    Dialog(
        onDismissRequest = mainScreenViewModel::hideMakeOrderDialogState,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            backgroundColor = Color.Transparent,
            topBar = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            this.detectVerticalDragGestures { change, dragAmount ->
                                mainScreenViewModel.hideMakeOrderDialogState()
                            }
                        }
                ) {

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Icon(painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = product.name,
                            fontSize = 20.sp,
                            color = themeColors.primaryFontColor
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Icon(painter = painterResource(id = R.drawable.baseline_info_24),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Gray
                        )
                    }
                }
            }
        ) {

            Card(
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        end = it.calculateEndPadding(LocalLayoutDirection.current),
                        start = it.calculateStartPadding(LocalLayoutDirection.current),
                        bottom = it.calculateBottomPadding()
                    ),
            ) {
                val state = rememberLazyListState()
                LazyColumn(
                    Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {



                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SubcomposeAsyncImage(
                                model = product.imageUrl,
                                contentDescription = "",
                                loading = {
                                    CircularProgressIndicator()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )
                            Text(text = product.name, color = themeColors.secondaryFontColor, fontSize = 15.sp)
                        }

                        Text(
                            text = "Емкость",
                            fontSize = 24.sp,
                            fontWeight = W900
                        )

                        LazyRow(Modifier.fillMaxWidth()) {
                            items(volumes) {
                                val isSelected = it == currentSelectedVolume

                                val color = animateColorAsState(
                                    targetValue = if(isSelected) Color.Black else Color.White,
                                    animationSpec = tween(1000)
                                )

                                val textColor = animateColorAsState(
                                    targetValue = if(isSelected) Color.White else Color.Gray,
                                    animationSpec = tween(1000)
                                )
                                Card(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(50.dp)
                                        .padding(end = 10.dp,)
                                        .clickable { currentSelectedVolume = it },
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = color.value
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(50.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${it.second} мл",
                                            color = textColor.value,
                                            fontWeight = W900,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "Добавки",
                            fontSize = 24.sp,
                            fontWeight = W900
                        )

                        LazyRow(Modifier.fillMaxWidth()) {
                            items(subs.value) {

                                val isSelected = currentSelectedSubs.contains(it.id)
                                Column(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clickable {
                                            if (isSelected) {
                                                mainScreenViewModel.selectedSubs.remove(it.id)
                                            } else {
                                                mainScreenViewModel.selectedSubs.add(it.id)
                                            }
                                        }
                                        .border(
                                            width = if (isSelected) 5.dp else 0.dp,
                                            color = themeColors.primary
                                        )
                                ) {

                                    if(isSelected) {
                                        SubcomposeAsyncImage(
                                            model = it.imageUri,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .padding(bottom = 10.dp),
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    } else {
                                        Icon(painter = painterResource(id = R.drawable.baseline_add_24),
                                            contentDescription = "",
                                            modifier = Modifier.size(100.dp)
                                        )
                                    }

                                    Text(text = it.name, fontSize = 14.sp,color = themeColors.secondaryFontColor)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                mainScreenViewModel.makeOrder(product.id,currentSelectedVolume.first)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColors.primary),
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 40.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "Добавить в корзину за ${product.price.toInt() * currentSelectedVolume.first}",
                                color = themeColors.label,
                                fontSize = 20.sp,
                                fontWeight = W800
                            )
                        }


                    }
                }
            }
        }
    }
}
