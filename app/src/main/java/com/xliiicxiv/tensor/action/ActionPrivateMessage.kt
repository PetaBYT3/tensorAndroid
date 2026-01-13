package com.xliiicxiv.tensor.action

sealed interface ActionPrivateMessage {

    data class GetMessageId(val messageId: String): ActionPrivateMessage

    data class LoadMessageBubble(val messageId: String): ActionPrivateMessage

    data class TextFieldMessageText(val messageText: String): ActionPrivateMessage
    data object SendMessage: ActionPrivateMessage

}