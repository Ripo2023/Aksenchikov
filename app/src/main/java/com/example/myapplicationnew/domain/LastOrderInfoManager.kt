package com.example.myapplicationnew.domain

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.updateAll
import com.example.myapplicationnew.domain.models.LastOrderModel
import com.example.myapplicationnew.widget.Widget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

//Управляющий информацией о последнем заказе для widgetа
class LastOrderInfoManager @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
   @ApplicationContext private val context:Context
) {

    private val lastOrderKey = stringPreferencesKey("lastOrderKey")

    val lastOrder : Flow<LastOrderModel?>
        get() = preferencesStorage.getPropertyOrNull(lastOrderKey).map {
            try {
                Json.decodeFromString(LastOrderModel.serializer(),it!!)
            }catch (e:Exception) {
                null
            }
        }

    suspend fun setLastOrder(lastOrderModel: LastOrderModel) {
        preferencesStorage.writeProperty(lastOrderKey,Json.encodeToString(LastOrderModel.serializer(),lastOrderModel))

        Widget(lastOrder).updateAll(context)
    }
}