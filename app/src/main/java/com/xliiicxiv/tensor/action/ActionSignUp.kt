package com.xliiicxiv.tensor.action

sealed interface ActionSignUp {

    data class TextFieldEmail(val email: String): ActionSignUp
    data class TextFieldPassword(val password: String): ActionSignUp
    data class TextFieldRetypePassword(val retypePassword: String): ActionSignUp
    data object SignUp: ActionSignUp

}