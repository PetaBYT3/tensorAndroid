package com.xliiicxiv.tensor.extension

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes

suspend fun PlatformFile.toBitmap(): ImageBitmap {
    val imageBytes = this.readBytes()
    val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return imageBitmap.asImageBitmap()
}