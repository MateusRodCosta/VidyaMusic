package com.mateusrodcosta.apps.vidyamusic.data.di

import com.mateusrodcosta.apps.vidyamusic.domain.di.DomainModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.PropertyValue
import org.koin.core.annotation.Single

@PropertyValue("config.filename")
const val DEFAULT_CONFIG_FILENAME = "config.json"

@Module(includes = [DomainModule::class])
@ComponentScan("com.mateusrodcosta.apps.vidyamusic.data")
@Configuration
class DataModule {

    @Single
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Single
    fun provideHttpClient(json: Json): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
    }
}