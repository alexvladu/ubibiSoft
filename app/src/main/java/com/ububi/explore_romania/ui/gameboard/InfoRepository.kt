package com.ububi.explore_romania.ui.gameboard

import android.content.Context
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object InfoRepository {

    fun getInfoForCounty(context: Context, countyId: Int): String {
        try {
            val inputStream = context.assets.open("info/info_judete.json")
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val countyObj = jsonArray.getJSONObject(i)

                if (countyObj.has("id") && countyObj.getInt("id") == countyId) {
                    return if (countyObj.has("descriere")) {
                        countyObj.getString("descriere")
                    } else {
                        "Descrierea urmează să fie adăugată."
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Eroare la citirea informațiilor."
        }

        return "Nu există încă informații pentru acest județ (ID: $countyId)."
    }
}