package com.mateusrodcosta.apps.vidyamusic.features.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateusrodcosta.apps.vidyamusic.domain.player.AudioController
import com.mateusrodcosta.apps.vidyamusic.domain.player.PlayerState
import com.mateusrodcosta.apps.vidyamusic.domain.repository.PreferencesRepository
import com.mateusrodcosta.apps.vidyamusic.domain.usecases.GetAvailablePlaylistsUseCase
import com.mateusrodcosta.apps.vidyamusic.domain.usecases.LoadPlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val loadPlaylistUseCase: LoadPlaylistUseCase,
    private val getAvailablePlaylistsUseCase: GetAvailablePlaylistsUseCase,
    private val audioController: AudioController,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    val playerState = audioController.playerState
    val currentTrack = audioController.currentTrack

    val currentPositionMs = audioController.currentPositionMs
    val bufferedPositionMs = audioController.bufferedPositionMs
    val durationMs = audioController.durationMs

    val usePrimaryOnRoster = preferencesRepository.usePrimaryOnRoster

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getAvailablePlaylistsUseCase().onSuccess { playlists ->
                _uiState.update { it.copy(availablePlaylists = playlists) }
            }

            loadPlaylistUseCase(playlistId = null).onSuccess { playlist ->
                _uiState.update { it.copy(selectedPlaylist = playlist, isLoading = false) }

                if (playlist.tracks.isNotEmpty()) {
                    val skipIntro = preferencesRepository.skipPlaylistIntro.first()

                    val tracksToPlay = if (skipIntro && playlist.tracks.size > 1)
                        playlist.tracks.drop(1)
                    else
                        playlist.tracks

                    val startIndex = if (tracksToPlay.isNotEmpty()) {
                        tracksToPlay.indices.random()
                    } else {
                        0
                    }

                    audioController.startPlaylist(tracksToPlay, startIndex)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message ?: "Unknown connection error",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectPlaylist(playlistId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            loadPlaylistUseCase(playlistId).onSuccess { playlist ->
                _uiState.update { it.copy(selectedPlaylist = playlist, isLoading = false) }

                if (playlist.tracks.isNotEmpty()) {
                    val skipIntro = preferencesRepository.skipPlaylistIntro.first()

                    val tracksToPlay = if (skipIntro && playlist.tracks.size > 1)
                        playlist.tracks.drop(1)
                    else
                        playlist.tracks

                    val startIndex = if (tracksToPlay.isNotEmpty()) {
                        tracksToPlay.indices.random()
                    } else {
                        0
                    }

                    audioController.startPlaylist(tracksToPlay, startIndex)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message ?: "Unknown connection error",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun reload() {
        val currentPlaylistId = _uiState.value.selectedPlaylist?.id

        if (currentPlaylistId != null) {
            selectPlaylist(currentPlaylistId)
        } else {
            fetchInitialData()
        }
    }

    fun playTrack(trackIndex: Int) {
        val currentPlaylist = _uiState.value.selectedPlaylist?.tracks ?: return
        audioController.startPlaylist(currentPlaylist, trackIndex)
    }

    fun togglePlayPause() {
        if (playerState.value == PlayerState.PLAYING) {
            audioController.pause()
        } else {
            audioController.play()
        }
    }

    fun skipToNext() {
        audioController.skipToNext()
    }

    fun skipToPrevious() {
        audioController.skipToPrevious()
    }

    fun seekTo(positionMs: Long) {
        audioController.seekTo(positionMs)
    }
}