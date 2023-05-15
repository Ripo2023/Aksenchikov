package com.example.myapplicationnew.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
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

    private var lastOrderModel:LastOrderModel? = null

    private var isCollect:Boolean = false

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)

        scope.coroutineContext.cancelChildren()
        lastOrderModel = null
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        startCollect(context,id)
        provideContent {
            Column() {
                Column(GlanceModifier.fillMaxSize()) {
                    Text(text = "Название: ${lastOrderModel?.name}")
                    Text(text = "Количество: ${lastOrderModel?.count}")
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
                lastOrderModel = it

                this@Widget.updateAll(context)
                this@Widget.update(context,glanceId)
            }
        }
    }
}