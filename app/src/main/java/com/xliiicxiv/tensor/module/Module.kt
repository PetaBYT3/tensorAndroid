package com.xliiicxiv.tensor.module

import com.google.firebase.auth.FirebaseAuth
import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.viewmodel.ViewModelHome
import com.xliiicxiv.tensor.viewmodel.ViewModelMain
import com.xliiicxiv.tensor.viewmodel.ViewModelProfile
import com.xliiicxiv.tensor.viewmodel.ViewModelSetup
import com.xliiicxiv.tensor.viewmodel.ViewModelSignIn
import com.xliiicxiv.tensor.viewmodel.ViewModelSignUp
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object Module {

    private val moduleViewModel = module {
        singleOf(::ViewModelMain)
        viewModelOf(::ViewModelSignIn)
        viewModelOf(::ViewModelSignUp)
        viewModelOf(::ViewModelSetup)
        viewModelOf(::ViewModelHome)
        viewModelOf(::ViewModelProfile)
    }

    private val moduleRepository = module {
        factoryOf(::RepositoryAuth)
        factoryOf(::RepositoryUser)
    }

    fun getAllModule() = listOf(
        moduleViewModel,
        moduleRepository,
    )

}