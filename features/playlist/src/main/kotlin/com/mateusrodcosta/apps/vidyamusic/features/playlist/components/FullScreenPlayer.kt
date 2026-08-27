package com.mateusrodcosta.apps.vidyamusic.features.playlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mateusrodcosta.apps.vidyamusic.domain.entity.TrackEntity
import com.mateusrodcosta.apps.vidyamusic.domain.player.PlayerState

@Composable
fun FullScreenPlayer(
    track: TrackEntity,
    playlistName: String,
    state: PlayerState,
    currentPositionMs: Long,
    bufferedPositionMs: Long,
    durationMs: Long,
    onSeek: (Long) -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onCollapseClick: () -> Unit,
    statusBarPaddings: PaddingValues,
    navigationBarPaddings: PaddingValues,
    modifier: Modifier = Modifier,
    useLandscapeLayout: Boolean = false,
) {
    val isPlaying = state == PlayerState.PLAYING

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = statusBarPaddings.calculateTopPadding(),
                bottom = navigationBarPaddings.calculateBottomPadding()
            )
            .testTag("full_player"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FullScreenPlayerHeader(
            playlistName = playlistName,
            onCollapseClick = onCollapseClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(if (useLandscapeLayout) 8.dp else 16.dp))

        if (useLandscapeLayout) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 48.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FullScreenPlayerCoverArt(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    FullScreenPlayerTrackInfo(track)

                    Spacer(modifier = Modifier.weight(1f))

                    FullScreenPlayerSeekBar(
                        currentPositionMs = currentPositionMs,
                        bufferedPositionMs = bufferedPositionMs,
                        durationMs = durationMs,
                        onSeek = onSeek
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FullScreenPlayerControls(
                        isPlaying = isPlaying,
                        onPlayPauseClick = onPlayPauseClick,
                        onPreviousClick = onPreviousClick,
                        onNextClick = onNextClick
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FullScreenPlayerCoverArt()

                Spacer(modifier = Modifier.weight(1f))

                FullScreenPlayerTrackInfo(track)

                Spacer(modifier = Modifier.weight(1f))

                FullScreenPlayerSeekBar(
                    currentPositionMs = currentPositionMs,
                    bufferedPositionMs = bufferedPositionMs,
                    durationMs = durationMs,
                    onSeek = onSeek
                )

                Spacer(modifier = Modifier.height(16.dp))

                FullScreenPlayerControls(
                    isPlaying = isPlaying,
                    onPlayPauseClick = onPlayPauseClick,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick
                )
            }
        }
    }
}