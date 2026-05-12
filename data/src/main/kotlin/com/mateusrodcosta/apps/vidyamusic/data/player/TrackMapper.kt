package com.mateusrodcosta.apps.vidyamusic.data.player

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mateusrodcosta.apps.vidyamusic.domain.entity.TrackEntity
import org.koin.core.annotation.Single

@Single
class TrackMapper(private val context: Context) {

    private val packageName: String = context.packageName

    fun mapToMediaItem(track: TrackEntity): MediaItem {
        val artworkUri = "android.resource://$packageName/drawable/cover_art".toUri()

        return MediaItem.Builder()
            .setMediaId(track.id.toString())
            .setUri(track.url)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(track.title)
                    .setArtist(track.game)
                    .setComposer(track.comp)
                    .setArtworkUri(artworkUri)
                    .build()
            )
            .build()
    }
}