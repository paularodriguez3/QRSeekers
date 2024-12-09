package com.qrseekers.data

data class Question(
    val id: String,
    val text: String,
    val type: String, // multichoice, text,
    val points: Int,
    val options: List<String>? = null, // Optional for multiple choice
    val correctAnswer: Any,
    val imageUrl: String? = null, // Optional for image-based questions
)
