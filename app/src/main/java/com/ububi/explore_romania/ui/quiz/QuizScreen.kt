package com.ububi.explore_romania.ui.quiz

import android.graphics.BitmapFactory
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ububi.explore_romania.MusicManager
import com.ububi.explore_romania.MusicTrack
import com.ububi.explore_romania.SoundEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun QuizScreen(navController: NavController) {
    val context = LocalContext.current

    val previousStack = navController.previousBackStackEntry
    val countyName = previousStack?.savedStateHandle?.get<String>("selected_county") ?: "București"
    val countyId = previousStack?.savedStateHandle?.get<Int>("selected_county_id") ?: 1
    val category = previousStack?.savedStateHandle?.get<String>("selected_category") ?: "geografie"

    var currentQuestion by remember { mutableStateOf<Question?>(null) }
    var isLoading by remember { mutableStateOf(true) }


    var countyImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(countyId) {
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("imagini_judete/$countyId.jpg")
                val bitmap = BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                withContext(Dispatchers.Main) {
                    countyImageBitmap = bitmap
                }
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Box(modifier = Modifier.padding(32.dp)) {
                    CircularProgressIndicator(color = Color(0xFF311B92))
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.85f)
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // 1. IMAGINE JUDEȚ
                    if (countyImageBitmap != null) {
                        Image(
                            bitmap = countyImageBitmap!!,
                            contentDescription = countyName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 2. ÎNTREBAREA ȘI RĂSPUNSURILE
                    currentQuestion?.let { question ->

                        Text(
                            text = question.text,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        question.answers.forEachIndexed { index, answer ->
                            val isSelectedAsFinal = selectedAnswerIndex == index
                            val isAlreadyWrong = wrongAnswers.contains(index)
                            val isCorrect = index == question.correctIndex

                            val backgroundColor = when {
                                quizFinished && isCorrect -> Color(0xFF4CAF50)
                                isSelectedAsFinal && isCorrect -> Color(0xFF4CAF50)
                                isSelectedAsFinal && !isCorrect -> Color(0xFFF44336)
                                isAlreadyWrong -> Color(0xFFE57373)
                                else -> Color.White
                            }

                            val textColor = if (backgroundColor == Color.White) Color.Black else Color.White
                            val borderColor = if (backgroundColor == Color.White) Color.Gray else Color.Transparent
                            val isEnabled = !quizFinished && !isAlreadyWrong

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .background(backgroundColor, RoundedCornerShape(12.dp))
                                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
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
                                    color = textColor,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // 3. HINT
                        if (attempts > 0) {
                            val hintText = question.hint ?: "Mai încearcă o dată!"
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFF141280), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "HINT",
                                        color = Color(0xFF141280),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = hintText,
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}