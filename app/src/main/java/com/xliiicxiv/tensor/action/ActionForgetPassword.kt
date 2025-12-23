package com.xliiicxiv.tensor.action

sealed interface ActionForgetPassword {

    data class TextFieldEmail(val email: String): ActionForgetPassword

    data object SendPasswordResetEmail: ActionForgetPassword

}