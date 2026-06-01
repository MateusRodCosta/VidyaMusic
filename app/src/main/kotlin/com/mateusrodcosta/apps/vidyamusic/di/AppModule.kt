package com.mateusrodcosta.apps.vidyamusic.di

import com.mateusrodcosta.apps.vidyamusic.features.playlist.di.PlaylistModule
import com.mateusrodcosta.apps.vidyamusic.features.settings.di.SettingsModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [SettingsModule::class, PlaylistModule::class])
@ComponentScan("com.mateusrodcosta.apps.vidyamusic")
@Configuration
class AppModule
