package com.vasberc.bookflix

import android.app.Application
import com.vasberc.data_local.di.DataLocalModule
import com.vasberc.data_remote.di.DataRemoteModule
import com.vasberc.domain.di.DomainModule
import com.vasberc.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class BookFlixApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@BookFlixApp)
            modules(
                PresentationModule().module,
                DomainModule().module,
                DataRemoteModule().module,
                DataLocalModule().module
            )
        }
    }
}