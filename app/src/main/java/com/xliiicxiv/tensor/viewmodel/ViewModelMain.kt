package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.xliiicxiv.tensor.navigation.RoutePage
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.state.StateMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelMain(
    private val repositoryAuth: RepositoryAuth,
    private val repositoryUser: RepositoryUser
): ViewModel() {

    private val _state = MutableStateFlow(StateMain())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val getUserAuth = withContext(Dispatchers.IO) {
                repositoryAuth.getUserAuth()
            }

            getUserAuth.collect { userAuth ->
                _state.update {
                    if (userAuth != null) {
                        it.copy(isLoggedIn = true)
                    } else {
                        it.copy(isLoggedIn = false)
                    }
                }
            }
        }

        viewModelScope.launch {
            val getUserData = withContext(Dispatchers.IO) {
                repositoryUser.getUserData()
            }

            getUserData.collect { userData ->
                _state.update {
                    if (userData?.userName != null && userData.displayName != null) {
                        it.copy(isSetupDone = true)
                    } else {
                        it.copy(isSetupDone = false)
                    }
                }
            }
        }
    }

}