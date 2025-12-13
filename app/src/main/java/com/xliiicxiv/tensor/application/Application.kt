package com.xliiicxiv.tensor.application

import android.app.Application
import com.xliiicxiv.tensor.module.Module
import org.koin.core.context.startKoin

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(Module.getAllModule())
        }

    }

}