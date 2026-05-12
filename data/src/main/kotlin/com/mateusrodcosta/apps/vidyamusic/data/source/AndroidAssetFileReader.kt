package com.mateusrodcosta.apps.vidyamusic.data.source

import android.content.Context
import org.koin.core.annotation.Single
import java.io.InputStreamReader

@Single
class AndroidAssetFileReader(private val context: Context) : AssetFileReader {
    override fun readJsonFile(fileName: String): String {
        return context.assets.open(fileName).use { inputStream ->
            InputStreamReader(inputStream).readText()
        }
    }
}