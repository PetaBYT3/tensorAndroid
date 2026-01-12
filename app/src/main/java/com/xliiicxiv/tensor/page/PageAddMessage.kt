@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.action.ActionAddMessage
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.state.StateAddMessage
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.CustomIconButton
import com.xliiicxiv.tensor.template.CustomTextField
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelAddMessage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageAddMessageCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelAddMessage = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Scaffold(
        backStack = backStack,
        state = state,
        onAction = onAction
    )

}

@Composable
private fun Scaffold(
    backStack: NavBackStack<NavKey>,
    state: StateAddMessage,
    onAction: (ActionAddMessage) -> Unit
) {
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection)
            .imePadding(),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehaviour,
                backStack = backStack,
                state = state,
                onAction = onAction
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Content(
                    backStack = backStack,
                    state = state,
                    onAction = onAction
                )
            }
        }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    backStack: NavBackStack<NavKey>,
    state: StateAddMessage,
    onAction: (ActionAddMessage) -> Unit
) {
    LargeTopAppBar(
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBackIos,
                onClick = { backStack.removeAt(backStack.lastIndex) }
            )
        },
        title = { Text(text = "Create New Message") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateAddMessage,
    onAction: (ActionAddMessage) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState, enabled = true)
            .padding(generalPadding)
    ) {
        CustomTextField(
            label = "Search User",
            leadingIcon = Icons.Rounded.Search,
            value = state.textFieldSearchUser,
            onValueChange = { onAction(ActionAddMessage.TextFieldSearchUser(it)) }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val backStack = rememberNavBackStack()

    Scaffold(
        backStack = backStack,
        state = StateAddMessage(),
        onAction = {}
    )
}