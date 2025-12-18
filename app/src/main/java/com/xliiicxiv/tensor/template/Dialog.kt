@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.template

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.AspectRatio
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import com.smarttoolfactory.cropper.settings.CropType

@Composable
fun ImageCropDialog(
    imageBitmap: ImageBitmap,
    onConfirm: (ImageBitmap) -> Unit,
    onCancel: () -> Unit
) {
    var shouldCrop by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onCancel.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crop Image") },
                    navigationIcon = {
                        CustomIconButton(
                            icon = Icons.Rounded.Close,
                            onClick = { onCancel.invoke() }
                        )
                    },
                    actions = {
                        CustomIconButton(
                            icon = Icons.Rounded.Check,
                            onClick = { shouldCrop = true }
                        )
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                ImageCropper(
                    modifier = Modifier.fillMaxSize(),
                    imageBitmap = imageBitmap,
                    contentDescription = "Image Cropper",
                    crop = shouldCrop,
                    cropProperties = CropDefaults.properties(
                        cropType = CropType.Static,
                        handleSize = 30f,
                        maxZoom = 5f,
                        aspectRatio = AspectRatio(1f / 1f),
                        cropOutlineProperty = CropOutlineProperty(
                            outlineType = OutlineType.RoundedRect,
                            cropOutline = RectCropShape(
                                id = 0,
                                title = "Rect"
                            )
                        )
                    ),
                    cropStyle = CropDefaults.style(
                        overlayColor = Color.Black.copy(alpha = 0.7f),
                        drawOverlay = true,
                        drawGrid = true
                    ),
                    onCropStart = {
                    },
                    onCropSuccess = { croppedResultBitmap ->
                        shouldCrop = false
                        onConfirm(croppedResultBitmap)
                    }
                )
            }
        }
    }
}