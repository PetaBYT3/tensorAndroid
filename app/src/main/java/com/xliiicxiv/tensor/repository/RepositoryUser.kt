package com.xliiicxiv.tensor.repository

import android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.xliiicxiv.tensor.dataclass.DataClassUser
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RepositoryUser {

    private val fireStore = FirebaseFirestore.getInstance()
    private val userUid = FirebaseAuth.getInstance().currentUser?.uid
    private val userCollection = "usersData"

    fun getUserData(): Flow<DataClassUser?> {
        return callbackFlow {
            if (userUid == null) {
                trySend(null)
            }

            val firestoreListener = fireStore
                .collection(userCollection)
                .document(userUid!!)
                .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userData = snapshot.toObject<DataClassUser>()
                    trySend(userData)
                }
            }

            awaitClose { firestoreListener.remove() }
        }
    }

    suspend fun uploadProfilePicture(
        profilePicture: String?
    ): FirebaseResponse {
        return try {
            val setProfilePicture = hashMapOf(
                "userProfilePicture" to profilePicture
            )

            fireStore
                .collection(userCollection)
                .document(userUid!!)
                .set(setProfilePicture, SetOptions.merge())
                .await()
            FirebaseResponse.Success

        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    suspend fun setUserName(
        userName: String
    ): FirebaseResponse {
        return try {
            if (userUid == null) {
                FirebaseResponse.Failed("Session Not Found")
            }

            val checkUserName = fireStore
                .collection(userCollection)
                .whereEqualTo("userName", userName.trim().lowercase())
                .limit(1)
                .get()
                .await()

            if (checkUserName.isEmpty) {

                val setUserName = hashMapOf(
                    "userName" to userName
                )

                fireStore
                    .collection(userCollection)
                    .document(userUid!!)
                    .set(setUserName, SetOptions.merge())
                FirebaseResponse.Success

            } else {
                FirebaseResponse.Failed("User Name Already Exist")
            }
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    suspend fun setDisplayName(
        displayName: String
    ): FirebaseResponse {
        return try {
            if (userUid == null) {
                FirebaseResponse.Failed("Session Not Found")
            }

            val setDisplayName = hashMapOf(
                "displayName" to displayName
            )

            fireStore
                .collection(userCollection)
                .document(userUid!!)
                .set(setDisplayName, SetOptions.merge())
                .await()
            FirebaseResponse.Success
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

}