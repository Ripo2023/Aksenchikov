package com.example.myapplication.domain

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import java.util.EnumMap
import javax.inject.Inject

class QrGeneratorUseCase @Inject constructor() {

    fun generateQrCode(string: String) : Bitmap {
        val x = 500
        val y = 500
        val hints = EnumMap<EncodeHintType,Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(string,BarcodeFormat.QR_CODE,x,y,hints)
        val bitmap = Bitmap.createBitmap(x,y,Bitmap.Config.RGB_565)

        for (w in 0 until  x) {
            for (h in 0 until y) {
                bitmap.set(
                    w,
                    h,
                    if(bitMatrix[w,h]) Color.BLACK else Color.WHITE
                )
            }
        }

        return bitmap
    }
}