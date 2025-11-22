package com.ububi.explore_romania.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D1733), Color(0xFF1A2A6E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EXPLORE ROMANIA",
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 8.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Aventura începe aici!",
                fontSize = 24.sp,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .width(280.dp)
                    .height(90.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
            ) {
                Text(
                    text = "START",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}