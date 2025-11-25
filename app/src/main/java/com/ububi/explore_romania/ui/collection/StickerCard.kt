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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.R
import com.ububi.explore_romania.ui.stickers.StickerRarity
import com.ububi.explore_romania.ui.theme.*



@Composable
fun StickerCard(
    name: String,
    rarity: StickerRarity,
    imageBitmap: ImageBitmap?,
    owned: Boolean,
    modifier: Modifier = Modifier
) {
    val outlineColor = when (rarity) {
        StickerRarity.COMMON -> CommonOutline
        StickerRarity.RARE -> RareOutline
        StickerRarity.EPIC -> EpicOutline
        StickerRarity.LEGENDARY -> LegendaryOutline
    }

    val RobloxFont = FontFamily(Font(R.font.roblox))
    val NameFont = FontFamily.Default

    Card(
        modifier = modifier
            .width(180.dp)
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(4.dp, outlineColor),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Imagine
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (imageBitmap == null) {
                    Text("NO IMAGE", color = Color.Red)
                }
            }

            // Nume sticker
            Text(
                text = name,
                fontSize = 18.sp,
                fontFamily = NameFont,
                textAlign = TextAlign.Center
            )
        }
    }
}
