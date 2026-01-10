package com.ububi.explore_romania.ui.gameboard

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ububi.explore_romania.PlayerPreferences
import com.ububi.explore_romania.Routes
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun BoardScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var boardCountyIds by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    var countiesOnBoard by remember { mutableStateOf<List<County>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var pawnPosition by rememberSaveable { mutableIntStateOf(0) }
    var characterId by remember { mutableIntStateOf(1) }
    var showConfetti by remember { mutableStateOf(false) }

    var coinsEarnedThisGame by rememberSaveable { mutableIntStateOf(0) }
    var sessionResetDone by rememberSaveable { mutableStateOf(false) }

    // State-uri pentru fereastra de informații despre județ
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedInfoCounty by remember { mutableStateOf<County?>(null) }
    var infoText by remember { mutableStateOf("") }
    var locationMapBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // --- STATE-URI PENTRU DIALOGURI TUTORIAL ---
    var showRulesDialog by remember { mutableStateOf(false) }
    var showCountyInfoTipDialog by remember { mutableStateOf(false) }


    LaunchedEffect(selectedInfoCounty) {
        locationMapBitmap = null
        selectedInfoCounty?.let { county ->
            withContext(Dispatchers.IO) {
                val bitmap = loadBitmapFromAssets(context, "localizare_harta/${county.id}.png")
                withContext(Dispatchers.Main) {
                    locationMapBitmap = bitmap
                }
            }
        }
    }

    LaunchedEffect(Unit) { MusicManager.playTrack(MusicTrack.BOARD) }

    LaunchedEffect(Unit) {
        PlayerPreferences.getCharacterId(context).collect { saved -> characterId = saved }
    }

    // --- LOGICA DE AFIȘARE A REGULILOR (Doar o singură dată) ---
    LaunchedEffect(Unit) {
        val seenRules = PlayerPreferences.getSeenRulesTutorial(context).first()
        if (!seenRules) {
            showRulesDialog = true
        }
    }

    LaunchedEffect(Unit) {
        if (!sessionResetDone) {
            withContext(Dispatchers.IO) {
                PlayerPreferences.resetGameSession(context)
                coinsEarnedThisGame = 0
            }
            sessionResetDone = true
        } else {
            withContext(Dispatchers.IO) {
                val currentPending = PlayerPreferences.getPendingCoins(context).first()
                withContext(Dispatchers.Main) { coinsEarnedThisGame = currentPending }
            }
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val loadedData = loadBoardData(
                context,
                if (boardCountyIds.isNotEmpty()) boardCountyIds else null
            )
            withContext(Dispatchers.Main) {
                countiesOnBoard = loadedData
                if (boardCountyIds.isEmpty()) {
                    boardCountyIds = loadedData.map { it.id }
                }
                isLoading = false
            }
        }
    }


    val currentBackStackEntry = navController.currentBackStackEntry

    val quizTimestamp by currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<Long>("quiz_result_timestamp", 0L)
        ?.collectAsState() ?: remember { mutableStateOf(0L) }

    LaunchedEffect(quizTimestamp) {
        if (quizTimestamp != 0L) {
            val resultType = currentBackStackEntry?.savedStateHandle?.get<String>("quiz_result_type")

            if (resultType != null) {
                withContext(Dispatchers.IO) {
                    var addedCoins = 0
                    when (resultType) {
                        "PERFECT" -> {
                            val currentStreakValue = PlayerPreferences.getCurrentStreak(context).first()
                            val newStreak = currentStreakValue + 1
                            addedCoins = 2 + (newStreak / 2)
                            PlayerPreferences.saveCurrentStreak(context, newStreak)
                            withContext(Dispatchers.Main) { showConfetti = true }
                        }
                        "RECOVERY" -> {
                            PlayerPreferences.saveCurrentStreak(context, 0)
                            addedCoins = 1
                            withContext(Dispatchers.Main) { showConfetti = true }
                        }
                        else -> {
                            PlayerPreferences.saveCurrentStreak(context, 0)
                            addedCoins = 0
                        }
                    }

                    if (addedCoins > 0) {
                        val currentPendingCoins = PlayerPreferences.getPendingCoins(context).first()
                        val newPendingTotal = currentPendingCoins + addedCoins
                        PlayerPreferences.savePendingCoins(context, newPendingTotal)
                        withContext(Dispatchers.Main) {
                            coinsEarnedThisGame = newPendingTotal
                        }
                    }
                }


                pawnPosition++

                // --- VERIFICARE AFIȘARE INFO TIP (După prima întrebare) ---
                if (pawnPosition == 1) {
                    val seenInfoTip = PlayerPreferences.getSeenCountyInfoTutorial(context).first()
                    if (!seenInfoTip) {
                        delay(500) // Mic delay să nu apară brusc peste confetti
                        showCountyInfoTipDialog = true
                    }
                }

                delay(1500)
                showConfetti = false

                if (countiesOnBoard.isNotEmpty() && pawnPosition >= countiesOnBoard.size) {
                    withContext(Dispatchers.IO) {
                        PlayerPreferences.finalizePendingCoins(context)
                    }
                    navController.navigate(Routes.CHEST) {
                        popUpTo(Routes.GAME_BOARD) { inclusive = true }
                    }
                }

                currentBackStackEntry.savedStateHandle["quiz_result_timestamp"] = 0L
            }
        }
    }


    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (countiesOnBoard.isNotEmpty()) {
                val safeIndex = pawnPosition % countiesOnBoard.size
                val currentCounty = countiesOnBoard[safeIndex]

                GameBoard(
                    counties = countiesOnBoard,
                    pawnPosition = pawnPosition,
                    characterId = characterId,
                    showConfetti = showConfetti,
                    pendingCoins = coinsEarnedThisGame,
                    onHistoryClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_county", currentCounty.name)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_county_id", currentCounty.id)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_category", "istorie")
                        navController.navigate(Routes.QUIZ)
                    },
                    onGeographyClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_county", currentCounty.name)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_county_id", currentCounty.id)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_category", "geografie")
                        navController.navigate(Routes.QUIZ)
                    },
                    onCardClick = { clickedCounty ->
                        selectedInfoCounty = clickedCounty
                        infoText = InfoRepository.getInfoForCounty(context, clickedCounty.id)
                        showInfoDialog = true
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Eroare la încărcarea datelor", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }

            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            // --- FEREASTRA INFORMATII (ALERT DIALOG - ORIGINAL) ---
            if (showInfoDialog && selectedInfoCounty != null) {
                AlertDialog(
                    onDismissRequest = { showInfoDialog = false },
                    containerColor = Color(0xFFEEEEEE),
                    title = {
                        Text(
                            text = selectedInfoCounty!!.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            if (selectedInfoCounty!!.image != null) {
                                Image(
                                    bitmap = selectedInfoCounty!!.image!!,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 200.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            Text(
                                text = infoText,
                                fontSize = 16.sp,
                                color = Color.Black,
                                lineHeight = 22.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                            )

                            if (locationMapBitmap != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Localizare:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    bitmap = locationMapBitmap!!,
                                    contentDescription = "Harta localizare",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showInfoDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF311B92))
                        ) {
                            Text("Închide", color = Color.White)
                        }
                    }
                )
            }

            // --- DIALOGUL PENTRU REGULI (PRIMUL MESAJ - APARTE DOAR LA PRIMA DESCHIDERE) ---
            if (showRulesDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showRulesDialog = false
                        scope.launch(Dispatchers.IO) {
                            PlayerPreferences.setSeenRulesTutorial(context, true)
                        }
                    },
                    containerColor = Color(0xFFEEEEEE),
                    title = {
                        Text(
                            text = "Regulile Jocului",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF311B92)
                        )
                    },
                    text = {
                        Text(
                            text = "Pentru fiecare întrebare la care ai răspuns corect primești 2 puncte.\n\n" +
                                    "Dacă răspunzi greșit, nu îți face griji: vei primi un indiciu și ai șansa să mai încerci o dată - pentru un răspuns corect vei primi un punct.\n\n" +
                                    "Dacă răspunzi corect la minim două întrebări consecutive vei primi un bonus!",
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Justify
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showRulesDialog = false
                                scope.launch(Dispatchers.IO) {
                                    PlayerPreferences.setSeenRulesTutorial(context, true)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF311B92))
                        ) {
                            Text("Am înțeles", color = Color.White)
                        }
                    }
                )
            }

            // --- DIALOGUL PENTRU INFO JUDEȚ (APARTE DUPĂ PRIMA ÎNTREBARE) ---
            if (showCountyInfoTipDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showCountyInfoTipDialog = false
                        scope.launch(Dispatchers.IO) {
                            PlayerPreferences.setSeenCountyInfoTutorial(context, true)
                        }
                    },
                    containerColor = Color(0xFFEEEEEE),
                    title = {
                        Text(
                            text = "Sfat util! 💡",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFFF57C00)
                        )
                    },
                    text = {
                        Text(
                            text = "Apasă pe un județ după ce ai răspuns la întrebare pentru a descoperi informații despre acesta.",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showCountyInfoTipDialog = false
                                scope.launch(Dispatchers.IO) {
                                    PlayerPreferences.setSeenCountyInfoTutorial(context, true)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00))
                        ) {
                            Text("Super!", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}

suspend fun loadBitmapFromAssets(context: android.content.Context, path: String): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            context.assets.open(path).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            null
        }
    }
}