package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.route.RoutePage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewModelMain(
    private val repositoryAuth: RepositoryAuth
): ViewModel() {

    private val _effect = MutableSharedFlow<RoutePage>(replay = 1)
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(2_500)
            repositoryAuth.getCurrentUser().collect { firebaseUser ->
                if (firebaseUser != null) {
                    _effect.emit(RoutePage.PageTemplate)
                } else {
                    _effect.emit(RoutePage.PageSignIn)
                }
            }
        }
    }

}