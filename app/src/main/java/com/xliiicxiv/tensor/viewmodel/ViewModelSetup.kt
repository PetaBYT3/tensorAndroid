package com.xliiicxiv.tensor.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionSetup
import com.xliiicxiv.tensor.extension.toBase64
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.state.StateSetup
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSetup(
    private val repositoryUser: RepositoryUser
): ViewModel() {

    init {
        viewModelScope.launch {
            repositoryUser.getUserData().collect { userData ->
                _state.update { it.copy(userData = userData) }
            }
        }
    }

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _movePager = MutableSharedFlow<Int>(replay = 1)
    val movePager = _movePager.asSharedFlow()

    private val _state = MutableStateFlow(StateSetup())
    val state = _state.asStateFlow()

    fun onAction(action: ActionSetup) {
        when (action) {
            is ActionSetup.TextFieldUserName -> {
                _state.update { it.copy(textFieldUserName = action.userName) }
            }
            ActionSetup.SetUserName -> {
                setUserName()
            }
            is ActionSetup.TextFieldDisplayName -> {
                _state.update { it.copy(textFieldDisplayName = action.displayName) }
            }
            ActionSetup.SetDisplayName -> {
                setDisplayName()
            }
            is ActionSetup.PickedImage -> {
                viewModelScope.launch {
                    _state.update { it.copy(pickedImage = action.pickedImage?.toImageBitmap()) }
                }
            }
            is ActionSetup.CroppedImage -> {
                uploadProfilePicture(action.croppedImage)
            }
        }
    }

    private fun setUserName() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonSetUserNameLoading = true) }

            val setUserNameResult = repositoryUser.setUserName(
                userName = _state.value.textFieldUserName
            )

            when (setUserNameResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("User Name Successfully Created")
                    _movePager.emit(1)
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(setUserNameResult.message)
                }
            }

            _state.update { it.copy(isButtonSetUserNameLoading = false) }
        }
    }

    private fun setDisplayName() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonDisplayNameLoading = true) }

            val setDisplayNameResult = repositoryUser.setDisplayName(
                displayName = _state.value.textFieldDisplayName
            )

            when (setDisplayNameResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Display Name Successfully Created")
                    _movePager.emit(1)
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(setDisplayNameResult.message)
                }
            }

            _state.update { it.copy(isButtonDisplayNameLoading = false) }
        }
    }

    private fun uploadProfilePicture(croppedImage: ImageBitmap?) {
        viewModelScope.launch {
            _state.update { it.copy(pickedImage = null) }

            if (croppedImage != null) {
                val uploadProfilePictureResult = repositoryUser.uploadProfilePicture(croppedImage.toBase64())

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

}