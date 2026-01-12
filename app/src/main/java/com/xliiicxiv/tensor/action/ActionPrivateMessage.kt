package com.xliiicxiv.tensor.action

sealed interface ActionPrivateMessage {

    data class GetMessageId(val messageId: String): ActionPrivateMessage

}