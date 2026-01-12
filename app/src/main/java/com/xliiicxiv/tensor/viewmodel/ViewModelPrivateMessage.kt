package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import com.xliiicxiv.tensor.action.ActionPrivateMessage
import com.xliiicxiv.tensor.state.StatePrivateMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.getScopeId

class ViewModelPrivateMessage(

): ViewModel() {

    private val _state = MutableStateFlow(StatePrivateMessage())
    val state = _state.asStateFlow()

    fun onAction(action: ActionPrivateMessage) {
        when (action) {
            is ActionPrivateMessage.GetMessageId -> {
                _state.update { it.copy(messageId = action.messageId) }
            }
        }
    }

}