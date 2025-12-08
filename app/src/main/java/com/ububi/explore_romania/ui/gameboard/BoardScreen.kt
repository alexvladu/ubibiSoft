package com.ububi.explore_romania.ui.gameboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ububi.explore_romania.PlayerPreferences
import com.ububi.explore_romania.Routes
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@Composable
fun BoardScreen(navController: NavController) {
    val context = LocalContext.current

    var boardCountyIds by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    var countiesOnBoard by remember { mutableStateOf<List<County>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var pawnPosition by rememberSaveable { mutableIntStateOf(0) }
    var characterId by remember { mutableIntStateOf(1) }
    var showConfetti by remember { mutableStateOf(false) }

    var coinsEarnedThisGame by rememberSaveable { mutableIntStateOf(0) }

    var sessionResetDone by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.HOME)
    }

    LaunchedEffect(Unit) {
        PlayerPreferences.getCharacterId(context).collect { saved ->
            characterId = saved
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
                withContext(Dispatchers.Main) {
                    coinsEarnedThisGame = currentPending
                }
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
    val quizTimestampState = currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<Long>("quiz_result_timestamp", 0L)
        ?.collectAsState()
    val quizTimestamp = quizTimestampState?.value ?: 0L

    val quizResultTypeState = currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<String?>("quiz_result_type", null)
        ?.collectAsState()
    val quizResultType = quizResultTypeState?.value

    LaunchedEffect(quizTimestamp) {
        if (quizTimestamp != 0L && quizResultType != null) {

            withContext(Dispatchers.IO) {
                var addedCoins = 0
                when (quizResultType) {
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

            currentBackStackEntry?.savedStateHandle?.remove<Long>("quiz_result_timestamp")
            currentBackStackEntry?.savedStateHandle?.remove<String>("quiz_result_type")
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
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_category", "istorie")
                        navController.navigate(Routes.QUIZ)
                    },
                    onGeographyClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_county", currentCounty.name)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selected_category", "geografie")
                        navController.navigate(Routes.QUIZ)
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
        }
    }
}