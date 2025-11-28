package com.ububi.explore_romania.ui.chest
import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun ChestItemCard(
    chest: ChestModel,
    userPoints: Int,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canAfford = userPoints >= chest.cost

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0xFF333333), RoundedCornerShape(12.dp))
            .padding(6.dp) // Reduced padding slightly
    ) {
        // 1. The Image (Made shorter)
        Image(
            painter = painterResource(id = chest.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                // --- KEY CHANGE: Aspect Ratio > 1.0 makes it wider than it is tall ---
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(8.dp))
        )

        // Reduced Spacer
        Spacer(modifier = Modifier.height(4.dp))

        // 2. The Title
        Text(
            text = chest.name,
            color = chest.color,
            fontSize = 13.sp, // Slightly smaller font
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        // 3. The Cost Label
        Text(
            text = "${chest.cost} pts",
            color = Color.Gray,
            fontSize = 11.sp
        )

        // Reduced Spacer
        Spacer(modifier = Modifier.height(4.dp))

        // 4. The Button (Made shorter)
        Button(
            onClick = onOpen,
            enabled = canAfford,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canAfford) Color(0xFFFFA826) else Color.Blue,
                disabledContainerColor = Color(0xFF555555)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp) // --- KEY CHANGE: Reduced button height ---
        ) {
            Text(text = "Open", color = Color.White, fontSize = 11.sp)
        }
    }
}