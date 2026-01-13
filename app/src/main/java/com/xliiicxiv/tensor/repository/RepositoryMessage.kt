package com.xliiicxiv.tensor.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.xliiicxiv.tensor.dataclass.DataClassMessage
import com.xliiicxiv.tensor.dataclass.DataClassMessageBubble
import com.xliiicxiv.tensor.extension.capitalizeEachWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RepositoryMessage {

    private val messageCollection = "messageData"

    suspend fun addMessage(
        currentUid: String,
        targetUid: String
    ): FirebaseResponse {
        return withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()

                val checkMessageExistence = firestore
                    .collection(messageCollection)
                    .whereArrayContains("messageParticipant", currentUid)
                    .limit(1)
                    .get()
                    .await()

                if (checkMessageExistence.isEmpty) {
                    val messageId = firestore
                        .collection(messageCollection)
                        .document()
                        .id

                    val messageData = hashMapOf(
                        "messageId" to messageId,
                        "messageParticipant" to listOf(
                            currentUid, targetUid
                        )
                    )

                    firestore
                        .collection(messageCollection)
                        .document(messageId)
                        .set(messageData, SetOptions.merge())
                        .await()

                    FirebaseResponse.Success
                } else {
                    FirebaseResponse.Failed("You Already Have Message Room With This User")
                }
            } catch (e: Exception) {
                FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
            }
        }
    }

    fun getMessage(): Flow<List<DataClassMessage>> {
        return callbackFlow {
            val fireStore = FirebaseFirestore.getInstance()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val fireStoreListener = fireStore
                    .collection(messageCollection)
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

    fun getMessageBubble(
        messageId: String
    ): Flow<List<DataClassMessageBubble>> {
        return callbackFlow {
            try {
                val firestore = FirebaseFirestore.getInstance()

                val firestoreListener = firestore
                    .collection(messageCollection)
                    .document(messageId)
                    .collection("messageBubble")
                    .orderBy("timeStamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val messageBubbleList = snapshot.documents.mapNotNull { documentSnapshot ->
                                documentSnapshot.toObject(DataClassMessageBubble::class.java)
                            }
                            trySend(messageBubbleList)
                        }
                    }

                awaitClose { firestoreListener.remove() }
            } catch (e: Exception) {
                trySend(emptyList())
                close()
                return@callbackFlow
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessageBubble(
        messageId: String,
        messageText: String
    ): FirebaseResponse {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                val firestore = FirebaseFirestore.getInstance()
                val messageBubbleId = firestore
                    .collection(messageCollection)
                    .document(messageId)
                    .collection("messageBubble")
                    .document()
                    .id

                val messageBubbleData = DataClassMessageBubble(
                    messageId = messageBubbleId,
                    messageOwner = currentUser,
                    image = null,
                    message = messageText,
                    timeStamp = null
                )

                firestore
                    .collection(messageCollection)
                    .document(messageId)
                    .collection("messageBubble")
                    .document(messageBubbleId)
                    .set(messageBubbleData)
                    .await()
                FirebaseResponse.Success
            } catch (e: Exception) {
                FirebaseResponse.Failed(e.message.toString().capitalizeEachWord())
            }
        }
    }

}