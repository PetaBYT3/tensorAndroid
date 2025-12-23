package com.xliiicxiv.tensor.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RepositoryAuth {

    fun getUserAuth(): Flow<FirebaseUser?> {
        return callbackFlow {
            val firebaseAuth = FirebaseAuth.getInstance()

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
            val firebaseAuth = FirebaseAuth.getInstance()

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

    suspend fun signInWithGoogle(
        context: Context
    ): FirebaseResponse {
        return try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("89927130220-f9d74d665om3402r71qdt7t6omskl7sa.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential).await()

                if (firebaseAuth.currentUser != null) {
                    FirebaseResponse.Success
                } else {
                    FirebaseResponse.Failed("Sign In Failed")
                }
            } else {
                FirebaseResponse.Failed("Something Went Wrong With Credentials")
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
            val firebaseAuth = FirebaseAuth.getInstance()

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

    suspend fun sendPasswordResetEmail(
        email: String
    ): FirebaseResponse {
        return try {
            val firebaseAuth = FirebaseAuth.getInstance()

            firebaseAuth.sendPasswordResetEmail(email).await()
            FirebaseResponse.Success
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    fun signOut(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        return "Sign Out Successful"
    }

}