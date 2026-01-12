@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.xliiicxiv.tensor.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xliiicxiv.tensor.R
import com.xliiicxiv.tensor.action.ActionSignIn
import com.xliiicxiv.tensor.navigation.RoutePage
import com.xliiicxiv.tensor.state.StateSignIn
import com.xliiicxiv.tensor.template.CustomButton
import com.xliiicxiv.tensor.template.CustomOutlinedButton
import com.xliiicxiv.tensor.template.CustomTextField
import com.xliiicxiv.tensor.template.CustomTextFieldPassword
import com.xliiicxiv.tensor.template.VerticalSpacer
import com.xliiicxiv.tensor.template.generalPadding
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PageSignInCore(
    backStack: NavBackStack<NavKey>,
    viewModel: ViewModelSignIn = koinViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Scaffold(
        backStack = backStack,
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
    state: StateSignIn,
    onAction: (ActionSignIn) -> Unit,
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
        snackbarHost = { SnackbarHost( snackBarHostState ) }
    )
}

@Composable
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    backStack: NavBackStack<NavKey>,
    state: StateSignIn,
    onAction: (ActionSignIn) -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = "Sign In") },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun Content(
    backStack: NavBackStack<NavKey>,
    state: StateSignIn,
    onAction: (ActionSignIn) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState, enabled = true)
            .padding(generalPadding)
    ) {
        CustomTextField(
            label = "E-Mail",
            leadingIcon = Icons.Rounded.Mail,
            value = state.textFieldEmail,
            onValueChange = { onAction(ActionSignIn.TextFieldEmail(it)) }
        )
        VerticalSpacer()
        CustomTextFieldPassword(
            label = "Password",
            value = state.textFieldPassword,
            onValueChange = { onAction(ActionSignIn.TextFieldPassword(it)) }
        )
        VerticalSpacer()
        CustomButton(
            text = "Sign In",
            isLoading = state.isSignInButtonLoading,
            onClick = { onAction(ActionSignIn.SignIn) }
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Forget Password",
            isLoading = false,
            onClick = { backStack.add(RoutePage.PageForgetPassword) },
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Sign Up",
            isLoading = false,
            onClick = { backStack.add(RoutePage.PageSignUp) },
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Continue With Google",
            isLoading = false,
            onClick = { onAction(ActionSignIn.SignInWithGoogle(context)) }
        )
        VerticalSpacer()
        CustomOutlinedButton(
            text = "Continue With Facebook",
            isLoading = false,
            onClick = {  }
        )
        val geminiComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gemini_ai))
        val geminiProgress by animateLottieCompositionAsState(
            composition = geminiComposition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            composition = geminiComposition,
            progress = { geminiProgress }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {

    val backStack = rememberNavBackStack()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        backStack = backStack,
        state = StateSignIn(),
        onAction = {},
        snackBarHostState = snackBarHostState
    )
}