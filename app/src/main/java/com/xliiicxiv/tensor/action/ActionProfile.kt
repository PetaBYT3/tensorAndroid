package com.xliiicxiv.tensor.action

import androidx.compose.ui.graphics.ImageBitmap
import io.github.vinceglb.filekit.PlatformFile

sealed interface ActionProfile {

    data class PickedImage(val pickedImage: PlatformFile?): ActionProfile
    data class CroppedImage(val croppedImage: ImageBitmap?): ActionProfile
    data object DeleteProfilePicture: ActionProfile
    data object ChangePassword: ActionProfile
    data object SignOut: ActionProfile

}