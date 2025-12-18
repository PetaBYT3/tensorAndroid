package com.xliiicxiv.tensor.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.xliiicxiv.tensor.dataclass.DataClassUser

sealed class FirebaseResponse {
    data object Success: FirebaseResponse()
    data class Failed(val message: String): FirebaseResponse()
}