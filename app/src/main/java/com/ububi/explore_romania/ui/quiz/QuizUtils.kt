package com.ububi.explore_romania.ui.quiz

import android.content.Context
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.Normalizer
import kotlin.random.Random

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int,
    val hint: String? = null
)

object QuizRepository {

    private fun normalizeString(input: String): String {
        return input.lowercase()
            .replace("ș", "s")
            .replace("ş", "s")
            .replace("ț", "t")
            .replace("ţ", "t")
            .replace("ă", "a")
            .replace("â", "a")
            .replace("î", "i")
            .trim()
    }

    fun getRandomQuestionForCounty(
        context: Context,
        countyName: String,
        category: String
    ): Question {
       val fileName = if (category.contains("istorie", ignoreCase = true))
            "intrebari_istorie.json"
        else
            "intrebari_geografie.json"

        try {
            val inputStream = context.assets.open("intrebari/$fileName")
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

            val jsonArray = JSONArray(jsonString)

            val targetCounty = normalizeString(countyName)

            for (i in 0 until jsonArray.length()) {
                val countyObj = jsonArray.getJSONObject(i)
                val jsonCountyName = countyObj.getString("judet")

                if (normalizeString(jsonCountyName) == targetCounty) {

                    val questionsArray = countyObj.getJSONArray("intrebari")

                    if (questionsArray.length() > 0) {
                        val randomIndex = Random.nextInt(questionsArray.length())
                        val qObj = questionsArray.getJSONObject(randomIndex)

                        val text = qObj.getString("text")
                        val correctString = qObj.getString("raspuns_corect")
                        val hint = if (qObj.has("hint")) qObj.getString("hint") else null

                        val answersJson = qObj.getJSONArray("raspunsuri")
                        val answersList = mutableListOf<String>()
                        for (j in 0 until answersJson.length()) {
                            answersList.add(answersJson.getString(j))
                        }

                        answersList.shuffle()

                        var correctIndex = answersList.indexOf(correctString)

                        if (correctIndex == -1) {
                            answersList.add(correctString)
                            answersList.shuffle()
                            correctIndex = answersList.indexOf(correctString)
                        }

                        return Question(text, answersList, correctIndex, hint)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Question("Eroare la citire: ${e.message}", listOf("Ok"), 0)
        }

        return Question(
            "Nu am găsit întrebări pentru $countyName ($category).",
            listOf("Înapoi"),
            0
        )
    }
}