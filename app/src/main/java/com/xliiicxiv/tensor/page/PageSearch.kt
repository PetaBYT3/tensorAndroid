@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.xliiicxiv.tensor.action.ActionMessage
import com.xliiicxiv.tensor.action.ActionSearch
import com.xliiicxiv.tensor.state.StateMessage
import com.xliiicxiv.tensor.state.StateSearch
import com.xliiicxiv.tensor.template.CustomTextContent
import com.xliiicxiv.tensor.template.CustomTextField
import com.xliiicxiv.tensor.template.CustomTextTitle
import com.xliiicxiv.tensor.template.HorizontalSpacer
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelSearch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSearchCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelSearch = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Content(
        backStack = backStack,
        state = state,
        onAction = onAction
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateSearch,
    onAction: (ActionSearch) -> Unit
) {
    val searchBarState = rememberSearchBarState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(generalPadding)
    ) {
        SearchBar(
            inputField = {
                CustomTextField(
                    label = "",
                    leadingIcon = Icons.Rounded.Search,
                    value = "",
                    onValueChange = {}
                )
            },
            state = searchBarState
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    val backStack = rememberNavBackStack()
    val snackBarHostState = remember { SnackbarHostState() }

    Content(
        backStack = backStack,
        state = StateSearch(),
        onAction = {}
    )
}

