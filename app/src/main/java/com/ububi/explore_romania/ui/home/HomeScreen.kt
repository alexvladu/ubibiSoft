package com.ububi.explore_romania.ui.home

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val MarioFont = FontFamily(
        Font(R.font.retromario)
    )
    val context = LocalContext.current
    val activity = context as? Activity

    var playerName by remember { mutableStateOf("") }
    var characterId by remember { mutableIntStateOf(1) }
    var coins by remember { mutableIntStateOf(67) }
    var isNameLoaded by remember { mutableStateOf(false) }

    // Play home music when screen appears
    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.HOME)
    }

    // Load player name only once on initial load
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


    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        DvdBackground(
            imageRes = R.drawable.homescreen_bg
        )


        Row(
            modifier = Modifier.fillMaxSize()
        ) {


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    modifier = Modifier.padding(start = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "Explore Romania",
                        fontSize = 48.sp,
                        fontFamily = MarioFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(26.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(26.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HomeButton("Play") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                CoroutineScope(Dispatchers.IO).launch {
                                    PlayerPreferences.savePlayerName(context, playerName)
                                    PlayerPreferences.saveCharacterId(context, characterId)
                                }
                                onPlayClick()
                            }

                            HomeButton("Collection") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                onCollectionClick()
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(26.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            HomeButton("Treasure") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                onTreasureClick()
                            }


                            HomeButton("Exit") {
                                MusicManager.playSoundEffect(SoundEffect.BUTTON)
                                CoroutineScope(Dispatchers.IO).launch {
                                    PlayerPreferences.savePlayerName(context, playerName)
                                    PlayerPreferences.saveCharacterId(context, characterId)
                                }
                                activity?.finishAffinity()
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {

                    // Name input field - now with transparent gray background
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        placeholder = {
                            Text(
                                "Who am I?",
                                fontSize = 20.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 28.sp,
                            color = Color.Black
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedContainerColor = Color(0x99CCCCCC),
                            unfocusedContainerColor = Color(0x99CCCCCC),
                            cursorColor = Color.Black
                        ),
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp)
                    )

                    // Character carousel selector
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

        // Coin display in bottom-right corner
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
            // Circle placeholder for coin image
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
                text = coins.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(5.dp, Color(0xFFF57C00)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFB74D)
        )
    ) {
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Preview(
    name = "Phone Landscape",
    showBackground = true,
    widthDp = 720,
    heightDp = 360
)
@Composable
fun PreviewHomeScreenPhone() {
    HomeScreen({}, {}, {})
}

@Preview(
    name = "Tablet Landscape",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800
)
@Composable
fun PreviewHomeScreenTablet() {
    HomeScreen({}, {}, {})
}
