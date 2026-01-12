package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.R
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
    onCardClick: (County) -> Unit,
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
                        width = 200.dp,
                        height = 130.dp,
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
                        width = 200.dp,
                        height = 130.dp,
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
                            text = "Ai câștigat $pendingCoins monede! 🪙",
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
                    modifier = Modifier.offset(x = cellWidth * col, y = cellHeight * row),
                    onClick = { onCardClick(county) }
                )
            }

            val displayPawnPos = if (pawnPosition >= 16) 15 else pawnPosition
            val (pawnCol, pawnRow) = calculateBoardPosition(displayPawnPos)
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

            if (showConfetti) {
                val characterCenterXDp = pawnX.value + cellWidth.value / 2
                val characterCenterYDp = pawnY.value + cellHeight.value / 2

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

        // Contor bani
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Coin",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$pendingCoins",
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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

    Box(
        modifier = Modifier
            .width(width + 12.dp)
            .height(height + 12.dp)
            .rotate(rotation),
        contentAlignment = Alignment.TopStart
    ) {
        Surface(modifier = Modifier.offset(12.dp, 12.dp).size(width, height), color = baseColor.copy(alpha = 0.7f), shape = shape) {}

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = shape,
            modifier = Modifier
                .size(width, height)
                .paint(
                    painter = painterResource(id = com.ububi.explore_romania.R.drawable.texture),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(baseColor, BlendMode.Modulate)
                )
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = androidx.compose.ui.geometry.Offset(3f, 3f),
                        blurRadius = 2f
                    )
                )
            )
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