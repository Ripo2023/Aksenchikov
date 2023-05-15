package com.example.myapplicationnew.presentation.OrderScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.myapplicationnew.R
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderItemsDialogState
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import com.example.myapplicationnew.presentation.OrderScreen.models.QrState
import com.example.myapplicationnew.themeColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderScreen(
    orderScreenViewModel: OrderScreenViewModel = hiltViewModel()
) {
    val ready by orderScreenViewModel.readyOrders.collectAsState()
    
    val inProcess by orderScreenViewModel.inProcessOrders.collectAsState()

    val orderDialogState by orderScreenViewModel.orderDialogState.collectAsState()
    
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Готовы",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900
            )
        }

        items(ready) {

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(themeColors.primary)
                    .clickable { orderScreenViewModel.showOrderDialog(it) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.photoImage,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 60.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = it.id,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W800
                    )
                }
            }
        }

        item {
            Text(
                text = "В процессе",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.W900
            )
        }

        items(inProcess) {
            val color = themeColors.primary
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .clickable { orderScreenViewModel.showOrderDialog(it) }
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.photoImage,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 60.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .drawBehind {
                            drawCircle(color)
                        }
                )
                Text(
                    text = it.id,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W800,
                    textAlign = TextAlign.End
                )
            }
        }
    }

    if(orderDialogState is OrderItemsDialogState.Show) {
        OrderItemsDialog(order = (orderDialogState as OrderItemsDialogState.Show).order,orderScreenViewModel)
    }
}


@Composable
fun OrderItemsDialog(order:OrderViewModel,orderScreenViewModel: OrderScreenViewModel) {

    val cofPair = listOf(
        1f to 200,
        1.5f to 300,
        2f to 400
    )

    val qrState = orderScreenViewModel.qrState.collectAsState()


    val price = remember() {
        orderScreenViewModel.dialogItemPrice
    }
    LaunchedEffect(key1 = Unit, block = {
        orderScreenViewModel.loadImageForDialog(order.productId)
    })
    Dialog(
        onDismissRequest = orderScreenViewModel::hideOrderDialog,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .pointerInput(Unit) {
                            this.detectVerticalDragGestures { change, dragAmount ->
                                orderScreenViewModel.hideOrderDialog()
                            }
                        }
                ) {

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    if (qrState.value is QrState.Show) {
                                        orderScreenViewModel.hideQr()
                                    } else {
                                        orderScreenViewModel.hideOrderDialog()
                                    }
                                },
                        )

                        Text(
                            text = if(qrState.value is QrState.Hide) stringResource(R.string.pr) else stringResource(
                                                            R.string.qr),
                            fontSize = 30.sp,
                            color = themeColors.primaryFontColor,
                            fontWeight = FontWeight.W900,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp)
                        )
                    }
                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp),color = Color.Gray,)

                }
            }
        ) {
            if(qrState.value is QrState.Hide) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(themeColors.background)
                    ) {
                        Button(
                            onClick = { orderScreenViewModel.showQr(order) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColors.primary),
                            modifier = Modifier.fillMaxWidth().padding(15.dp).height(75.dp)
                        ) {
                            Text(
                                text = "QR code",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.W900,
                                color = themeColors.label
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = (qrState.value as? QrState.Show)?.image,
                        contentDescription = "",
                    )
                }
            }
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = it.calculateTopPadding(),
                        end = it.calculateEndPadding(LocalLayoutDirection.current),
                        start = it.calculateStartPadding(LocalLayoutDirection.current),
                        bottom = it.calculateBottomPadding()
                    )
            ) {

                if(qrState.value is QrState.Hide) {
                    item {

                        Row(
                            Modifier
                                .fillMaxWidth()
                        ) {

                            SubcomposeAsyncImage(
                                model = order.photoImage,
                                contentDescription = "",
                                modifier = Modifier.size(80.dp),
                                loading = { CircularProgressIndicator() }
                            )

                            Column {
                                Text(
                                    text = order.name ?: "",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W900,
                                    color = Color.Black
                                )

                                Text(
                                    text = "${cofPair.first { it.first == order.volume }.second} мл",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W900,
                                    color = Color.Black
                                )

                            }
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                Text(
                                    text = "${price.value.toInt() * order.volume} р",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W900,
                                    color = Color.Black
                                )
                            }


                        }

                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .height(1.dp),color = Color.Gray,)



                    }
                }

            }
        }
    }
}