package com.vasberc.bookflix

import android.app.Application
import org.koin.android.ext.koin.androidContext

class BookFlixApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
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