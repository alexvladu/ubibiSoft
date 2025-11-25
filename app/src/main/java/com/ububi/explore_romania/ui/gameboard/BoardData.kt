package com.ububi.explore_romania.ui.gameboard

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.BufferedReader
import java.io.InputStreamReader

data class County(
    val id: Int,
    val name: String,
    val image: ImageBitmap?
)

fun loadRandomCounties(context: Context): List<County> {
    val allCountiesList = mutableListOf<Pair<Int, String>>()

    try {
        val inputStream = context.assets.open("judete.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.readLine()

        reader.forEachLine { line ->
            val cleanLine = line.trim()
            if (cleanLine.isNotEmpty()) {
                val tokens = cleanLine.split(",")

                if (tokens.size >= 2) {
                    val name = tokens[0].trim()
                    val idString = tokens[1].trim()

                    val id = idString.toIntOrNull()

                    if (id != null && name.isNotEmpty()) {
                        allCountiesList.add(id to name)
                    }
                }
            }
        }
        reader.close()
    } catch (e: Exception) {
        Log.e("BoardData", "Critical error reading CSV: ${e.message}")
        e.printStackTrace()
    }

    Log.d("BoardData", "Read ${allCountiesList.size} counties from CSV.")

    if (allCountiesList.size < 16) {
        val testList = mutableListOf<County>()
        for (i in 1..16) testList.add(County(i, "Test County $i", null))
        return testList
    }

    // Select 16 random counties
    val selectedCounties = allCountiesList.shuffled().take(16)

    // Load images corresponding to IDs
    return selectedCounties.map { (id, name) ->
        val bitmap = loadImageFromAssets(context, "imagini_judete/$id.jpg")
        County(id, name, bitmap)
    }
}

// Helper function to load an image from assets
fun loadImageFromAssets(context: Context, path: String): ImageBitmap? {
    return try {
        val inputStream = context.assets.open(path)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        Log.e("BoardData", "Image not found at path: $path")
        null
    }
}