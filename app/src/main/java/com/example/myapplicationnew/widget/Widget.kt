package com.example.myapplicationnew.widget

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.myapplicationnew.domain.models.LastOrderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//Класс отприсовки виджета
class Widget(
    private val lastOrder:Flow<LastOrderModel?>
) : GlanceAppWidget() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var lastOrderModel:MutableState<LastOrderModel?> = mutableStateOf(null)

    private var isCollect:Boolean = false

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)

        scope.coroutineContext.cancelChildren()
        lastOrderModel.value = null
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        startCollect(context,id)
        provideContent {
            Column() {
                LazyColumn(
                    GlanceModifier.fillMaxSize()
                    .background(Color(0xFFFF5712)).padding(10.dp),
                    horizontalAlignment = androidx.glance.layout.Alignment.Horizontal.CenterHorizontally,
                ) {

                    if(lastOrderModel.value == null) {
                        item {
                            Text(
                                text = "Вы ещё не делали заказов",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = ColorProvider(Color.White),
                                    fontSize = 16.sp
                                )
                            )
                        }
                    } else  {
                        item {
                            Column() {
                                Text(
                                    text = "Название: ${lastOrderModel.value?.name}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color.White),
                                        fontSize = 16.sp
                                    )
                                )
                                Text(
                                    text = "Количество: ${lastOrderModel.value?.count}",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorProvider(Color.White),
                                        fontSize = 16.sp
                                    ),

                                    )
                            }
                        }
                    }
                }
            }
        }
    }

    //наблюдение за последним заказом
    private fun startCollect(context: Context,glanceId: GlanceId) {
        if(isCollect) return
        isCollect = true
        scope.launch {
            lastOrder.collect() {
                lastOrderModel.value = it

                this@Widget.updateAll(context)
                this@Widget.update(context,glanceId)
            }
        }
    }
}