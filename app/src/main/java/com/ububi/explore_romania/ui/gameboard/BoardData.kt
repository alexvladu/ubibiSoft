package com.ububi.explore_romania.ui.gameboard

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.BufferedReader
import java.io.InputStreamReader

data class County(
    val id: Int,
    val name: String,
    val image: ImageBitmap?
)


fun loadBoardData(context: Context, savedIds: List<Int>? = null): List<County> {
    val allMetadata = readAllCountiesMetadata(context)

    if (allMetadata.isEmpty()) {
        val testList = mutableListOf<County>()
        for (i in 1..16) testList.add(County(i, "Test $i", null))
        return testList
    }

    val selectedMetadata = if (savedIds != null && savedIds.isNotEmpty()) {
        savedIds.mapNotNull { id ->
            allMetadata.find { it.first == id }
        }
    } else {
        allMetadata.shuffled().take(16)
    }

    return selectedMetadata.map { (id, name) ->
        val bitmap = loadImageFromAssets(context, "imagini_judete/$id.jpg")
        County(id, name, bitmap)
    }
}

private fun readAllCountiesMetadata(context: Context): List<Pair<Int, String>> {
    val list = mutableListOf<Pair<Int, String>>()
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
                    val id = tokens[1].trim().toIntOrNull()
                    if (id != null && name.isNotEmpty()) {
                        list.add(id to name)
                    }
                }
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list
}

fun loadImageFromAssets(context: Context, path: String): ImageBitmap? {
    return try {
        val inputStream = context.assets.open(path)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}