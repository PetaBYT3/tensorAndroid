package com.xliiicxiv.tensor.action

import android.content.Context

sealed interface ActionSignIn {

    data class TextFieldEmail(val email: String): ActionSignIn
    data class TextFieldPassword(val password: String): ActionSignIn
    data object SignIn: ActionSignIn

    data class SignInWithGoogle(val context: Context): ActionSignIn

}