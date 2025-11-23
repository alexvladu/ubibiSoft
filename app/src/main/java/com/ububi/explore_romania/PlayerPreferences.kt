package com.ububi.explore_romania

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

object PlayerPreferences {
    private val KEY_PLAYER_NAME = stringPreferencesKey("player_name")

    fun getPlayerName(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_PLAYER_NAME] ?: ""
        }

    suspend fun savePlayerName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PLAYER_NAME] = name
        }
    }
}