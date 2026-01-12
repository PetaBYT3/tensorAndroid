package com.xliiicxiv.tensor.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xliiicxiv.tensor.dataclass.DataClassMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class RepositoryMessage {

    private val chatMessageCollection = "chatMessage"

    fun getMessage(): Flow<List<DataClassMessage>> {
        return callbackFlow {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val fireStoreListener = fireStore
                    .collection(chatMessageCollection)
                    .whereArrayContains("messageParticipant", userUid)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val messageData = snapshot.documents.mapNotNull { documentSnapshot ->
                                documentSnapshot.toObject(DataClassMessage::class.java)
                            }

                            trySend(messageData)
                        }
                    }

                awaitClose { fireStoreListener.remove() }
            } else {
                trySend(emptyList())
                close()
                return@callbackFlow
            }
        }.flowOn(Dispatchers.IO)
    }

}