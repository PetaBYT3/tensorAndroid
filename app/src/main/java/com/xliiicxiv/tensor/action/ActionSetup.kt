package com.xliiicxiv.tensor.action

import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap
import io.github.vinceglb.filekit.PlatformFile

sealed interface ActionSetup {

    data class TextFieldUserName(val userName: String): ActionSetup
    data object SetUserName: ActionSetup
    data class TextFieldDisplayName(val displayName: String): ActionSetup
    data object SetDisplayName: ActionSetup
    data class PickedImage(val pickedImage: PlatformFile?): ActionSetup
    data class CroppedImage(val croppedImage: ImageBitmap): ActionSetup

}