@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.xliiicxiv.tensor.action.ActionMessage
import com.xliiicxiv.tensor.navigation.RoutePage
import com.xliiicxiv.tensor.state.StateMessage
import com.xliiicxiv.tensor.template.CustomIconButton
import com.xliiicxiv.tensor.template.CustomMessageList
import com.xliiicxiv.tensor.template.CustomTextContent
import com.xliiicxiv.tensor.template.CustomTextTitle
import com.xliiicxiv.tensor.template.HorizontalSpacer
import com.xliiicxiv.tensor.template.MessageItemList
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelMessage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageMessageCore(
    backStack: NavBackStack<NavKey>,
    snackBarHostState: SnackbarHostState,
    viewModel: ViewModelMessage = koinViewModel()
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
    state: StateMessage,
    onAction: (ActionMessage) -> Unit
) {
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection)
            .imePadding(),
        contentWindowInsets = WindowInsets(bottom = 0.dp),
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
    state: StateMessage,
    onAction: (ActionMessage) -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = "Message") },
        actions = {
            CustomIconButton(
                icon = Icons.Rounded.Add,
                onClick = { backStack.add(RoutePage.PageAddMessage) }
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateMessage,
    onAction: (ActionMessage) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(generalPadding)
    ) {
        if (state.messagePerson.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
            ) {
                items(
                    items = state.messagePerson,
                ) { messageList ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        onClick = { backStack.add(RoutePage.PagePrivateMessage(messageList.messageId!!)) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.Black)
                            ) {}
                            HorizontalSpacer()
                            Column() {
                                CustomTextTitle(text = "User")
                                CustomTextContent(text = messageList.lastMessage ?: "")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.5.dp))
                }
            }
        } else {
            Text(text = "No Message Available")
        }
    }
}
