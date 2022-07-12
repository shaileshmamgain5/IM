package io.interviewmate.behavioural.util

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun getSampleQuestions(context: Context): JSONArray {
    val jsonString = getJsonDataFromAsset(context, "csvjson.json")
    return JSONArray(jsonString)
}