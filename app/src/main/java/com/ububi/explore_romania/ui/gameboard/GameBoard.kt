package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.ui.components.CharacterSprite
import com.ububi.explore_romania.ui.components.ConfettiAnimation
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.SoundEffect

@Composable
fun GameBoard(
    counties: List<County>,
    pawnPosition: Int,
    characterId: Int = 1,
    showConfetti: Boolean = false,
    pendingCoins: Int = 0,
    onHistoryClick: () -> Unit,
    onGeographyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val cellWidth = screenWidth / 6f
        val cellHeight = screenHeight / 4f
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .width(cellWidth * 4)
                .height(cellHeight * 2)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            if (pawnPosition < 16) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StackedButton(
                        text = "ISTORIE",
                        baseColor = Color(0xFF8B4513),
                        rotation = -8f,
                        onClick = {
                            MusicManager.playSoundEffect(SoundEffect.BUTTON)
                            onHistoryClick()
                        },
                        width = 160.dp,
                        height = 90.dp,
                        fontSize = 18.sp
                    )
                    StackedButton(
                        text = "GEOGRAFIE",
                        baseColor = Color(0xFF2E8B57),
                        rotation = 8f,
                        onClick = {
                            MusicManager.playSoundEffect(SoundEffect.BUTTON)
                            onGeographyClick()
                        },
                        width = 160.dp,
                        height = 90.dp,
                        fontSize = 18.sp
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "JOC TERMINAT!\nFELICITĂRI!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    if (pendingCoins > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ai câștigat $pendingCoins coin-uri! 🪙",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        )
                    }
                }
            }
        }


        if (counties.size >= 16) {
            for (index in 0 until 16) {
                val county = counties[index]
                val (col, row) = calculateBoardPosition(index)

                val isRevealed = index < pawnPosition

                GameCard(
                    county = county,
                    width = cellWidth,
                    height = cellHeight,
                    isRevealed = isRevealed,
                    modifier = Modifier.offset(x = cellWidth * col, y = cellHeight * row)
                )
            }

            // Pawn position - instant teleport (no animation)
            val displayPawnPos = if (pawnPosition >= 16) 15 else pawnPosition
            val (pawnCol, pawnRow) = calculateBoardPosition(displayPawnPos)

            // Calculate position directly without animation
            val pawnX = cellWidth * pawnCol
            val pawnY = cellHeight * pawnRow

            Box(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellHeight)
                    .offset(x = pawnX, y = pawnY),
                contentAlignment = Alignment.Center
            ) {
                CharacterSprite(
                    characterId = characterId,
                    size = 64,
                    modifier = Modifier.shadow(8.dp, CircleShape)
                )
            }

            // Confetti animation overlay at character position
            if (showConfetti) {
                // Calculate center of character in dp then convert to pixels
                val characterCenterXDp = pawnX.value + cellWidth.value / 2
                val characterCenterYDp = pawnY.value + cellHeight.value / 2

                // Convert to pixels using density
                with(density) {
                    val centerXPx = characterCenterXDp.dp.toPx()
                    val centerYPx = characterCenterYDp.dp.toPx()

                    ConfettiAnimation(
                        isAnimating = showConfetti,
                        centerX = centerXPx,
                        centerY = centerYPx,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun StackedButton(
    text: String,
    baseColor: Color,
    rotation: Float,
    onClick: () -> Unit,
    width: Dp,
    height: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    val shape = RoundedCornerShape(16.dp)
    val darkerColor = baseColor.copy(alpha = 0.8f)

    Box(
        modifier = Modifier
            .width(width + 12.dp)
            .height(height + 12.dp)
            .rotate(rotation),
        contentAlignment = Alignment.TopStart
    ) {
        Surface(modifier = Modifier.offset(12.dp, 12.dp).size(width, height), color = darkerColor, shape = shape, shadowElevation = 2.dp) {}
        Surface(modifier = Modifier.offset(6.dp, 6.dp).size(width, height), color = darkerColor, shape = shape, shadowElevation = 4.dp) {}
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = baseColor),
            shape = shape,
            elevation = ButtonDefaults.buttonElevation(8.dp),
            modifier = Modifier.size(width, height)
        ) {
            Text(text, fontSize = fontSize, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}


fun calculateBoardPosition(index: Int): Pair<Int, Int> {
    return when (index) {
        0 -> 0 to 0;
        1 -> 1 to 0;
        2 -> 2 to 0;
        3 -> 3 to 0;
        4 -> 4 to 0;
        5 -> 5 to 0
        6 -> 5 to 1;
        7 -> 5 to 2;
        8 -> 5 to 3;
        9 -> 4 to 3;
        10 -> 3 to 3
        11 -> 2 to 3;
        12 -> 1 to 3;
        13 -> 0 to 3;
        14 -> 0 to 2;
        15 -> 0 to 1
        else -> 0 to 0
    }
}