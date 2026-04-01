package com.mateusrodcosta.apps.vidyamusic.data.repository

import com.mateusrodcosta.apps.vidyamusic.data.dto.PlaylistDto
import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistConfigEntity
import com.mateusrodcosta.apps.vidyamusic.domain.entity.PlaylistEntity
import com.mateusrodcosta.apps.vidyamusic.domain.entity.TrackEntity
import com.mateusrodcosta.apps.vidyamusic.domain.repository.PlaylistRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(private val client: HttpClient) : PlaylistRepository {
    override suspend fun fetchPlaylist(playlistConfig: PlaylistConfigEntity): Result<PlaylistEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = client.get(playlistConfig.url)
                val playlistDto: PlaylistDto = response.body()

                val isSourcePlaylist = playlistConfig.isSource
                val safeBaseUrl = playlistDto.url.removeSuffix("/")

                val trackEntities = playlistDto.tracks.mapIndexed { index, dto ->
                    val shouldUseSource = isSourcePlaylist && dto.sFile != null

                    val baseId = if (shouldUseSource) (dto.sId ?: dto.id) else dto.id
                    val id = baseId ?: index

                    val title = if (shouldUseSource) (dto.sTitle ?: dto.title) else dto.title
                    val file = if (shouldUseSource) dto.sFile else dto.file

                    val fileName = "$file.${playlistDto.ext}"

                    val finalUrl =
                        if (shouldUseSource && playlistConfig.sourcePath?.isNotEmpty() == true) {
                            val safeSourcePath =
                                playlistConfig.sourcePath?.removePrefix("/")?.removeSuffix("/")
                            "$safeBaseUrl/$safeSourcePath/$fileName"
                        } else {
                            "$safeBaseUrl/$fileName"
                        }

                    TrackEntity(
                        id = id,
                        game = dto.game,
                        title = title,
                        comp = dto.comp,
                        arr = if (!shouldUseSource) dto.arr else null,
                        file = file,
                        url = finalUrl
                    )
                }

                val playlistEntity = PlaylistEntity(
                    id = playlistConfig.id,
                    name = playlistConfig.name,
                    description = playlistConfig.description,
                    changelog = playlistDto.changelog,
                    url = playlistDto.url,
                    ext = playlistDto.ext,
                    newId = playlistDto.newId,
                    tracks = trackEntities,
                )

                Result.success(playlistEntity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}