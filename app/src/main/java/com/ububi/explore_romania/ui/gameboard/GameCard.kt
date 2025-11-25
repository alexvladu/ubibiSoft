package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameCard(
    county: County,
    width: Dp,
    height: Dp,
    isRevealed: Boolean,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(cardShape)
            .border(2.dp, Color.White, cardShape)
    ) {
        // Imaginea de fundal
        if (county.image != null) {
            Image(
                bitmap = county.image,
                contentDescription = county.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = if (isRevealed) 1.0f else 0.6f
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
        }

        // Strat "ceata"
        if (!isRevealed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.4f))
            )
        }

        // Nume judet
        if (isRevealed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = county.name,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}