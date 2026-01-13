package com.ububi.explore_romania.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.SoundEffect
import androidx.compose.runtime.Composable

@Composable
fun CharacterCarousel(
    characterId: Int,
    onCharacterChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxCharacters = 30 // 20 vechi + 10 noi

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                val newId = if (characterId > 1) characterId - 1 else maxCharacters
                onCharacterChange(newId)
            },
            modifier = Modifier.size(48.dp).background(Color(0xFFC27A35), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Prev", tint = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            CharacterSprite(characterId = characterId, size = 200)
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = {
                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                val newId = if (characterId < maxCharacters) characterId + 1 else 1
                onCharacterChange(newId)
            },
            modifier = Modifier.size(48.dp).background(Color(0xFFC27A35), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next", tint = Color.White)
        }
    }
}
