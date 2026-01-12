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
    data object PageMainPager: RoutePage, NavKey

    @Serializable
    data object PageAddMessage: RoutePage, NavKey

    @Serializable
    data class PagePrivateMessage(
        val messageId: String
    ): RoutePage, NavKey

    @Serializable
    data object PageTemplate: RoutePage, NavKey

}