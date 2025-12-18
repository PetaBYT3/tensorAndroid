package com.xliiicxiv.tensor.state

import androidx.compose.ui.graphics.ImageBitmap
import com.xliiicxiv.tensor.dataclass.DataClassUser
import io.github.vinceglb.filekit.PlatformFile

data class StateSetup(

    val userData: DataClassUser? = null,

    val textFieldUserName: String = "",
    val isButtonSetUserNameLoading: Boolean = false,

    val textFieldDisplayName: String = "",
    val isButtonDisplayNameLoading: Boolean = false,

    val pickedImage: ImageBitmap? = null,
    val croppedImage: ImageBitmap? = null

)
