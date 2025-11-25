package com.ububi.explore_romania

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

object PlayerPreferences {
    private val KEY_PLAYER_NAME = stringPreferencesKey("player_name")
    private val KEY_CHARACTER_ID = intPreferencesKey("character_id")
    private val KEY_COINS = intPreferencesKey("player_coins")

    fun getPlayerName(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_PLAYER_NAME] ?: ""
        }

    suspend fun savePlayerName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PLAYER_NAME] = name
        }
    }

    fun getCharacterId(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_CHARACTER_ID] ?: 1
        }

    suspend fun saveCharacterId(context: Context, characterId: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CHARACTER_ID] = characterId
        }
    }

    fun getCoins(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_COINS] ?: 67
        }

    suspend fun saveCoins(context: Context, coins: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_COINS] = coins
        }
    }
}