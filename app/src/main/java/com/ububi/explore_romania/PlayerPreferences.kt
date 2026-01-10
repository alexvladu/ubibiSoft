package com.ububi.explore_romania

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val MAX_STREAK = intPreferencesKey("maximum_streak")

    private val KEY_SEEN_RULES_TUTORIAL = booleanPreferencesKey("seen_rules_tutorial")
    private val KEY_SEEN_COUNTY_INFO_TUTORIAL = booleanPreferencesKey("seen_county_info_tutorial")
    private val KEY_SEEN_CHEST_TUTORIAL = booleanPreferencesKey("seen_chest_tutorial")

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
        context.dataStore.edit { prefs ->
            prefs[KEY_PENDING_COINS] = coins
        }
    }

    suspend fun finalizePendingCoins(context: Context) {
        context.dataStore.edit { prefs ->
            val currentTotal = prefs[KEY_COINS] ?: 0
            val pending = prefs[KEY_PENDING_COINS] ?: 0
            prefs[KEY_COINS] = currentTotal + pending
            prefs[KEY_PENDING_COINS] = 0
        }
    }

    suspend fun resetGameSession(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CURRENT_STREAK] = 0
            prefs[KEY_PENDING_COINS] = 0
        }
    }

    fun getSeenRulesTutorial(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_SEEN_RULES_TUTORIAL] ?: false
        }

    suspend fun setSeenRulesTutorial(context: Context, seen: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SEEN_RULES_TUTORIAL] = seen
        }
    }

    fun getSeenCountyInfoTutorial(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_SEEN_COUNTY_INFO_TUTORIAL] ?: false
        }

    suspend fun setSeenCountyInfoTutorial(context: Context, seen: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SEEN_COUNTY_INFO_TUTORIAL] = seen
        }
    }

    fun getSeenChestTutorial(context: Context) =
        context.dataStore.data.map { prefs ->
            prefs[KEY_SEEN_CHEST_TUTORIAL] ?: false
        }

    suspend fun setSeenChestTutorial(context: Context, seen: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SEEN_CHEST_TUTORIAL] = seen
        }
    }
}