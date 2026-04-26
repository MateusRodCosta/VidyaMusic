package com.mateusrodcosta.apps.vidyamusic.domain.repository

import com.mateusrodcosta.apps.vidyamusic.core.enums.ThemeMode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val themeMode: Flow<ThemeMode>
    val useDynamicColor: Flow<Boolean>
    val usePrimaryOnRoster: Flow<Boolean>
    val skipPlaylistIntro: Flow<Boolean>


    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setUseDynamicColor(useDynamic: Boolean)
    suspend fun setUsePrimaryOnRoster(usePrimary: Boolean)
    suspend fun setSkipPlaylistIntro(skip: Boolean)
}