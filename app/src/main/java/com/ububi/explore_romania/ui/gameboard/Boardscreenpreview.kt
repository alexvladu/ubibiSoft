package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ububi.explore_romania.R

/**
 * Preview principal - background + butoane ISTORIE/GEOGRAFIE + un card revelat
 */
@Preview(
    name = "Board Preview",
    showBackground = true,
    widthDp = 800,
    heightDp = 480
)
@Composable
fun BoardPreview() {
    val mockCounties = List(16) { index ->
        County(id = index + 1, name = "Cluj", image = null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.board),
                contentScale = ContentScale.Crop
            )
    ) {
        GameBoard(
            counties = mockCounties,
            pawnPosition = 1,
            characterId = 1,
            showConfetti = false,
            pendingCoins = 0,
            onHistoryClick = {},
            onGeographyClick = {},
            onCardClick = {}
        )
    }
}

/**
 * Preview doar pentru un GameCard revelat
 */
@Preview(
    name = "Card Preview",
    showBackground = true,
    widthDp = 150,
    heightDp = 120
)
@Composable
fun CardPreview() {
    GameCard(
        county = County(id = 1, name = "Cluj", image = null),
        width = 150.dp,
        height = 120.dp,
        isRevealed = true,
        onClick = {}
    )
}