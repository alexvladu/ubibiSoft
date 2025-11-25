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
import com.ububi.explore_romania.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BoardScreen(navController: NavController) {
    val context = LocalContext.current
    var boardCountyIds by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }

    var countiesOnBoard by remember { mutableStateOf<List<County>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    var pawnPosition by rememberSaveable { mutableIntStateOf(0) }

    val currentBackStackEntry = navController.currentBackStackEntry
    val quizTimestampState = currentBackStackEntry?.savedStateHandle
        ?.getStateFlow<Long>("quiz_result_timestamp", 0L)
        ?.collectAsState()
    val quizTimestamp = quizTimestampState?.value ?: 0L

    LaunchedEffect(quizTimestamp) {
        if (quizTimestamp != 0L) {
            if (pawnPosition < 16) {
                pawnPosition++
            }
            currentBackStackEntry?.savedStateHandle?.remove<Long>("quiz_result_timestamp")
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