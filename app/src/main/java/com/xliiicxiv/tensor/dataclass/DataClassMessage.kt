package com.xliiicxiv.tensor.dataclass

data class DataClassMessage(

    val messageId: String? = null,
    val messageParticipant: List<String> = emptyList(),
    val profilePicture: String? = null,
    val displayName: String? = null,
    val lastMessage: String? = null,

)
