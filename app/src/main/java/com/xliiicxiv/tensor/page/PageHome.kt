@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.xliiicxiv.tensor.navigation.RoutePage
import com.xliiicxiv.tensor.state.StateHome
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelHome
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageHomeCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelHome = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Scaffold(
        backStack = backStack,
        pagerState = pagerState,
        state = state,
        onAction = onAction,
        snackBarHostState = snackBarHostState
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collect { snackBarMessage ->
            snackBarHostState.showSnackbar(
                message = snackBarMessage,
                withDismissAction = true
            )
        }
    }
}

@Composable
private fun Scaffold(
    backStack: NavBackStack<NavKey>,
    pagerState: PagerState,
    state: StateHome,
    onAction: (ActionHome) -> Unit,
    snackBarHostState: SnackbarHostState
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
                pagerState = pagerState,
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
                    pagerState = pagerState,
                    state = state,
                    onAction = onAction
                )
            }
        },
        bottomBar = {
            BottomBar(
                pagerState = pagerState
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    pagerState: PagerState,
    backStack: NavBackStack<NavKey>,
    state: StateHome,
    onAction: (ActionHome) -> Unit
) {
    val title = when(pagerState.currentPage) {
        0 -> "Home"
        1 -> "Notification"
        2 -> "Profile"
        else -> "Main"
    }
    LargeTopAppBar(
        title = { Text(text = title) },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    pagerState: PagerState,
    state: StateHome,
    onAction: (ActionHome) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
        ) { index ->
            when (index) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(text = "Home")
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(text = "Notification")
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) { }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    data class NavigationBarItemList(
        val title: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    )
    val navigationBarItemList = listOf(
        NavigationBarItemList(
            title = "Home",
            icon = Icons.Rounded.Home,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        ),
        NavigationBarItemList(
            title = "Notification",
            icon = Icons.Rounded.Notifications,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        ),
        NavigationBarItemList(
            title = "Profile",
            icon = Icons.Rounded.Person,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(2)
                }
            }
        )
    )
    NavigationBar(
        content = {
            navigationBarItemList.forEachIndexed { index, navigation ->
                NavigationBarItem(
                    selected = false,
                    onClick = { navigation.onClick.invoke() },
                    icon = {
                        Icon(
                            imageVector = navigation.icon,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val snackBarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val backStack = rememberNavBackStack()

    Scaffold(
        backStack = backStack,
        pagerState = pagerState,
        state = StateHome(),
        onAction = {},
        snackBarHostState = snackBarHostState
    )
}