package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import com.xliiicxiv.tensor.action.ActionProfile
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.state.StateProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelProfile(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _state = MutableStateFlow(StateProfile())
    val state = _state.asStateFlow()

    fun onAction(action: ActionProfile) {
        when (action) {
            ActionProfile.SignOut -> {
                repositoryAuth.signOut()
            }
        }
    }

}