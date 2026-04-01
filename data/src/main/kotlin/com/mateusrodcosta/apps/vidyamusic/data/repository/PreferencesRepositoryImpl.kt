package com.mateusrodcosta.apps.vidyamusic.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mateusrodcosta.apps.vidyamusic.core.enums.ThemeMode
import com.mateusrodcosta.apps.vidyamusic.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {
    private val THEME_KEY = stringPreferencesKey("theme_mode")
    private val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
    private val SKIP_INTRO_KEY = booleanPreferencesKey("skip_intro")


    override val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val savedValue = preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
        ThemeMode.valueOf(savedValue)
    }

    override val useDynamicColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_KEY] ?: true
    }

    override val skipPlaylistIntro: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SKIP_INTRO_KEY] ?: true
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }

    override suspend fun setUseDynamicColor(useDynamic: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = useDynamic
        }
    }

    override suspend fun setSkipPlaylistIntro(skip: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SKIP_INTRO_KEY] = skip
        }
    }
}