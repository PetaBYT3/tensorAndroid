package com.xliiicxiv.tensor.dataclass

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class DataClassMessageBubble(

    val messageId: String? = null,
    val messageOwner: String? = null,
    val image: String? = null,
    val message: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null

)
