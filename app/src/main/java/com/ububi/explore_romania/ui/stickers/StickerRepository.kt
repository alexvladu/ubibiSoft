package com.ububi.explore_romania.ui.stickers

import android.content.Context
import android.util.Log
import com.ububi.explore_romania.StickerPreferences
import kotlinx.coroutines.flow.first
import java.io.IOException

class StickerRepository(private val context: Context) {
    suspend fun loadAllStickers(): List<Sticker> {
        val rarities = mapOf(
            "common" to StickerRarity.COMMON,
            "rare" to StickerRarity.RARE,
            "epic" to StickerRarity.EPIC,
            "legendary" to StickerRarity.LEGENDARY
        )

        val stickers = mutableListOf<Sticker>()

        for ((folder, rarity) in rarities) {
            val path = "stickers/$folder"

            val files = try {
                context.assets.list(path) ?: emptyArray()
            } catch (e: IOException) {
                emptyArray()
            }
            Log.d("ASSETS_DEBUG", "Listing folder: $path -> ${files.joinToString()}")


            for (file in files) {
                if (!file.endsWith(".png")) continue

                val baseName = file.removeSuffix(".png")

                if (baseName.endsWith("_gray")) continue  // ignorăm imaginile gri (le deducem noi)

                val displayName = baseName.replace("_", " ")
                val id = "${folder}_${baseName}"

                val assetPath = "$path/$file"

                val grayPath = "$path/${baseName}_gray.png"

                stickers.add(
                    Sticker(
                        id = id,
                        name = displayName,
                        rarity = rarity,
                        assetPath = assetPath,
                        grayAssetPath = grayPath
                    )
                )
            }

        }

        Log.d("STICKERS_LOADED", "Loaded stickers = ${stickers.map { it.id }}")
        return stickers
    }

    suspend fun loadOwnedStickers(): Map<String, String> {
        return StickerPreferences.getStickerCollection(context).first()
    }

    suspend fun addSticker(id: String, rarity: StickerRarity) {
        val current = loadOwnedStickers().toMutableMap()
        current[id] = rarity.name
        StickerPreferences.saveStickerCollection(context, current)
    }

    suspend fun isOwned(id: String): Boolean {
        val current = loadOwnedStickers()
        return current.containsKey(id)
    }
}
