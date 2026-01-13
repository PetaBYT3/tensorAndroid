package com.xliiicxiv.tensor.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.xliiicxiv.tensor.dataclass.DataClassUser
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RepositoryUser {

    private val userCollection = "usersData"

    fun getUserData(): Flow<DataClassUser?> {
        return callbackFlow {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val firestoreListener = fireStore
                    .collection(userCollection)
                    .document(userUid)
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            trySend(null)
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            val userData = snapshot.toObject<DataClassUser>()
                            trySend(userData)
                        } else {
                            trySend(null)
                        }
                    }

                awaitClose { firestoreListener.remove() }
            } else {
                trySend(null)
                close()
            }
        }
    }

    suspend fun getUserDataByUserName(keyWord: String): List<DataClassUser>? {
        return withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()

                val userDataResult = firestore
                    .collection(userCollection)
                    .orderBy("userName")
                    .startAt(keyWord.lowercase())
                    .endAt(keyWord.lowercase() + "\uf8ff")
                    .get()
                    .await()

                userDataResult.toObjects(DataClassUser::class.java)

            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun uploadProfilePicture(
        profilePicture: String?
    ): FirebaseResponse {
        return try {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val setProfilePicture = hashMapOf(
                    "userProfilePicture" to profilePicture
                )
                fireStore
                    .collection(userCollection)
                    .document(userUid)
                    .set(setProfilePicture, SetOptions.merge())
                    .await()
                FirebaseResponse.Success
            } else {
                return FirebaseResponse.Failed("No Session Detected")
            }
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    suspend fun setUserName(
        userName: String
    ): FirebaseResponse {
        return try {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                if (userName.isBlank()) {
                    return FirebaseResponse.Failed("User Name Cannot Be Empty")
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
                        .document(userUid)
                        .set(setUserName, SetOptions.merge())
                    FirebaseResponse.Success
                } else {
                    FirebaseResponse.Failed("User Name Already Exist")
                }
            } else {
                return FirebaseResponse.Failed("Session Not Found")
            }
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

    suspend fun setDisplayName(
        displayName: String
    ): FirebaseResponse {
        return try {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                if (displayName.isBlank()) {
                    return FirebaseResponse.Failed("Display Name Cannot Be Empty")
                }

                val setDisplayName = hashMapOf(
                    "displayName" to displayName
                )
                fireStore
                    .collection(userCollection)
                    .document(userUid)
                    .set(setDisplayName, SetOptions.merge())
                    .await()
                FirebaseResponse.Success
            } else {
                return FirebaseResponse.Failed("Session Not Found")
            }
        } catch (e: Exception) {
            FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
        }
    }

}