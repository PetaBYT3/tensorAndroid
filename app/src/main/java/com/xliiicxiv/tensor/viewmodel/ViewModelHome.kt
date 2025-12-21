package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.action.ActionHome
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelHome(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _state = MutableStateFlow(StateHome())
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

    fun onAction(action: ActionHome) {
        when (action) {
            else -> {}
        }
    }

}