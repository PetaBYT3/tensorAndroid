package com.xliiicxiv.tensor.state

import androidx.compose.ui.graphics.ImageBitmap
import com.google.firebase.auth.FirebaseUser
import com.xliiicxiv.tensor.dataclass.DataClassUser
import io.github.vinceglb.filekit.PlatformFile

data class StateProfile(

    val firebaseUser: FirebaseUser? = null,
    val userData: DataClassUser? = null,

    val bottomSheetChangeProfilePicture: Boolean = false,

    val dialogCropImage: Boolean = false,
    val imageBitmap: ImageBitmap? = null,
    val croppedImageBitmap: ImageBitmap? = null,

)
