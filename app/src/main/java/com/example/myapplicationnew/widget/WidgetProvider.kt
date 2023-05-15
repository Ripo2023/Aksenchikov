package com.example.myapplicationnew.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.myapplicationnew.domain.LastOrderInfoManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//Провайдер виджета
@AndroidEntryPoint
class WidgetProvider : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var lastOrderInfoManager: LastOrderInfoManager

    override val glanceAppWidget: GlanceAppWidget by lazy { Widget(lastOrderInfoManager.lastOrder) }
}