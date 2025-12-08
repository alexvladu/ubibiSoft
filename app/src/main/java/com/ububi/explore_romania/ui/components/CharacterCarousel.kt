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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left arrow button
        IconButton(
            onClick = {
                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                val newId = if (characterId > 1) characterId - 1 else 20
                onCharacterChange(newId)
            },
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFFFB74D), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous Character",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Character display - instant change, no animation
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CharacterSprite(
                characterId = characterId,
                size = 180
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right arrow button
        IconButton(
            onClick = {
                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                val newId = if (characterId < 20) characterId + 1 else 1
                onCharacterChange(newId)
            },
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFFFB74D), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next Character",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

