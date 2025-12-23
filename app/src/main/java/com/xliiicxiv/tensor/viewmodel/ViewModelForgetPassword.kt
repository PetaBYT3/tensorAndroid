package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionForgetPassword
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateForgetPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelForgetPassword(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(StateForgetPassword())
    val state = _state.asStateFlow()

    fun onAction(action: ActionForgetPassword) {
        when (action) {
            is ActionForgetPassword.TextFieldEmail -> {
                _state.update { it.copy(textFieldEmail = action.email) }
            }
            ActionForgetPassword.SendPasswordResetEmail -> {
                sendPasswordResetEmail()
            }
        }
    }

    private fun sendPasswordResetEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val sendPasswordResetEmailResult = withContext(Dispatchers.IO) {
                repositoryAuth.sendPasswordResetEmail(_state.value.textFieldEmail)
            }

            when (sendPasswordResetEmailResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Reset Password Have Been Sent To Your Email")
                    _state.update { it.copy(textFieldEmail = "") }
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(sendPasswordResetEmailResult.message)
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

}