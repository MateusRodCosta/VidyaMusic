package com.mateusrodcosta.apps.vidyamusic.domain.repository

import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistConfigEntity
import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistEntity
import org.koin.core.annotation.Singleton

@Singleton
interface PlaylistRepository {
    suspend fun fetchPlaylist(playlistConfig: PlaylistConfigEntity): Result<PlaylistEntity>
}