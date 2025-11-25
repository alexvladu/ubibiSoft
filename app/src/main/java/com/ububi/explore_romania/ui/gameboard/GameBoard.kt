package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameBoard(
    counties: List<County>,
    pawnPosition: Int,
    onHistoryClick: () -> Unit,
    onGeographyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        // --- GRID DIMENSIONS ---
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val cellWidth = screenWidth / 6f
        val cellHeight = screenHeight / 4f

        // --- CENTER AREA ---
        Box(
            modifier = Modifier
                .width(cellWidth * 4)
                .height(cellHeight * 2)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- HISTORY STACKED BUTTON ---
                StackedButton(
                    text = "HISTORY",
                    baseColor = Color(0xFF8B4513), // Brown
                    rotation = -12f,
                    onClick = onHistoryClick,
                    width = 220.dp,
                    height = 120.dp,
                    fontSize = 20.sp
                )

                // --- GEOGRAPHY STACKED BUTTON ---
                StackedButton(
                    text = "GEOGRAPHY",
                    baseColor = Color(0xFF2E8B57), // Green
                    rotation = 12f,
                    onClick = onGeographyClick,
                    width = 220.dp,
                    height = 120.dp,
                    fontSize = 18.sp
                )
            }
        }

        // --- DRAW CARDS ON EDGES ---
        if (counties.size >= 16) {
            for (index in 0 until 16) {
                val county = counties[index]
                val (col, row) = calculateBoardPosition(index)
                GameCard(
                    county = county,
                    width = cellWidth,
                    height = cellHeight,
                    modifier = Modifier.offset(x = cellWidth * col, y = cellHeight * row)
                )
            }
            // --- PAWN ---
            val (pawnCol, pawnRow) = calculateBoardPosition(pawnPosition)
            Box(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellHeight)
                    .offset(x = cellWidth * pawnCol, y = cellHeight * pawnRow),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Red, shape = CircleShape)
                        .border(3.dp, Color.White, shape = CircleShape)
                        .shadow(6.dp, CircleShape)
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
    val darkerColor = baseColor.copy(alpha = 0.8f)

    Box(
        modifier = Modifier
            .width(width + 12.dp)
            .height(height + 12.dp)
            .rotate(rotation),
        contentAlignment = Alignment.TopStart
    ) {
        // --- "Cartea" de jos (Cea mai din spate) ---
        Surface(
            modifier = Modifier
                .offset(x = 12.dp, y = 12.dp) // Offset mare
                .width(width)
                .height(height),
            color = darkerColor,
            shape = shape,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
            shadowElevation = 2.dp
        ) {}

        // --- "Cartea" din mijloc ---
        Surface(
            modifier = Modifier
                .offset(x = 6.dp, y = 6.dp) // Offset mediu
                .width(width)
                .height(height),
            color = darkerColor,
            shape = shape,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
            shadowElevation = 4.dp
        ) {}

        // --- Butonul principal (Deasupra) ---
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = baseColor),
            shape = shape,
            border = BorderStroke(2.dp, Color.White),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp
            ),
            modifier = Modifier
                .width(width)
                .height(height)
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge.copy(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}

// --- LOGIC FOR POSITIONING (6x4 Grid) ---
// (Neschimbată)
fun calculateBoardPosition(index: Int): Pair<Int, Int> {
    return when (index) {
        0 -> 0 to 0
        1 -> 1 to 0
        2 -> 2 to 0
        3 -> 3 to 0
        4 -> 4 to 0
        5 -> 5 to 0
        6 -> 5 to 1
        7 -> 5 to 2
        8 -> 5 to 3
        9 -> 4 to 3
        10 -> 3 to 3
        11 -> 2 to 3
        12 -> 1 to 3
        13 -> 0 to 3
        14 -> 0 to 2
        15 -> 0 to 1
        else -> 0 to 0
    }
}