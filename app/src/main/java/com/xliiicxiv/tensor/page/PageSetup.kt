@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.xliiicxiv.tensor.page

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.action.ActionProfile
import com.xliiicxiv.tensor.action.ActionSetup
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.extension.toImageBitmap
import com.xliiicxiv.tensor.route.RoutePage
import com.xliiicxiv.tensor.state.StateSetup
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomBasicButton
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.CustomOutlinedButton
import com.xliiicxiv.tensor.template.CustomTextField
import com.xliiicxiv.tensor.template.ImageCropDialog
import com.xliiicxiv.tensor.template.PolygonShape
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelSetup
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSetupCore(
    navController: NavController,
    viewModel: ViewModelSetup = koinViewModel()
) {
    val pagerCount = 4
    val pagerState = rememberPagerState(
        pageCount = { pagerCount }
    )
    val snackBarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Scaffold(
        navController = navController,
        pagerState = pagerState,
        state = state,
        onAction = onAction,
        snackBarHostState = snackBarHostState
    )

    LaunchedEffect(Unit) {
        viewModel.movePager.collect { movePager ->
            val nextPage = pagerState.currentPage + movePager
            if (nextPage < pagerState.pageCount) {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { snackBarMessage ->
            snackBarHostState.showSnackbar(
                message = snackBarMessage,
                withDismissAction = true
            )
        }
    }

    if (state.pickedImage != null) {
        ImageCropDialog(
            imageBitmap = state.pickedImage!!,
            onConfirm = { onAction(ActionSetup.CroppedImage(it)) },
            onCancel = { onAction(ActionSetup.PickedImage(null)) }
        )
    }
}

@Composable
private fun Scaffold(
    navController: NavController,
    pagerState: PagerState,
    state: StateSetup,
    onAction: (ActionSetup) -> Unit,
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
                navController = navController,
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
                    navController = navController,
                    pagerState = pagerState,
                    state = state,
                    onAction = onAction
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    pagerState: PagerState,
    navController: NavController,
    state: StateSetup,
    onAction: (ActionSetup) -> Unit
) {
    val title = when (pagerState.currentPage) {
        0 -> "User Name"
        1 -> "Display Name"
        2 -> "Profile Picture"
        3 -> "Finish"
        else -> "Setup"
    }
    LargeTopAppBar(
        title = { Text(text = title) },
        scrollBehavior = scrollBehavior
    )
}

@SuppressLint("FrequentlyChangingValue")
@Composable
private fun Content(
    navController: NavController,
    pagerState: PagerState,
    state: StateSetup,
    onAction: (ActionSetup) -> Unit
) {
    val scrollState = rememberScrollState()

    val currentProgress = remember(
        pagerState.currentPage,
        pagerState.currentPageOffsetFraction
    ) {
        val absolutePosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
        val maxPosition = (4 - 1).coerceAtLeast(1).toFloat()
        (absolutePosition / maxPosition).coerceIn(0f, 1f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState, enabled = true)
    ) {
        LinearWavyProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = generalPadding, end = generalPadding, bottom = generalPadding),
            progress = { currentProgress }
        )
        HorizontalPager(
            modifier = Modifier
                .weight(1f),
            state = pagerState,
            userScrollEnabled = false
        ) { index ->
            when (index) {
                0 -> {
                    PagerUserName(
                        state = state,
                        onAction = onAction
                    )
                }
                1 -> {
                    PagerDisplayName(
                        state = state,
                        onAction = onAction
                    )
                }
                2 -> {
                    PagerProfilePicture(
                        state = state,
                        onAction = onAction,
                        pagerState = pagerState
                    )
                }
                3 -> {
                    PagerFinish(
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun PagerUserName(
    state: StateSetup,
    onAction: (ActionSetup) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = generalPadding)
    ) {
        CustomTextField(
            label = "User Name",
            leadingIcon = Icons.Rounded.AlternateEmail,
            value = state.textFieldUserName,
            onValueChange = { onAction(ActionSetup.TextFieldUserName(it)) }
        )
        VerticalSpacer()
        CustomButton(
            text = "Next",
            isLoading = state.isButtonSetUserNameLoading,
            onClick = { onAction(ActionSetup.SetUserName) }
        )
    }
}

@Composable
private fun PagerDisplayName(
    state: StateSetup,
    onAction: (ActionSetup) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = generalPadding)
    ) {
        CustomTextField(
            label = "Display Name",
            leadingIcon = Icons.Rounded.Person,
            value = state.textFieldDisplayName,
            onValueChange = { onAction(ActionSetup.TextFieldDisplayName(it)) }
        )
        VerticalSpacer()
        CustomButton(
            text = "Next",
            isLoading = state.isButtonDisplayNameLoading,
            onClick = { onAction(ActionSetup.SetDisplayName) }
        )
    }
}

@Composable
private fun PagerProfilePicture(
    pagerState: PagerState,
    state: StateSetup,
    onAction: (ActionSetup) -> Unit
) {
    val scope = rememberCoroutineScope()
    val imagePicker = rememberFilePickerLauncher(
        title = "Pick Image For Profile Picture",
        type = FileKitType.Image,
        mode = FileKitMode.Single,
        onResult = { file: PlatformFile? ->
            onAction(ActionSetup.PickedImage(file))
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = generalPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center),
                shape = PolygonShape(MaterialShapes.Cookie12Sided)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (state.userData?.userProfilePicture != null) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            bitmap = state.userData.userProfilePicture.toImageBitmap()!!,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.Center),
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        VerticalSpacer()
        CustomButton(
            text = "Set Profile Picture",
            isLoading = false,
            onClick = { imagePicker.launch() }
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Next",
            isLoading = false,
            onClick = {
                scope.launch {
                    val nextPage = pagerState.currentPage + 1
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        )
    }
}

@Composable
private fun PagerFinish(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = generalPadding)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "You're All Setup",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLargeEmphasized
        )
        VerticalSpacer()
        CustomButton(
            text = "Finish",
            isLoading = false,
            onClick = { navController.navigate(RoutePage.PageHome) }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val navController = rememberNavController()

//    Scaffold(
//        navController = navController,
//        state = StateSetup(),
//        onAction = {}
//    )

//    PagerUserName(
//        state = StateSetup(),
//        onAction = {}
//    )

//    PagerDisplayName(
//        state = StateSetup(),
//        onAction = {}
//    )

//    PagerProfilePicture(
//        state = StateSetup(),
//        onAction = {}
//    )

    PagerFinish(
        navController = navController
    )

}