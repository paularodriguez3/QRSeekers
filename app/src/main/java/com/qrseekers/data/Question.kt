package com.qrseekers.data

data class Question(
    var id: String = "",
    val text: String = "",
    val type: String = "", // e.g., "multi" or "text"
    val points: Int = 0,
    val options: List<String>? = null, // Optional for multiple-choice questions
    val correctAnswer: Any? = null,
    val imageUrl: String? = null // Optional for image-based questions
){
    override fun toString(): String {
        return "Question(id='$id', text='$text', points=$points, options=$options, imageUrl=$imageUrl)"
    }
}

