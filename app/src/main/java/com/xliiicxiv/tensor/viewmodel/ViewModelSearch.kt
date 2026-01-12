package com.xliiicxiv.tensor.viewmodel

import androidx.lifecycle.ViewModel
import com.xliiicxiv.tensor.action.ActionSearch
import com.xliiicxiv.tensor.state.StateSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModelSearch(

): ViewModel() {

    private val _state = MutableStateFlow(StateSearch())
    val state = _state.asStateFlow()

    fun onAction(action: ActionSearch) {
        when (action) {
            is ActionSearch.TextFieldSearch -> {
                _state.update { it.copy(textFieldSearch = action.searchText) }
            }
        }
    }

}