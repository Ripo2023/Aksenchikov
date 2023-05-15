package com.example.myapplicationnew

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("737a2d8c-c188-4a83-8e7d-10b5c042c9b7");
    }
}