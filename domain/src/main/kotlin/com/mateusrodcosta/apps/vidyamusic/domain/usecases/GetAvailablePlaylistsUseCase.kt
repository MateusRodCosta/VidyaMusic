package com.mateusrodcosta.apps.vidyamusic.domain.usecases

import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistConfigEntity
import com.mateusrodcosta.apps.vidyamusic.domain.repository.ConfigRepository
import org.koin.core.annotation.Factory

@Factory
class GetAvailablePlaylistsUseCase(private val configRepository: ConfigRepository) {
    suspend operator fun invoke(): Result<List<PlaylistConfigEntity>> {
        return configRepository.getAvailablePlaylists()
    }
}
