package com.ububi.explore_romania.ui.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ububi.explore_romania.R
import com.ububi.explore_romania.ui.stickers.StickerRarity
import com.ububi.explore_romania.ui.theme.*


@Composable
fun StickerCard(
    name: String,
    rarity: StickerRarity,
    imagePath: String,
    grayImagePath: String,
    owned: Boolean
) {
    val finalPath = if (owned) imagePath else grayImagePath


    val DarkGreenBorder = Color(0xFF192E29)
    val LightGreenBg = Color(0xFFE8FCE6)

    Card(
        modifier = Modifier.width(180.dp).height(220.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(4.dp, DarkGreenBorder),
        colors = CardDefaults.cardColors(containerColor = LightGreenBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/$finalPath")
                        .crossfade(true).build(),
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreenBorder,
                textAlign = TextAlign.Center
            )
        }
    }
}