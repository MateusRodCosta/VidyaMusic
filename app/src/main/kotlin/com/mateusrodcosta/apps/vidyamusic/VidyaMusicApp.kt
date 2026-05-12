package com.mateusrodcosta.apps.vidyamusic

import android.app.Application
import com.mateusrodcosta.apps.vidyamusic.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class VidyaMusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin<VidyaMusicApp> {
            androidLogger()
            androidContext(this@VidyaMusicApp)
        }
    }
}