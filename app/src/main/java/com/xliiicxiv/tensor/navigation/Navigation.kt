package com.xliiicxiv.tensor.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.xliiicxiv.tensor.page.PageForgetPasswordCore
import com.xliiicxiv.tensor.page.PageHomeCore
import com.xliiicxiv.tensor.page.PageProfileCore
import com.xliiicxiv.tensor.page.PageSetupCore
import com.xliiicxiv.tensor.page.PageSignInCore
import com.xliiicxiv.tensor.page.PageSignUpCore
import com.xliiicxiv.tensor.page.PageSplashCore
import com.xliiicxiv.tensor.viewmodel.ViewModelMain
import kotlinx.coroutines.delay
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationCore(
    modifier: Modifier = Modifier,
    viewModel: ViewModelMain = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(RoutePage.PageSplash::class, RoutePage.PageSplash.serializer())
                    subclass(RoutePage.PageSignIn::class, RoutePage.PageSignIn.serializer())
                    subclass(RoutePage.PageSignUp::class, RoutePage.PageSignUp.serializer())
                    subclass(RoutePage.PageForgetPassword::class, RoutePage.PageForgetPassword.serializer())
                    subclass(RoutePage.PageSetup::class, RoutePage.PageSetup.serializer())
                    subclass(RoutePage.PageHome::class, RoutePage.PageHome.serializer())
                    subclass(RoutePage.PageProfile::class, RoutePage.PageProfile.serializer())
                }
            }
        },
        RoutePage.PageSplash
    )

    LaunchedEffect(state.isLoggedIn, state.isSetupDone) {
        delay(2_500)
        when {
            state.isLoggedIn == false && state.isSetupDone == false -> {
                backStack.clear()
                backStack.add(RoutePage.PageSignIn)
            }
            state.isLoggedIn == false && state.isSetupDone == true -> {
                backStack.clear()
                backStack.add(RoutePage.PageSignIn)
            }
            state.isLoggedIn == true && state.isSetupDone == false -> {
                backStack.clear()
                backStack.add(RoutePage.PageSetup)
            }
            state.isLoggedIn == true && state.isSetupDone == true -> {
                backStack.clear()
                backStack.add(RoutePage.PageHome)
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = { navKey ->
            when (navKey) {
                is RoutePage.PageSplash -> {
                    NavEntry(navKey) {
                        PageSplashCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageSignIn -> {
                    NavEntry(navKey) {
                        PageSignInCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageSignUp -> {
                    NavEntry(navKey) {
                        PageSignUpCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageForgetPassword -> {
                    NavEntry(navKey) {
                        PageForgetPasswordCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageSetup -> {
                    NavEntry(navKey) {
                        PageSetupCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageHome -> {
                    NavEntry(navKey) {
                        PageHomeCore(
                            backStack = backStack
                        )
                    }
                }
                is RoutePage.PageProfile -> {
                    NavEntry(navKey) {
                        PageProfileCore(
                            backStack = backStack
                        )
                    }
                }
                else -> error("Unknown NavKey : $navKey")
            }
        },
        transitionSpec = {
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
        }
    )
}