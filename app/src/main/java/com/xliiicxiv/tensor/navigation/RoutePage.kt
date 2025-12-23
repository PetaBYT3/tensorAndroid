package com.xliiicxiv.tensor.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface RoutePage: NavKey {

    @Serializable
    data object PageSplash: RoutePage, NavKey

    @Serializable
    data object PageSignIn: RoutePage, NavKey

    @Serializable
    data object PageSignUp: RoutePage, NavKey

    @Serializable
    data object PageForgetPassword: RoutePage, NavKey

    @Serializable
    data object PageSetup: RoutePage, NavKey

    @Serializable
    data object PageHome: RoutePage, NavKey

    @Serializable
    data object PageProfile: RoutePage, NavKey

    @Serializable
    data object PageTemplate: RoutePage, NavKey

}