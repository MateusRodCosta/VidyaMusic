package com.mateusrodcosta.apps.vidyamusic.features.settings.di

import com.mateusrodcosta.apps.vidyamusic.data.di.DataModule
import com.mateusrodcosta.apps.vidyamusic.domain.di.DomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class, DataModule::class])
@ComponentScan("com.mateusrodcosta.apps.vidyamusic.features.settings")
@Configuration
class SettingsModule