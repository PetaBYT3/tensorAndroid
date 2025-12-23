package com.xliiicxiv.tensor.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionSignUp
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateSignUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelSignUp(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(StateSignUp())
    val state = _state.asStateFlow()

    fun onAction(action: ActionSignUp) {
        when (action) {
            is ActionSignUp.TextFieldEmail -> {
                _state.update { it.copy(textFieldEmail = action.email) }
            }
            is ActionSignUp.TextFieldPassword -> {
                _state.update { it.copy(textFieldPassword = action.password) }
            }
            is ActionSignUp.TextFieldRetypePassword -> {
                _state.update { it.copy(textFieldRetypePassword = action.retypePassword) }
            }
            ActionSignUp.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.update { it.copy(isSignUpButtonLoading = true) }

            val signInResult = withContext(Dispatchers.IO) {
                repositoryAuth.signUp(
                    _state.value.textFieldEmail,
                    _state.value.textFieldPassword,
                    _state.value.textFieldRetypePassword
                )
            }

            when (signInResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Sign Up Successful")
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(signInResult.message)
                }
            }

            _state.update { it.copy(isSignUpButtonLoading = false) }
        }
    }

}