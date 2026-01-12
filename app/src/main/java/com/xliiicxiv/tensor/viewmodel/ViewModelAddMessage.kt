package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import com.xliiicxiv.tensor.action.ActionAddMessage
import com.xliiicxiv.tensor.state.StateAddMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelAddMessage(

): ViewModel() {

    private val _state = MutableStateFlow(StateAddMessage())
    val state = _state.asStateFlow()

    fun onAction(action: ActionAddMessage) {
        when (action) {
            is ActionAddMessage.TextFieldSearchUser -> {
                _state.update { it.copy(textFieldSearchUser = action.userName) }
            }
        }
    }

}