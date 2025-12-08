package com.ububi.explore_romania

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.stickerDataStore by preferencesDataStore("sticker_prefs")

object StickerPreferences {

    private val KEY_STICKER_JSON = stringPreferencesKey("sticker_json")

    fun getStickerCollection(context: Context) =
        context.stickerDataStore.data.map { prefs ->
            val jsonString = prefs[KEY_STICKER_JSON] ?: "{}"
            parseStickerJson(jsonString)
        }

    suspend fun saveStickerCollection(context: Context, collection: Map<String, String>) {
        context.stickerDataStore.edit { prefs ->
            prefs[KEY_STICKER_JSON] = collectionToJson(collection)
        }
    }

    private fun parseStickerJson(jsonString: String): Map<String, String> {
        val json = JSONObject(jsonString)
        val map = mutableMapOf<String, String>()

        json.keys().forEach { key ->
            map[key] = json.getString(key)
        }

        return map
    }

    private fun collectionToJson(map: Map<String, String>): String {
        val json = JSONObject()
        map.forEach { (key, value) ->
            json.put(key, value)
        }
        return json.toString()
    }
}
