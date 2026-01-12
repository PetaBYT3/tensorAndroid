package com.xliiicxiv.tensor.action

sealed interface ActionSearch {

    data class TextFieldSearch(val searchText: String): ActionSearch

}