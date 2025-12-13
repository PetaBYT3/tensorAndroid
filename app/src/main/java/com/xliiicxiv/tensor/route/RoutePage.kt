package com.xliiicxiv.tensor.route

import kotlinx.serialization.Serializable

@Serializable
sealed class RoutePage {

    @Serializable
    data object PageSplash: RoutePage()

    @Serializable
    data object PageSignIn: RoutePage()

    @Serializable
    data object PageSignUp: RoutePage()

    @Serializable
    data object PageTemplate: RoutePage()

}