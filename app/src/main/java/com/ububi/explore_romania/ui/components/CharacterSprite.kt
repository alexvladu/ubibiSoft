package com.ububi.explore_romania.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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

    // --- ANIMAȚIA DE BOUNCE (SĂRITURĂ) ---
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -25f, // Înălțimea săriturii
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yDelta"
    )

    LaunchedEffect(characterId) {
        characterBitmap = loadCharacterSprite(context, characterId)
    }

    characterBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Character $characterId",
            modifier = modifier
                .size(size.dp)
                .graphicsLayer {
                    translationY = translateY
                }
        )
    }
}

suspend fun loadCharacterSprite(context: Context, characterId: Int): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val (fileName, totalInSheet, offsetIndex) = if (characterId <= 20) {
            Triple("sprite_sheet_character.png", 20, characterId - 1)
        } else {
            Triple("personaje_ema.png", 10, characterId - 21)
        }

        val inputStream = context.assets.open(fileName)
        val fullBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val spriteWidth = fullBitmap.width / totalInSheet
        val spriteHeight = fullBitmap.height
        val x = offsetIndex * spriteWidth

        Bitmap.createBitmap(fullBitmap, x, 0, spriteWidth, spriteHeight)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}