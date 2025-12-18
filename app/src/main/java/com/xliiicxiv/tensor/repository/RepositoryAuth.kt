package com.xliiicxiv.tensor.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RepositoryAuth {

    private val firebaseAuth = FirebaseAuth.getInstance()

    val currentUid: String? get() = firebaseAuth.uid

    fun getCurrentUser(): Flow<FirebaseUser?> {
        return callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                trySend(firebaseAuth.currentUser)
            }

            firebaseAuth.addAuthStateListener(authStateListener)

            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): FirebaseResponse {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return FirebaseResponse.Failed("Please Fill All Text Field")
            }

            val signInResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (signInResult.user != null) {
                FirebaseResponse.Success
            } else {
                FirebaseResponse.Failed("Email Or Password Incorrect")
            }

        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        retypePassword: String
    ): FirebaseResponse {
        return try {
            if (email.isBlank() || password.isBlank() || retypePassword.isBlank()) {
                return FirebaseResponse.Failed("Please Fill All Text Field")
            }

            if (password != retypePassword) {
                return FirebaseResponse.Failed("Password Not Match")
            }

            val signUpResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if (signUpResult.user != null) {
                FirebaseResponse.Success
            } else {
                FirebaseResponse.Failed("Fail To Create Account")
            }

        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

}