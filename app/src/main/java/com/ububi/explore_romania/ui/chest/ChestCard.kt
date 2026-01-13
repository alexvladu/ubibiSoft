package com.ububi.explore_romania.ui.chest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ChestItemCard(
    chest: ChestModel,
    userPoints: Int,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canAfford = userPoints >= chest.cost

    val darkWarm = Color(0xFF43271B)
    val lightWarm = Color(0xFFC27A35)
    val creamText = Color(0xFFFFF8E1)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(darkWarm, RoundedCornerShape(16.dp)) // Fundal maro închis
            .padding(8.dp)
    ) {
        // 1. Imaginea Cufărului
        Image(
            painter = painterResource(id = chest.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit, // Folosim Fit pentru a nu tăia din cufăr
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Numele (Text îmbunătățit)
        Text(
            text = chest.name.uppercase(), // Majuscule pentru un aspect mai profi
            color = chest.color, // Păstrăm culoarea rarității (Comun, Rar, etc.)
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 3. Indicatorul de Preț
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .background(lightWarm.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(18.dp)
                    .background(lightWarm, CircleShape)
            ) {
                Text(
                    text = "C",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Text(
                text = chest.cost.toString(),
                color = creamText, // Textul prețului pe crem
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Butonul de Deschidere
        Button(
            onClick = onOpen,
            enabled = canAfford,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightWarm, // Folosim culoarea ta caldă
                disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(
                text = "DESCHIDE",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}