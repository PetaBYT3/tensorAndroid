package com.xliiicxiv.tensor.viewmodel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.action.ActionProfile
import com.xliiicxiv.tensor.extension.toBase64
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.state.StateProfile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelProfile(
    private val repositoryAuth: RepositoryAuth,
    private val repositoryUser: RepositoryUser
): ViewModel() {

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(StateProfile())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val getUserAuth = withContext(Dispatchers.IO) {
                repositoryAuth.getUserAuth()
            }
            getUserAuth.collect { firebaseUser ->
                _state.update { it.copy(firebaseUser = firebaseUser) }
            }
        }

        viewModelScope.launch {
            val getUserData = withContext(Dispatchers.IO) {
                repositoryUser.getUserData()
            }
            getUserData.collect { userData ->
                _state.update { it.copy(userData = userData) }
            }
        }
    }

    fun onAction(action: ActionProfile) {
        when (action) {
            ActionProfile.BottomSheetChangeProfilePicture -> {
                _state.update { it.copy(bottomSheetChangeProfilePicture = !it.bottomSheetChangeProfilePicture) }
            }
            is ActionProfile.CapturedImage -> {
                capturedImage(action.capturedImage)
            }
            is ActionProfile.PickedImage -> {
                pickedImage(action.pickedImage)
            }
            is ActionProfile.CroppedImage -> {
                croppedImage(action.croppedImage)
            }
            ActionProfile.DeleteProfilePicture -> {
                deleteProfilePicture()
            }
            ActionProfile.ChangePassword -> {
                changePassword()
            }
            ActionProfile.SignOut -> {
                signOut()
            }
        }
    }

    private fun capturedImage(capturedImage: Bitmap?) {
        viewModelScope.launch {
            _state.update { currentState ->
                if (capturedImage != null) {
                    val imageBitmap = withContext(Dispatchers.Default) {
                        capturedImage.asImageBitmap()
                    }
                    currentState.copy(imageBitmap = imageBitmap)
                } else {
                    currentState.copy(imageBitmap = null)
                }
            }
        }
    }

    private fun pickedImage(pickedImage: PlatformFile?) {
        viewModelScope.launch {
            if (pickedImage != null) {
                val imageBitmap = withContext(Dispatchers.IO) {
                    pickedImage.toImageBitmap()
                }

                _state.update { it.copy(imageBitmap = imageBitmap) }
            } else {
                _state.update { it.copy(imageBitmap = null) }
            }
        }
    }

    private fun croppedImage(croppedImage: ImageBitmap?) {
        viewModelScope.launch {
            _state.update {
                it.copy(imageBitmap = null, croppedImageBitmap = croppedImage)
            }

            if (croppedImage != null) {
                val croppedImageBase64 = withContext(Dispatchers.Default) {
                    croppedImage.toBase64()
                }
                val uploadProfilePictureResult = withContext(Dispatchers.IO) {
                    repositoryUser.uploadProfilePicture(croppedImageBase64)
                }

                when (uploadProfilePictureResult) {
                    FirebaseResponse.Success -> {
                        _effect.emit("Profile Picture Successfully Uploaded")
                    }
                    is FirebaseResponse.Failed -> {
                        _effect.emit(uploadProfilePictureResult.message)
                    }
                }
            }
        }
    }

    private fun deleteProfilePicture() {
        viewModelScope.launch {
            val deleteProfilePictureResult = withContext(Dispatchers.IO) {
                repositoryUser.uploadProfilePicture(null)
            }

            when (deleteProfilePictureResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Profile Picture Successfully Deleted")
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(deleteProfilePictureResult.message)
                }
            }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            val changePasswordResult = withContext(Dispatchers.IO) {
                repositoryAuth.sendPasswordResetEmail(_state.value.firebaseUser?.email.toString())
            }

            when (changePasswordResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Password Reset Receipt Has Been Sent To Your Email")
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(changePasswordResult.message)
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            val signOutResult = withContext(Dispatchers.IO) {
                repositoryAuth.signOut()
            }
            _effect.emit(signOutResult)
        }
    }

}