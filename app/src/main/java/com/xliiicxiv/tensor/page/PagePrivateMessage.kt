@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.action.ActionPrivateMessage
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.state.StatePrivateMessage
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.CustomIconButton
import com.xliiicxiv.tensor.template.CustomTextField
import com.xliiicxiv.tensor.template.HorizontalSpacer
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelPrivateMessage
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PagePrivateMessageCore(
    messageId: String,
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelPrivateMessage = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    LaunchedEffect(messageId) {
        onAction(ActionPrivateMessage.GetMessageId(messageId))
    }

    LaunchedEffect(messageId) {
        onAction(ActionPrivateMessage.LoadMessageBubble(messageId))
    }

    Scaffold(
        backStack = backStack,
        state = state,
        onAction = onAction
    )
}

@Composable
private fun Scaffold(
    backStack: NavBackStack<NavKey>,
    state: StatePrivateMessage,
    onAction: (ActionPrivateMessage) -> Unit
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
        },
        bottomBar = {
            BottomBar(
                state = state,
                onAction = onAction
            )
        }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    backStack: NavBackStack<NavKey>,
    state: StatePrivateMessage,
    onAction: (ActionPrivateMessage) -> Unit
) {
    LargeTopAppBar(
        navigationIcon = {
            CustomIconButton(
                icon = Icons.Rounded.ArrowBackIos,
                onClick = { backStack.removeAt(backStack.lastIndex) }
            )
        },
        title = { Text(text = state.messageId) },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StatePrivateMessage,
    onAction: (ActionPrivateMessage) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = generalPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(
                items = state.messageBubbleList
            ) { messageBubble ->
                if (messageBubble.messageOwner != state.currentUser) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card() {
                            Text(
                                modifier = Modifier
                                    .padding(generalPadding),
                                text = messageBubble.message!!
                            )
                        }
                    }
                    HorizontalSpacer()
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Card() {
                            Text(
                                modifier = Modifier
                                    .padding(generalPadding),
                                text = messageBubble.message!!
                            )
                        }
                    }
                    HorizontalSpacer()
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    state: StatePrivateMessage,
    onAction: (ActionPrivateMessage) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(generalPadding)
        ) {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Message",
                leadingIcon = Icons.Rounded.Message,
                value = state.textFieldMessageText,
                onValueChange = { onAction(ActionPrivateMessage.TextFieldMessageText(it)) }
            )
            VerticalSpacer()
            Row() {
                Spacer(modifier = Modifier.weight(1f))
                CustomIconButton(
                    icon = Icons.Rounded.Image,
                    onClick = {}
                )
                HorizontalSpacer()
                CustomIconButton(
                    icon = Icons.Rounded.Send,
                    onClick = { onAction(ActionPrivateMessage.SendMessage) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val backStack = rememberNavBackStack()

    Scaffold(
        backStack = backStack,
        state = StatePrivateMessage(),
        onAction = {}
    )
}