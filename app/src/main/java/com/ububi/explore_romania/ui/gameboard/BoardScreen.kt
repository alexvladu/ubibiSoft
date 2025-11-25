package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun BoardScreen(
    onBackClick: () -> Unit,
    onNavigateToQuiz: () -> Unit
) {
    val context = LocalContext.current

    // State for the list of counties on the board
    var countiesOnBoard by remember { mutableStateOf<List<County>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Pawn position (index from 0 to 15)
    var pawnPosition by remember { mutableIntStateOf(0) }

    // Load data when the screen opens
    LaunchedEffect(Unit) {
        countiesOnBoard = loadRandomCounties(context)
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {

            // CHECK: If we have data, draw the board
            if (countiesOnBoard.isNotEmpty()) {
                GameBoard(
                    counties = countiesOnBoard,
                    pawnPosition = pawnPosition,
                    onHistoryClick = { onNavigateToQuiz() },
                    onGeographyClick = { onNavigateToQuiz() },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // ERROR MESSAGE ON SCREEN
                Text(
                    text = "Error: Could not load counties from CSV.\nCheck judete.csv format",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
    }
}