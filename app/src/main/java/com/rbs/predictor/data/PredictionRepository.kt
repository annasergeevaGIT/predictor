package com.rbs.predictor.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PredictionRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val predictionsCollection = firestore.collection("predictions")

    // get today's date in YYYY-MM-DD format
    private fun getTodayDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    suspend fun getDailyPrediction(): PredictionResult {
        return try {
            val todayDate = getTodayDateString()
            val document = predictionsCollection.document(todayDate).get().await()

            if (document.exists()) {
                val coin = document.getString("coin")
                if (coin != null) {
                    PredictionResult.Success(todayDate, coin)
                } else {
                    PredictionResult.Error("Prediction data format incorrect for $todayDate")
                }
            } else {
                PredictionResult.NotFound("No prediction found for $todayDate")
            }
        } catch (e: Exception) {
            PredictionResult.Error("Error fetching prediction: ${e.message}")
        }
    }
}

// Sealed class to represent the result of fetching prediction from the repository
sealed class PredictionResult {
    data class Success(val date: String, val coin: String) : PredictionResult()
    data class Error(val message: String) : PredictionResult()
    data class NotFound(val message: String) : PredictionResult()
}