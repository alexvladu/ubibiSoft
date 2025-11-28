package com.ububi.explore_romania.ui.chest

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

// 1. Data Models
data object Rarity {
    const val COMUN = 10
    const val RAR = 20
    const val EPIC = 30
    const val LEGENDAR = 40
}

data class ChestModel(
    val id: Int,
    val name: String,
    val cost: Int,
    val color: Color,
    val imageRes: Int
)

@Composable
fun ChestScreen(navController: NavController) {
    // 2. UI State
    var showDialog by remember { mutableStateOf(false) }
    var rewardTitle by remember { mutableStateOf("") }
    var rewardColor by remember { mutableStateOf(Color.White) }
    var rewardImageRes by remember { mutableIntStateOf(0) }
    var points by remember { mutableIntStateOf(100) }

    // 3. Define the list of chests
    val chests = listOf(
        ChestModel(1, "Comun", Rarity.COMUN, Color(0xFFB0BEC5), R.drawable.star_on),
        ChestModel(2, "Rar", Rarity.RAR, Color(0xFF29B6F6), R.drawable.star_on),
        ChestModel(3, "Epic", Rarity.EPIC, Color(0xFFAB47BC), R.drawable.star_on),
        ChestModel(4, "Legendar", Rarity.LEGENDAR, Color(0xFFFFA726), R.drawable.star_on)
    )

    fun openChest(chest: ChestModel) {
        if (points >= chest.cost) {
            points -= chest.cost
            rewardImageRes = chest.imageRes
            rewardColor = chest.color
            rewardTitle = chest.name

            // Calculate Reward logic

            showDialog = true
        }
    }

    // --- MAIN CONTAINER ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121)),
        contentAlignment = Alignment.Center
    ) {

        // Back Button
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        // --- HORIZONTAL SCROLL ROW ---
        // We use LazyRow so you can scroll horizontally through the chests
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp), // Little padding on screen edges
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between cards
            verticalAlignment = Alignment.CenterVertically
        ) {
            chests.forEach { chest ->
                ChestItemCard(
                    chest = chest,
                    userPoints = points,
                    onOpen = { openChest(chest) },
                    // IMPORTANT: This makes each card take equal width (1/4 of screen)
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Points Text (Bottom Right)
        Text(
            text = "Ai $points puncte",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )

        // Popup Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color(0xFF333333),
                textContentColor = Color.White,
                title = {
                    Text(text = "Ai deschis un cufăr $rewardTitle!", color = rewardColor, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ai primit:", fontSize = 18.sp)
                        Image(
                            painter = painterResource(id = rewardImageRes),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
                        )

                        Text("Nume Sticker", fontSize = 18.sp)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = rewardColor)
                    ) {
                        Text("Colectează", color = Color.White)
                    }
                }
            )
        }
    }
}

