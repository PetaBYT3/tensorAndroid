package com.xliiicxiv.tensor.action

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import io.github.vinceglb.filekit.PlatformFile

sealed interface ActionProfile {

    data object BottomSheetChangeProfilePicture: ActionProfile
    data class CapturedImage(val capturedImage: Bitmap?): ActionProfile
    data class PickedImage(val pickedImage: PlatformFile?): ActionProfile
    data class CroppedImage(val croppedImage: ImageBitmap?): ActionProfile
    data object DeleteProfilePicture: ActionProfile
    data object ChangePassword: ActionProfile
    data object SignOut: ActionProfile

}