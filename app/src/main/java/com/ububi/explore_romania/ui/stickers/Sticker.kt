package com.ububi.explore_romania.ui.stickers

data class Sticker(
    val id: String,
    val name: String,
    val rarity: StickerRarity,
    val assetPath: String,
    val grayAssetPath: String,
)