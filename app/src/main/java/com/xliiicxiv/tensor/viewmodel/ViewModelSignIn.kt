package com.xliiicxiv.tensor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelSignIn(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(StateSignIn())
    val state = _state.asStateFlow()

    fun onAction(action: ActionSignIn) {
        when (action) {
            is ActionSignIn.TextFieldEmail -> {
                _state.update { it.copy(textFieldEmail = action.email) }
            }
            is ActionSignIn.TextFieldPassword -> {
                _state.update { it.copy(textFieldPassword = action.password) }
            }
            ActionSignIn.SignIn -> {
                signIn()
            }
            is ActionSignIn.SignInWithGoogle -> {
                signInWithGoogle(action.context)
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _state.update { it.copy(isSignInButtonLoading = true) }

            val signUpResult = withContext(Dispatchers.IO) {
                repositoryAuth.signIn(
                    _state.value.textFieldEmail,
                    _state.value.textFieldPassword
                )
            }

            when (signUpResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Sign In Successful")
                    _state.update { it.copy(
                        textFieldEmail = "",
                        textFieldPassword = ""
                    ) }
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(signUpResult.message)
                }
            }

            _state.update { it.copy(isSignInButtonLoading = false) }
        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            val signInWithGoogleResult = repositoryAuth.signInWithGoogle(context)

            when (signInWithGoogleResult) {
                FirebaseResponse.Success -> {
                    _effect.emit("Sign In With Google Successful")
                }
                is FirebaseResponse.Failed -> {
                    _effect.emit(signInWithGoogleResult.message)
                }
            }
        }
    }

}