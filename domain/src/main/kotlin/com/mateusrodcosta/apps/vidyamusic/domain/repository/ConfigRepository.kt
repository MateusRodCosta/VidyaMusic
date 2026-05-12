package com.mateusrodcosta.apps.vidyamusic.domain.repository

import com.mateusrodcosta.apps.vidyamusic.domain.entity.ConfigFileEntity
import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistConfigEntity
import org.koin.core.annotation.Singleton

@Singleton
interface ConfigRepository {
    suspend fun loadConfig(): Result<ConfigFileEntity>
    suspend fun getAvailablePlaylists(): Result<List<PlaylistConfigEntity>>
}