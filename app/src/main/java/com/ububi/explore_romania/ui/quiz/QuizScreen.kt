package com.ububi.explore_romania.ui.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import com.ububi.explore_romania.SoundEffect
import com.ububi.explore_romania.PlayerPreferences
import com.ububi.explore_romania.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@Composable
fun QuizScreen(navController: NavController) {
    val context = LocalContext.current

    val previousStack = navController.previousBackStackEntry
    val countyName = previousStack?.savedStateHandle?.get<String>("selected_county") ?: "București"
    val category = previousStack?.savedStateHandle?.get<String>("selected_category") ?: "geografie"

    var currentQuestion by remember { mutableStateOf<Question?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var pendingCoins by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val coins = PlayerPreferences.getPendingCoins(context).first()
            withContext(Dispatchers.Main) {
                pendingCoins = coins
            }
        }
    }

    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    val wrongAnswers = remember { mutableStateListOf<Int>() }
    var attempts by remember { mutableIntStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        MusicManager.playTrack(MusicTrack.QUESTION)
        withContext(Dispatchers.IO) {
            val loaded = QuizRepository.getRandomQuestionForCounty(context, countyName, category)
            withContext(Dispatchers.Main) {
                currentQuestion = loaded
                isLoading = false
            }
        }
    }

    LaunchedEffect(quizFinished) {
        if (quizFinished) {
            delay(1500)
            val resultType = if (wrongAnswers.isEmpty()) {
                "PERFECT"
            } else if (wrongAnswers.size == 1 && selectedAnswerIndex == currentQuestion!!.correctIndex) {
                "RECOVERY"
            } else {
                "FAILURE"
            }

            navController.previousBackStackEntry?.savedStateHandle?.set("quiz_result_timestamp", System.currentTimeMillis())
            navController.previousBackStackEntry?.savedStateHandle?.set("quiz_result_type", resultType)
            navController.popBackStack()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF212121)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF212121))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            currentQuestion?.let { question ->

                Text(
                    text = category.replaceFirstChar { it.uppercase() },
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = question.text,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                question.answers.forEachIndexed { index, answer ->
                    val isSelectedAsFinal = selectedAnswerIndex == index
                    val isAlreadyWrong = wrongAnswers.contains(index)
                    val isCorrect = index == question.correctIndex

                    val backgroundColor = when {
                        quizFinished && isCorrect -> Color(0xFF4CAF50)
                        isSelectedAsFinal && isCorrect -> Color(0xFF4CAF50)
                        isSelectedAsFinal && !isCorrect -> Color(0xFFF44336)
                        isAlreadyWrong -> Color(0xFFB71C1C)
                        else -> Color(0xFF424242)
                    }

                    val isEnabled = !quizFinished && !isAlreadyWrong

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(backgroundColor, RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .clickable(enabled = isEnabled) {
                                if (index == question.correctIndex) {
                                    selectedAnswerIndex = index
                                    MusicManager.playSoundEffect(SoundEffect.CORRECT)
                                    quizFinished = true
                                } else {
                                    MusicManager.playSoundEffect(SoundEffect.WRONG)

                                    if (attempts == 0) {
                                        wrongAnswers.add(index)
                                        attempts++
                                    } else {
                                        selectedAnswerIndex = index
                                        quizFinished = true
                                    }
                                }
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = answer,
                            fontSize = 16.sp,
                            color = if (isAlreadyWrong) Color.Gray else Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (attempts > 0) {
                    val hintText = question.hint ?: "Mai încearcă o dată!"
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF37474F), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFFFC107), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "HINT",
                                color = Color(0xFFFFC107),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = hintText,
                                color = Color.White,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Coin",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$pendingCoins",
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}