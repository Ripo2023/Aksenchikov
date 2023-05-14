package com.example.myapplicationnew.domain

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//Менежер для управления состоянием Onboard Screen
class OnboardScreenManager @Inject constructor(
    private val preferencesStorage: PreferencesStorage
) {
    private val isShowOnboardKey = booleanPreferencesKey("isShowOnboardKey")

    suspend fun changeState(state:Boolean) {
        preferencesStorage.writeProperty(isShowOnboardKey,state)
    }

    val isNeedShowOnboard : Flow<Boolean> = preferencesStorage.getProperty(isShowOnboardKey,true)
}