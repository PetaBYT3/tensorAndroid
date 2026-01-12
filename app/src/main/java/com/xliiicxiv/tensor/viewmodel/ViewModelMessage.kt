package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionMessage
import com.xliiicxiv.tensor.repository.RepositoryMessage
import com.xliiicxiv.tensor.state.StateMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelMessage(
    private val repositoryMessage: RepositoryMessage
): ViewModel() {

    init {
        viewModelScope.launch {
            repositoryMessage.getMessage().collect { messageData ->
                _state.update { it.copy(messagePerson = messageData) }
            }
        }
    }

    private val _state = MutableStateFlow(StateMessage())
    val state = _state.asStateFlow()

    fun onAction(action: ActionMessage) {
        when (action) {
            else -> {}
        }
    }

}