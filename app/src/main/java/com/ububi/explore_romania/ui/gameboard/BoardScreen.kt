package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(onBackClick: () -> Unit, onNavigateToQuiz: ()-> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A1733),
                            Color(0xFF1A2A5F),
                            Color(0xFF2D4B73)
                        )
                    )
                )
        )

        // Main GameBoard with safe horizontal margins
        GameBoard(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)  // ← Perfect side margins
                .padding(top = 60.dp, bottom = 20.dp),
            onNavigateToQuiz
        )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(56.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}