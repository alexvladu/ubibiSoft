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
    private val KEY_CURRENT_STREAK = intPreferencesKey("current_streak")
    private val KEY_PENDING_COINS = intPreferencesKey("pending_coins")
    //asta nu e folosit nicaieri deocamdata
    private val MAX_STREAK = intPreferencesKey("maximum_streak")


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
            prefs[KEY_COINS] ?: 0
        }

    suspend fun saveCoins(context: Context, coins: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_COINS] = coins
        }
    }

    fun getCurrentStreak(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_CURRENT_STREAK] ?: 0
        }

    suspend fun saveCurrentStreak(context: Context, streak: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CURRENT_STREAK] = streak
            if(streak > (prefs[MAX_STREAK] ?: 0)) {
                prefs[MAX_STREAK] = streak
            }
        }
    }

    fun getPendingCoins(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_PENDING_COINS] ?: 0
        }

    suspend fun savePendingCoins(context: Context, coins: Int) {
        android.util.Log.d("PlayerPreferences", "Salvare pending coins: $coins")
        context.dataStore.edit { prefs ->
            prefs[KEY_PENDING_COINS] = coins
        }
    }

    suspend fun finalizePendingCoins(context: Context) {
        context.dataStore.edit { prefs ->
            val currentTotal = prefs[KEY_COINS] ?: 0
            val pending = prefs[KEY_PENDING_COINS] ?: 0
            android.util.Log.d("PlayerPreferences", "   Total curent: $currentTotal, Pending: $pending")
            prefs[KEY_COINS] = currentTotal + pending
            prefs[KEY_PENDING_COINS] = 0
        }
    }

    suspend fun resetGameSession(context: Context) {
        android.util.Log.d("PlayerPreferences", "🔄 RESET GAME SESSION")
        context.dataStore.edit { prefs ->
            prefs[KEY_CURRENT_STREAK] = 0
            prefs[KEY_PENDING_COINS] = 0
        }
    }
}