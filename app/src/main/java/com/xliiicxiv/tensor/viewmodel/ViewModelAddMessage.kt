package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionAddMessage
import com.xliiicxiv.tensor.repository.FirebaseResponse
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryMessage
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.state.StateAddMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelAddMessage(
    private val repositoryUser: RepositoryUser,
    private val repositoryMessage: RepositoryMessage,
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage = _snackBarMessage.asSharedFlow()

    private val _state = MutableStateFlow(StateAddMessage())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryAuth.getUserAuth().collect { userAuth ->
                _state.update { it.copy(currentUser = userAuth) }
            }
        }
    }

    fun onAction(action: ActionAddMessage) {
        when (action) {
            is ActionAddMessage.TextFieldSearchUser -> {
                _state.update { it.copy(textFieldSearchUser = action.userName) }
            }
            ActionAddMessage.SearchUserByUserName -> {
                searchUserByUserName()
            }
            is ActionAddMessage.CreateNewMessage -> {
                createNewMessage(action.messageParticipant)
            }
        }
    }

    private fun searchUserByUserName() {
        viewModelScope.launch {
            val searchUserResult = repositoryUser.getUserDataByUserName(_state.value.textFieldSearchUser)
            _state.update { it.copy(searchUserResult = searchUserResult) }
        }
    }

    private fun createNewMessage(messageParticipant: String) {
        viewModelScope.launch {
            if (_state.value.currentUser != null) {
                val currentUser = _state.value.currentUser?.uid.toString()
                val createNewMessageResult = repositoryMessage.addMessage(
                    currentUid = currentUser,
                    targetUid = messageParticipant
                )

                when (createNewMessageResult) {
                    FirebaseResponse.Success -> {
                        _snackBarMessage.emit("Message Created")
                    }
                    is FirebaseResponse.Failed -> {
                        _snackBarMessage.emit(createNewMessageResult.message)
                    }
                }
            }
        }
    }

}