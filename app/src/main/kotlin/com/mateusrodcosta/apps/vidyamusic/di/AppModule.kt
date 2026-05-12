package com.mateusrodcosta.apps.vidyamusic.di

import com.mateusrodcosta.apps.vidyamusic.domain.player.AudioController
import com.mateusrodcosta.apps.vidyamusic.domain.repository.PreferencesRepository
import com.mateusrodcosta.apps.vidyamusic.domain.usecases.GetAvailablePlaylistsUseCase
import com.mateusrodcosta.apps.vidyamusic.domain.usecases.LoadPlaylistUseCase
import com.mateusrodcosta.apps.vidyamusic.features.playlist.PlaylistViewModel
import com.mateusrodcosta.apps.vidyamusic.features.playlist.di.PlaylistModule
import com.mateusrodcosta.apps.vidyamusic.features.settings.SettingsViewModel
import com.mateusrodcosta.apps.vidyamusic.features.settings.di.SettingsModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Module

@Module(includes = [SettingsModule::class, PlaylistModule::class])
@ComponentScan("com.mateusrodcosta.apps.vidyamusic")
@Configuration
class AppModule {

    @KoinViewModel
    fun settingsViewModel(preferencesRepository: PreferencesRepository) =
        SettingsViewModel(preferencesRepository)

    @KoinViewModel
    fun playlistViewModel(
        loadPlaylistUseCase: LoadPlaylistUseCase,
        getAvailablePlaylistsUseCase: GetAvailablePlaylistsUseCase,
        audioController: AudioController,
        preferencesRepository: PreferencesRepository
    ) = PlaylistViewModel(
        loadPlaylistUseCase, getAvailablePlaylistsUseCase, audioController, preferencesRepository
    )
}
