package com.xliiicxiv.tensor.dataclass

data class DataClassMessage(

    val messageParticipant: List<String> = emptyList(),
    val lastMessage: String? = null,
    val messageList: List<DataClassMessageList> = emptyList()

)
