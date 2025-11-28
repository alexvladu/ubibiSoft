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

    // Frozen coins earned this game (captured before finalize, for display at end)
    var coinsEarnedThisGame by rememberSaveable { mutableIntStateOf(0) }

    // Track if session reset has been done to prevent multiple resets
    var sessionResetDone by rememberSaveable { mutableStateOf(false) }

    // Play board music when screen appears
    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.BOARD)
    }

    // Load character ID
    LaunchedEffect(Unit) {
        PlayerPreferences.getCharacterId(context).collect { saved ->
            characterId = saved
        }
    }

    // Reset game session only once per game
    LaunchedEffect(Unit) {
        if (!sessionResetDone) {
            android.util.Log.d("BoardScreen", "🎮 BoardScreen pornit - resetare sesiune (PRIMA RULARE)")
            withContext(Dispatchers.IO) {
                PlayerPreferences.resetGameSession(context)
            }
            sessionResetDone = true
        } else {
            android.util.Log.d("BoardScreen", "🎮 BoardScreen recompus - reset deja făcut, păstrez progresul")
        }
    }

    val currentBackStackEntry = navController.currentBackStackEntry
    val quizTimestampState = currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<Long>("quiz_result_timestamp", 0L)
        ?.collectAsState()
    val quizTimestamp = quizTimestampState?.value ?: 0L

    val quizCorrectState = currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<Boolean>("quiz_answer_correct", false)
        ?.collectAsState()
    val wasAnswerCorrect = quizCorrectState?.value ?: false

    LaunchedEffect(quizTimestamp) {
        if (quizTimestamp != 0L) {
            android.util.Log.d("BoardScreen", "📝 Procesare răspuns la poziția $pawnPosition, corect: $wasAnswerCorrect")

            // Update streak and coins based on answer
            withContext(Dispatchers.IO) {
                if (wasAnswerCorrect) {
                    // Read current streak from DataStore and increment it
                    val currentStreakValue = PlayerPreferences.getCurrentStreak(context).first()
                    val newStreak = currentStreakValue + 1
                    android.util.Log.d("BoardScreen", "🔥 Streak: $currentStreakValue → $newStreak")

                    // Calculate coins: 2 points + floor(newStreak/2)
                    val coinsEarned = 2 + (newStreak / 2)
                    android.util.Log.d("BoardScreen", "💎 Coins câștigați: $coinsEarned (2 + ${newStreak/2})")

                    // Save the new streak
                    PlayerPreferences.saveCurrentStreak(context, newStreak)

                    // Read current pending coins from DataStore and add the earned coins
                    val currentPendingCoins = PlayerPreferences.getPendingCoins(context).first()
                    val newPendingCoins = currentPendingCoins + coinsEarned
                    android.util.Log.d("BoardScreen", "💰 Pending: $currentPendingCoins → $newPendingCoins")
                    PlayerPreferences.savePendingCoins(context, newPendingCoins)

                    // Show confetti
                    withContext(Dispatchers.Main) {
                        showConfetti = true
                    }
                } else {
                    android.util.Log.d("BoardScreen", "❌ Răspuns greșit - resetare streak")
                    // Reset streak on wrong answer (but keep pending coins)
                    PlayerPreferences.saveCurrentStreak(context, 0)
                }
            }

            pawnPosition++
            android.util.Log.d("BoardScreen", "👣 Poziție nouă: $pawnPosition")

            // Reset confetti after animation
            delay(1500)
            showConfetti = false

            currentBackStackEntry?.savedStateHandle?.remove<Long>("quiz_result_timestamp")
            currentBackStackEntry?.savedStateHandle?.remove<Boolean>("quiz_answer_correct")
        }
    }

    // Finalize coins when game is finished
    LaunchedEffect(pawnPosition) {
        if (pawnPosition >= 16) {
            android.util.Log.d("BoardScreen", "🏁 JOC TERMINAT la poziția $pawnPosition")
            withContext(Dispatchers.IO) {
                // IMPORTANT: Capture the value BEFORE finalize resets it to 0
                val finalPending = PlayerPreferences.getPendingCoins(context).first()
                android.util.Log.d("BoardScreen", "🎉 Pending coins final ÎNAINTE de finalizare: $finalPending")

                // Save the value for display (before it gets reset)
                withContext(Dispatchers.Main) {
                    coinsEarnedThisGame = finalPending
                }

                // Now finalize (which resets pending to 0 and adds to total)
                PlayerPreferences.finalizePendingCoins(context)
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

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            if (countiesOnBoard.isNotEmpty()) {
                GameBoard(
                    counties = countiesOnBoard,
                    pawnPosition = pawnPosition,
                    characterId = characterId,
                    showConfetti = showConfetti,
                    pendingCoins = coinsEarnedThisGame,  // Use frozen value for display
                    onHistoryClick = { navController.navigate(Routes.QUIZ) },
                    onGeographyClick = { navController.navigate(Routes.QUIZ) },
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