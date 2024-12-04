package com.qrseekers.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qrseekers.data.FirestoreInstance
import com.qrseekers.data.Question
import kotlinx.coroutines.launch
import com.google.firebase.firestore.DocumentReference

open class QuizViewModel : ViewModel() {

    private val db = FirestoreInstance.db

    open val questions = mutableStateListOf<Question>()
    var teamScore = mutableStateOf(0)

    fun loadQuestions(gameId: String) {
        db.collection("games")
            .document(gameId)
            .collection("questions")
            .get()
            .addOnSuccessListener { result ->
                questions.clear()
                for (document in result) {
                    val question = document.toObject(Question::class.java)
                    questions.add(question)
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    fun checkAnswersAndUpdateScore(gameId: String, teamId: String, answers: Map<String, String>) {
        var totalScore = 0
        questions.forEach { question ->
            val userAnswer = answers[question.id]
            if (userAnswer == question.correctAnswer) {
                totalScore += 1
            }
        }
        updateTeamScore(gameId, teamId, totalScore)
    }

    private fun updateTeamScore(gameId: String, teamId: String, score: Int) {
        val teamRef: DocumentReference = db.collection("teams").document(teamId)
        teamRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val currentScore = document.getLong("score") ?: 0
                val newScore = currentScore.toInt() + score
                teamRef.update("score", newScore)
                    .addOnSuccessListener {
                        teamScore.value = newScore
                    }
                    .addOnFailureListener { exception ->
                        // Handle error
                    }
            }
        }
    }


}
