package com.example.myapplicationnew.presentation.OrderScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplicationnew.themeColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OrderScreen(
    orderScreenViewModel: OrderScreenViewModel = hiltViewModel()
) {
    val ready by orderScreenViewModel.readyOrders.collectAsState()
    
    val inProcess by orderScreenViewModel.inProcessOrders.collectAsState()
    
    
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
                    .background(themeColors.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.photoImage,
                    contentDescription = "",
                    modifier = Modifier
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
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = it.photoImage,
                    contentDescription = "",
                    modifier = Modifier
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
    }
}