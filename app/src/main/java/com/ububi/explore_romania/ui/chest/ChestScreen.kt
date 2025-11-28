package com.ububi.explore_romania.ui.chest

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ububi.explore_romania.PlayerPreferences
import com.ububi.explore_romania.ui.stickers.StickerRarity
import com.ububi.explore_romania.ui.stickers.StickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = StickerRepository(context)
    var showDialog by remember { mutableStateOf(false) }
    var rewardTitle by remember { mutableStateOf("") }
    var rewardColor by remember { mutableStateOf(Color.White) }
    var rewardSubtitle by remember { mutableStateOf("") }
    var points by remember { mutableIntStateOf(0) }
    var rewardStickerBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var rewardFallbackRes by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        PlayerPreferences.getCoins(context).collect { savedCoins ->
            points = savedCoins
        }
    }

    val chests = listOf(
        ChestModel(
            1,
            "Comun",
            Rarity.COMUN,
            Color(0xFFB0BEC5),
            com.ububi.explore_romania.R.drawable.chest
        ),
        ChestModel(
            2,
            "Rar",
            Rarity.RAR,
            Color(0xFF29B6F6),
            com.ububi.explore_romania.R.drawable.chest
        ),
        ChestModel(
            3,
            "Epic",
            Rarity.EPIC,
            Color(0xFFAB47BC),
            com.ububi.explore_romania.R.drawable.chest
        ),
        ChestModel(
            4,
            "Legendar",
            Rarity.LEGENDAR,
            Color(0xFFFFA726),
            com.ububi.explore_romania.R.drawable.chest
        )
    )
    suspend fun loadBitmapFromAssets(context: Context, path: String): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open(path).use { inputStream ->
                    BitmapFactory.decodeStream(inputStream).asImageBitmap()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
    fun openChest(chest: ChestModel) {
        if (points >= chest.cost) {
            val previousPoints = points
            points -= chest.cost

            scope.launch {
                PlayerPreferences.saveCoins(context, points)

                val rarityEnum = when (chest.cost) {
                    Rarity.COMUN -> StickerRarity.COMMON
                    Rarity.RAR -> StickerRarity.RARE
                    Rarity.EPIC -> StickerRarity.EPIC
                    Rarity.LEGENDAR -> StickerRarity.LEGENDARY
                    else -> StickerRarity.COMMON
                }

                val sticker = repo.getRandomUnownedSticker(rarityEnum)

                if (sticker != null) {
                    repo.addSticker(sticker.id, sticker.rarity)

                    val bitmap = loadBitmapFromAssets(context, sticker.assetPath)

                    rewardStickerBitmap = bitmap
                    rewardFallbackRes = 0
                    rewardTitle = "Sticker Nou!"
                    rewardSubtitle = sticker.name
                    rewardColor = chest.color

                } else {
                    points = previousPoints
                    PlayerPreferences.saveCoins(context, points)

                    rewardStickerBitmap = null
                    rewardFallbackRes = chest.imageRes
                    rewardTitle = "Colecție Completă!"
                    rewardSubtitle =
                        "Ai toate stickerele din categoria ${chest.name}!"
                    rewardColor = chest.color
                }
                showDialog=true;
            }
        }
    }


    // --- MAIN CONTAINER ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF311B92),
                        Color(0xFF000000)
                    ),
                    radius = 1500f
                )
            ),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            chests.forEach { chest ->
                ChestItemCard(
                    chest = chest,
                    userPoints = points,
                    onOpen = { openChest(chest) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 30.dp, end = 30.dp)
                .background(
                    color = Color(0xFFFFB74D),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFFF57C00),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "C",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = points.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color(0xFF333333),
                textContentColor = Color.White,
                title = {
                    Text(text = rewardTitle, color = rewardColor, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if(rewardStickerBitmap != null) "Ai primit:" else "",
                            fontSize = 18.sp
                        )


                        if (rewardStickerBitmap != null) {
                            Image(
                                bitmap = rewardStickerBitmap!!,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(vertical = 16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Text(
                            text = rewardSubtitle,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = rewardColor)
                    ) {
                        Text(
                            text = if(rewardStickerBitmap != null) "Colectează" else "Ok",
                            color = Color.White
                        )
                    }
                }
            )
        }
    }
}
@Preview(
    name = "Chest Screen – Tablet Landscape",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800
)
@Composable
fun PreviewChestScreenTablet() {
    ChestScreen(navController = rememberNavController())
}

