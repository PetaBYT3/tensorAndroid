@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.action.ActionHome
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.state.StateHome
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelHome
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageHomeCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelHome = koinViewModel()
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
    state: StateHome,
    onAction: (ActionHome) -> Unit
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
    state: StateHome,
    onAction: (ActionHome) -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = "Template") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateHome,
    onAction: (ActionHome) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState, enabled = true)
            .padding(horizontal = generalPadding)
    ) {
        Text(text = state.firebaseAuth?.uid ?: "No User ID")
        CustomButton(
            text = "Sign Out",
            isLoading = false,
            onClick = { FirebaseAuth.getInstance().signOut() }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val backStack = rememberNavBackStack()

    Scaffold(
        backStack = backStack,
        state = StateHome(),
        onAction = {}
    )
}