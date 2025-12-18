package com.xliiicxiv.tensor.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionProfile
import com.xliiicxiv.tensor.extension.toBase64
import com.xliiicxiv.tensor.extension.toBitmap
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.state.StateProfile
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelProfile(
    private val repositoryAuth: RepositoryAuth,
    private val repositoryUser: RepositoryUser
): ViewModel() {

    private val _state = MutableStateFlow(StateProfile())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryAuth.getCurrentUser().collect { firebaseUser ->
                _state.update { it.copy(firebaseUser = firebaseUser) }
            }
        }

        viewModelScope.launch {
            repositoryUser.getUserData().collect { userData ->
                _state.update { it.copy(userData = userData) }
            }
        }
    }

    fun onAction(action: ActionProfile) {
        when (action) {
            is ActionProfile.PickedImage -> {
                viewModelScope.launch {
                    if (action.pickedImage != null) {
                        withContext(Dispatchers.IO) {
                            _state.update { it.copy(imageBitmap = action.pickedImage.toImageBitmap()) }
                        }
                    } else {
                        _state.update { it.copy(imageBitmap = null) }
                    }
                }
            }
            is ActionProfile.CroppedImage -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        imageBitmap = null,
                        croppedImageBitmap = action.croppedImage,
                    ) }
                    if (action.croppedImage != null) {
                        repositoryUser.uploadProfilePicture(action.croppedImage.toBase64())
                    }
                }
            }
            ActionProfile.DeleteProfilePicture -> {
                viewModelScope.launch {
                    repositoryUser.uploadProfilePicture(null)
                }
            }
            ActionProfile.SignOut -> {
                repositoryAuth.signOut()
            }
        }
    }

}