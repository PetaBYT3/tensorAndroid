@file:OptIn(ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.xliiicxiv.tensor.action.ActionMainPager
import com.xliiicxiv.tensor.state.StateMainPager
import com.xliiicxiv.tensor.viewmodel.ViewModelMainPager
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageMainPagerCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelMainPager = koinViewModel()
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
    state: StateMainPager,
    onAction: (ActionMainPager) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier
            .imePadding(),
        contentWindowInsets = WindowInsets(top = 0.dp),
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
                    onAction = onAction,
                    snackBarHostState = snackBarHostState
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
private fun Content(
    backStack: NavBackStack<NavKey>,
    pagerState: PagerState,
    state: StateMainPager,
    onAction: (ActionMainPager) -> Unit,
    snackBarHostState: SnackbarHostState
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
                        PageHomeCore(
                            backStack = backStack,
                            snackBarHostState = snackBarHostState
                        )
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PageMessageCore(
                            backStack = backStack,
                            snackBarHostState = snackBarHostState
                        )
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PageProfileCore(
                            backStack = backStack,
                            snackBarHostState = snackBarHostState
                        )
                    }
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
            title = "Message",
            icon = Icons.Rounded.Message,
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
                    selected = pagerState.currentPage == index,
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
        state = StateMainPager(),
        onAction = {},
        snackBarHostState = snackBarHostState
    )
}