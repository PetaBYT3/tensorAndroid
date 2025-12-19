package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import com.xliiicxiv.tensor.action.ActionHome
import com.xliiicxiv.tensor.state.StateHome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewModelHome(

): ViewModel() {

    private val _state = MutableStateFlow(StateHome())
    val state = _state.asStateFlow()

    fun onAction(action: ActionHome) {
        when (action) {
            else -> {}
        }
    }

}