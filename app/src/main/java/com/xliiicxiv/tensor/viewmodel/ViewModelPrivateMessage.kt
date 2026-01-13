package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionPrivateMessage
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryMessage
import com.xliiicxiv.tensor.state.StatePrivateMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelPrivateMessage(
    private val repositoryMessage: RepositoryMessage
): ViewModel() {

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage = _snackBarMessage.asSharedFlow()

    private val _state = MutableStateFlow(StatePrivateMessage())
    val state = _state.asStateFlow()

    fun onAction(action: ActionPrivateMessage) {
        when (action) {
            is ActionPrivateMessage.GetMessageId -> {
                _state.update { it.copy(messageId = action.messageId) }
            }
            is ActionPrivateMessage.LoadMessageBubble -> {
                loadMessageBubble(action.messageId)
            }
            is ActionPrivateMessage.TextFieldMessageText -> {
                _state.update { it.copy(textFieldMessageText = action.messageText) }
            }
            ActionPrivateMessage.SendMessage -> {
                sendMessage()
            }
        }
    }

    private fun loadMessageBubble(messageId: String) {
        viewModelScope.launch {
            repositoryMessage.getMessageBubble(messageId).collect { messageBubblesList ->
                _state.update { it.copy(messageBubbleList = messageBubblesList) }
            }
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val sendMessageResult = repositoryMessage.sendMessageBubble(
                messageId = state.value.messageId,
                messageText = state.value.textFieldMessageText
            )

            when (sendMessageResult) {
                FirebaseResponse.Success -> {
                    _state.update { it.copy(textFieldMessageText = "") }
                }
                is FirebaseResponse.Failed -> {
                    _snackBarMessage.emit(sendMessageResult.message)
                }
            }
        }
    }

}