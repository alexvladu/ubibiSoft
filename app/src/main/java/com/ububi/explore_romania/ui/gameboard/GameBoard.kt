package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun GameBoard(
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val topCounties = listOf("B", "CJ", "TM", "IS", "PH")
            topCounties.forEach { county ->
                BoardCard(
                    countyCode = county,
                    number = String.format("%02d", Random.nextInt(1, 100)),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // MIDDLE: Left + Center + Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // LEFT column (2 plates)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                BoardCard(countyCode = "AG", number = "07", modifier = Modifier.weight(1f))
                BoardCard(countyCode = "VL", number = "23", modifier = Modifier.weight(1f))
            }

            // CENTER - Main game area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF212121))
                    .border(8.dp, Color(0xFF424242), RoundedCornerShape(32.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // Two big letters (like in your screenshot)
                    Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                        BigCenterLetter("Istorie")
                        BigCenterLetter("Geografie")
                    }

                    Text(
                        text = "EXPLORE ROMANIA",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 6.sp
                    )

                    Text(
                        text = "Colectează județe • Călătorește • Descoperă",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB0BEC5)
                    )

                    // Bonus: Player name bubble like in your pic
                    Box(
                        modifier = Modifier
                            .offset(x = 80.dp, y = 20.dp)
                            .background(Color(0xFFE91E63), RoundedCornerShape(30.dp))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "ALEXANDRA-ISA...",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // RIGHT column (2 plates)
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                BoardCard(countyCode = "CT", number = "77", modifier = Modifier.weight(1f))
                BoardCard(countyCode = "BR", number = "01", modifier = Modifier.weight(1f))
            }
        }

        // BOTTOM ROW - 5 plates
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bottomCounties = listOf("IF", "GL", "HR", "SM", "BH")
            bottomCounties.forEach { county ->
                BoardCard(
                    countyCode = county,
                    number = String.format("%02d", Random.nextInt(1, 100)),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}