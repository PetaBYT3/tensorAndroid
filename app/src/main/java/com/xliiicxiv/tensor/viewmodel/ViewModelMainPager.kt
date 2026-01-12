package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionMainPager
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateMainPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelMainPager(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _effect = MutableSharedFlow<String>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow(StateMainPager())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val getUserAuth = withContext(Dispatchers.IO) {
                repositoryAuth.getUserAuth()
            }

            getUserAuth.collect { userAuth ->
                _state.update { it.copy(firebaseAuth = userAuth) }
            }
        }
    }

    fun onAction(action: ActionMainPager) {
        when (action) {
            ActionMainPager.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            val signOutResult = repositoryAuth.signOut()
            _effect.emit(signOutResult)
        }
    }

}