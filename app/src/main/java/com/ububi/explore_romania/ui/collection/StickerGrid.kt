package com.ububi.explore_romania.ui.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.ububi.explore_romania.ui.stickers.Sticker

@Composable
fun StickerGrid(
    stickers: List<Sticker>,
    ownedIds: Set<String>,
    images: Map<String, ImageBitmap?>,
    grayImages: Map<String, ImageBitmap?>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.padding(24.dp)
    ) {
        items(stickers) { sticker ->
            val owned = ownedIds.contains(sticker.id)

            StickerCard(
                name = if (owned) sticker.name else "???",
                rarity = sticker.rarity,
                imageBitmap = if (owned)
                    images[sticker.id]
                else
                    grayImages[sticker.id],
                owned = owned
            )
        }
    }
}
