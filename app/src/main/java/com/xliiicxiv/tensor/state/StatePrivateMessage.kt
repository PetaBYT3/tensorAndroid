package com.xliiicxiv.tensor.state

import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.dataclass.DataClassMessageBubble

data class StatePrivateMessage(

    val currentUser: String = FirebaseAuth.getInstance().uid.toString(),
    val messageId: String = "",

    val messageBubbleList: List<DataClassMessageBubble> = emptyList(),
    val textFieldMessageText: String = ""

)