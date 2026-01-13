package com.xliiicxiv.tensor.action

sealed interface ActionAddMessage {

    data class TextFieldSearchUser(val userName: String): ActionAddMessage
    data object SearchUserByUserName: ActionAddMessage
    data class CreateNewMessage(val messageParticipant: String): ActionAddMessage

}