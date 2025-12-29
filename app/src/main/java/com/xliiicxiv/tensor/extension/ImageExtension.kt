package com.xliiicxiv.tensor.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

fun ImageBitmap.toBase64(): String {
    val androidBitmap = this.asAndroidBitmap()
    return androidBitmap.toBase64()
}

private fun Bitmap.toBase64(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 75
): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(format, quality, outputStream)
    val bytes = outputStream.toByteArray()

    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun String.toImageBitmap(): ImageBitmap? {
    val androidBitmap = this.toAndroidBitmap()
    return androidBitmap?.asImageBitmap()
}

private fun String.toAndroidBitmap(): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
