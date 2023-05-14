package com.example.myapplication.domain

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("prefs")

    suspend fun <TYPE> writeProperty(key: Preferences.Key<TYPE>,value:TYPE) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    fun <TYPE> getProperty(key: Preferences.Key<TYPE>,defValue:TYPE) : Flow<TYPE> {
        return context.dataStore.data.map {
            it[key] ?: defValue
        }
    }

    fun <TYPE> getPropertyOrNull(key: Preferences.Key<TYPE>) : Flow<TYPE?> {
        return context.dataStore.data.map {
            it[key]
        }
    }

    suspend fun <TYPE> removeProperty(key:Preferences.Key<TYPE>) {
        context.dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun <TYPE> isContainProperty(key:Preferences.Key<TYPE>) : Flow<Boolean> {
         return context.dataStore.data.map {
            it.contains(key)
        }
    }
}