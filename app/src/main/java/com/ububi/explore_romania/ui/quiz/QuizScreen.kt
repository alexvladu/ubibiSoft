package com.ububi.explore_romania.ui.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    onBackToGameBoard: () -> Unit
) {
    val questions = remember {
        listOf(
            // Geography questions adapted with 4 options (added one incorrect where needed)
            Question(
                "Care este suprafața aproximativă a României?",
                listOf("238.000 km²", "338.000 km²", "438.000 km²", "138.000 km²"),
                0
            ),
            Question(
                "În câte județe este împărțită România (fără București)?",
                listOf("39 de județe", "40 de județe", "41 de județe", "42 de județe"),
                2
            ),
            Question(
                "Cu câte țări se învecinează România?",
                listOf("4 țări", "5 țări", "6 țări", "7 țări"),
                1
            ),
            Question(
                "Care este cel mai mic județ din România ca suprafață?",
                listOf("Covasna", "Ilfov", "Giurgiu", "Tulcea"),
                1
            ),
            Question(
                "Care e cel mai mare județ din România ca suprafață?",
                listOf("Tulcea", "Suceava", "Timiș", "Iași"),
                2
            ),
            // History questions
            Question(
                "În ce an a avut loc Unirea Principatelor Române?",
                listOf("1848", "1859", "1918", "1947"),
                1
            ),
            Question(
                "Cine a fost primul rege al României?",
                listOf("Ferdinand", "Carol I", "Mihai I", "Alexandru Ioan Cuza"),
                1
            ),
            Question(
                "În ce an a reușit Mihai Viteazul unirea celor trei mari țări medievale?",
                listOf("1500", "1550", "1600", "1650"),
                2
            ),
            Question(
                "Când a fost proclamată independența de stat a României?",
                listOf("1859", "1877", "1918", "1944"),
                1
            ),
            Question(
                "Cine a fost domnitorul care a construit Castelul Peleș?",
                listOf("Ștefan cel Mare", "Vlad Țepeș", "Carol I", "Ferdinand I"),
                2
            )
        ).shuffled() // Shuffle questions for randomness
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var showResult by remember { mutableStateOf(false) }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != -1) {
            delay(2000)

            onBackToGameBoard()

        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showResult) {
            Text(
                text = "Quiz Terminată!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Scor: $score / ${questions.size}",
                fontSize = 24.sp,
                color = Color(0xFFB0BEC5)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onBackToGameBoard,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("Înapoi la Game Board", color = Color.White)
            }
        } else {
            val currentQuestion = questions[currentQuestionIndex]

            Text(
                text = currentQuestion.text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            currentQuestion.answers.forEachIndexed { index, answer ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == currentQuestion.correctIndex
                val backgroundColor = when {
                    selectedAnswer != -1 && isCorrect -> Color.Green
                    isSelected && !isCorrect -> Color.Red
                    else -> Color(0xFF424242)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(backgroundColor, RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFF616161), RoundedCornerShape(16.dp))
                        .clickable(enabled = selectedAnswer == -1) {
                            selectedAnswer = index
                            if (isCorrect) score++
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = answer,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            if (selectedAnswer != -1) {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            selectedAnswer = -1
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showResult = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text("Următoarea Întrebare", color = Color.White)
                    }
                }
            }
        }
    }
}