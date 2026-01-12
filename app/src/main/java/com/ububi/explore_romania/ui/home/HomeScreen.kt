package com.ububi.explore_romania.ui.home

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ububi.explore_romania.PlayerPreferences
import com.ububi.explore_romania.ui.components.CharacterCarousel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ububi.explore_romania.R
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import com.ububi.explore_romania.SoundEffect

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit,
    onCollectionClick: () -> Unit,
    onTreasureClick: () -> Unit,
) {
    val MarioFont = FontFamily(Font(R.font.retromario))
    val context = LocalContext.current
    val activity = context as? Activity

    var playerName by remember { mutableStateOf("") }
    var characterId by remember { mutableIntStateOf(1) }
    var coins by remember { mutableIntStateOf(67) }
    var isNameLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.HOME)
    }

    LaunchedEffect(Unit) {
        PlayerPreferences.getPlayerName(context).collect { saved ->
            if (!isNameLoaded) {
                playerName = saved
                isNameLoaded = true
            }
        }
    }

    LaunchedEffect(Unit) {
        PlayerPreferences.getCharacterId(context).collect { saved ->
            characterId = saved
        }
    }

    LaunchedEffect(Unit) {
        PlayerPreferences.getCoins(context).collect { saved ->
            coins = saved
        }
    }

    // Box-ul principal care conține fundalul
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.start_game),
                contentScale = ContentScale.Crop
            )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Partea stângă: Titlu și Butoane
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    modifier = Modifier.padding(start = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Explore Romania",
                        fontSize = 68.sp, // Mărit pentru un impact mai mare
                        fontFamily = MarioFont,
                        fontWeight = FontWeight.Black,
                        style = androidx.compose.ui.text.TextStyle(
                            color = Color(0xFFC27A35),
                            shadow = Shadow(
                                color = Color(0xFF2D1A12),
                                offset = Offset(15f, 15f),
                                blurRadius = 4f
                            )
                        ),
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(26.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(26.dp)) {
                            HomeButton("Start Joc") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                CoroutineScope(Dispatchers.IO).launch {
                                    PlayerPreferences.savePlayerName(context, playerName)
                                    PlayerPreferences.saveCharacterId(context, characterId)
                                }
                                onPlayClick()
                            }
                            HomeButton("Colecție") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                onCollectionClick()
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(26.dp)) {
                            HomeButton("Cufere") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                onTreasureClick()
                            }
                            HomeButton("Ieșire") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                activity?.finishAffinity()
                            }
                        }
                    }
                }
            }

            // Partea dreaptă: Input nume și Carusel
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        placeholder = { Text("Nume", color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x99CCCCCC),
                            unfocusedContainerColor = Color(0x99CCCCCC)
                        ),
                        modifier = Modifier.width(300.dp).height(60.dp)
                    )

                    CharacterCarousel(
                        characterId = characterId,
                        onCharacterChange = { newId ->
                            characterId = newId
                            CoroutineScope(Dispatchers.IO).launch {
                                PlayerPreferences.saveCharacterId(context, newId)
                            }
                        }
                    )
                }
            }
        }

        // Indicator monede
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 30.dp, end = 30.dp)
                .background(color = Color(0xFF43271B), shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(32.dp).background(Color(0xFFC27A35), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "C", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Text(text = coins.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
} // AICI se închide HomeScreen

// FUNCȚIE SEPARATĂ (Top-level)
@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(200.dp).height(80.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(5.dp, Color(0xFF43271B)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC27A35))
    ) {
        Text(text = text, fontSize = 25.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Preview(name = "Phone Landscape", showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewHomeScreenPhone() {
    HomeScreen({}, {}, {})
}

@Preview(name = "Tablet Landscape", showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun PreviewHomeScreenTablet() {
    HomeScreen({}, {}, {})
}