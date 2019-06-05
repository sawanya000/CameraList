package com.example.cameralist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun BitmapToString(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT)
}

fun StringToBitmap(string: String): Bitmap {
    val imageBytes = android.util.Base64.decode(string, 0)
    val image= BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
    return image
}