package com.example.myapplicationnew.presentation.MapScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplicationnew.R
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapScreen() {
    AndroidView(
        factory = {
            MapView(it).apply {
                map.move(CameraPosition(Point(53.897147, 27.559250),20f,0f,0f))

                map.mapObjects.addPlacemark(
                    Point(53.897147, 27.559250),
                    ImageProvider.fromResource(it,R.drawable.location,true),
                    IconStyle()
                )

                map.mapObjects.addPlacemark(
                    Point(53.897332, 27.559063),
                    ImageProvider.fromResource(it,R.drawable.location,true),
                    IconStyle()
                )
            }
        }
    )
}