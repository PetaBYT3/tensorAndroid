package com.xliiicxiv.tensor.module

import com.xliiicxiv.tensor.repository.RepositoryAuth
import com.xliiicxiv.tensor.repository.RepositoryMessage
import com.xliiicxiv.tensor.repository.RepositoryUser
import com.xliiicxiv.tensor.viewmodel.ViewModelAddMessage
import com.xliiicxiv.tensor.viewmodel.ViewModelForgetPassword
import com.xliiicxiv.tensor.viewmodel.ViewModelHome
import com.xliiicxiv.tensor.viewmodel.ViewModelMainPager
import com.xliiicxiv.tensor.viewmodel.ViewModelMain
import com.xliiicxiv.tensor.viewmodel.ViewModelMessage
import com.xliiicxiv.tensor.viewmodel.ViewModelPrivateMessage
import com.xliiicxiv.tensor.viewmodel.ViewModelProfile
import com.xliiicxiv.tensor.viewmodel.ViewModelSearch
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
        viewModelOf(::ViewModelForgetPassword)
        viewModelOf(::ViewModelSetup)
        viewModelOf(::ViewModelMainPager)
        viewModelOf(::ViewModelHome)
        viewModelOf(::ViewModelMessage)
        viewModelOf(::ViewModelAddMessage)
        viewModelOf(::ViewModelPrivateMessage)
        viewModelOf(::ViewModelSearch)
        viewModelOf(::ViewModelProfile)
    }

    private val moduleRepository = module {
        factoryOf(::RepositoryAuth)
        factoryOf(::RepositoryUser)
        factoryOf(::RepositoryMessage)
    }

    fun getAllModule() = listOf(
        moduleViewModel,
        moduleRepository,
    )

}