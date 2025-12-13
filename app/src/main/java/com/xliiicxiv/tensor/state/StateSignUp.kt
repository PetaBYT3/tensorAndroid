package com.xliiicxiv.tensor.state

data class StateSignUp(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",
    val textFieldRetypePassword: String = "",

    val isSignUpButtonLoading: Boolean = false
)
