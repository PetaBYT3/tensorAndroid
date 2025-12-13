package com.xliiicxiv.tensor.action

sealed interface ActionSignIn {

    data class TextFieldEmail(val email: String): ActionSignIn
    data class TextFieldPassword(val password: String): ActionSignIn
    data object SignIn: ActionSignIn

}