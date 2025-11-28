package com.ububi.explore_romania.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CharacterSprite(
    characterId: Int,
    modifier: Modifier = Modifier,
    size: Int = 128
) {
    val context = LocalContext.current
    var characterBitmap by remember(characterId) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(characterId) {
        characterBitmap = loadCharacterSprite(context, characterId)
    }

    characterBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Character $characterId",
            modifier = modifier.size(size.dp)
        )
    }
}

suspend fun loadCharacterSprite(context: Context, characterId: Int): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.assets.open("sprite_sheet_character.png")
        val fullBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        // Assuming the sprite sheet has characters in a row
        // With 20 characters, we need to calculate the width of each sprite
        val spriteWidth = fullBitmap.width / 20
        val spriteHeight = fullBitmap.height

        // Character ID is 1-based (1-20), so we subtract 1 for 0-based index
        val index = (characterId - 1).coerceIn(0, 19)
        val x = index * spriteWidth

        // Extract the specific character sprite
        Bitmap.createBitmap(fullBitmap, x, 0, spriteWidth, spriteHeight)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
