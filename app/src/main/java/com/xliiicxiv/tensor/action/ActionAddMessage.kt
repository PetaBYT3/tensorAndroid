package com.xliiicxiv.tensor.action

sealed interface ActionAddMessage {

    data class TextFieldSearchUser(val userName: String): ActionAddMessage

}