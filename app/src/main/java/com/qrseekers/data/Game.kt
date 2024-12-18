package com.qrseekers.data

import androidx.compose.ui.graphics.Color
import com.qrseekers.R

data class Game(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val imageRes: String = "", // Store the resource name or URL as a string
    val color: Long = 0xFFBFDBFE, // Store color as a Long (hex format)
    val zones: List<String> = emptyList() // Zones remain as a list of strings
) {
    // Helper methods to convert Firestore values to UI-compatible types
    fun getImageResId(): Int {
        // You need a map of resource names to resource IDs
        return when (imageRes) {
            "las_palmas_image" -> R.drawable.las_palmas_image
            "prague_image" -> R.drawable.prague_image
            else -> R.drawable.indoor_image // Fallback image
        }
    }

    fun getColor(): Color {
        return Color(color)
    }
}
