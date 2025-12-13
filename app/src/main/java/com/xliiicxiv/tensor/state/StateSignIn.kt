package com.xliiicxiv.tensor.state

data class StateSignIn(
    val textFieldEmail: String = "",
    val textFieldPassword: String = "",

    val isSignInButtonLoading: Boolean = false
)
