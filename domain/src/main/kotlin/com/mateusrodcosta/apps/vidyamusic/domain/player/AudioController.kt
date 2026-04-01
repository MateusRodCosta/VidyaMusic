package com.mateusrodcosta.apps.vidyamusic.domain.player

import com.mateusrodcosta.apps.vidyamusic.domain.entity.TrackEntity
import kotlinx.coroutines.flow.StateFlow

interface AudioController {
    val playerState: StateFlow<PlayerState>
    val currentTrack: StateFlow<TrackEntity?>
    val currentPositionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>
    val bufferedPositionMs: StateFlow<Long>

    fun startPlaylist(tracks: List<TrackEntity>, startIndex: Int = 0)

    fun play()
    fun pause()
    fun stop()

    fun skipToNext()
    fun skipToPrevious()
    fun seekTo(positionMs: Long)
}