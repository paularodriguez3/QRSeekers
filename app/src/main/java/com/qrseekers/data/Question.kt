package com.qrseekers.data

data class Question(
    val id: String,
    val text: String,
    val type: String, // multichoice,
    val options: List<String>? = null, // Optional for multiple choice
    val points: Int,
    val correctAnswer: Any,
    val imageUrl: String? = null, // Optional for image-based questions
)
