package com.qrseekers.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.Question
import com.qrseekers.data.Zone
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class QuizViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _questions = mutableStateOf<List<Question>>(emptyList())
    val questions: State<List<Question>> get() = _questions

    private val _answers = mutableStateOf<Map<String, String>>(emptyMap())
    val answers: State<Map<String, String>> get() = _answers

    private val logTag = "QuizViewModel"

    // Reinicia el estado de preguntas y respuestas
    fun resetQuiz() {
        _questions.value = emptyList()
        _answers.value = emptyMap()
    }

    fun loadQuestions(zoneId: String) {
        viewModelScope.launch {
            try {
                resetQuiz() // Limpia el estado antes de cargar nuevas preguntas

                val zoneDoc = firestore.collection("Zones").document(zoneId).get().await()
                val zone = zoneDoc.toObject(Zone::class.java)
                val questionIds = zone?.questions ?: emptyList()

                val questionsList = mutableListOf<Question>()
                for (questionId in questionIds) {
                    val questionDoc = firestore.collection("Questions").document(questionId).get().await()
                    val question = questionDoc.toObject(Question::class.java)
                    question?.let {
                        it.id = questionId
                        questionsList.add(it)
                        Log.d(logTag, "Loaded question: ${question.text}")
                    }
                }

                _questions.value = questionsList

            } catch (e: Exception) {
                Log.e(logTag, "Error loading questions: ${e.localizedMessage}")
            }
        }
    }

    fun updateAnswer(questionId: String, answer: String) {
        _answers.value = _answers.value.toMutableMap().apply {
            this[questionId] = answer
        }
    }

    fun checkAnswers(onResult: (String, Int, Map<String, Boolean>) -> Unit) {
        viewModelScope.launch {
            try {
                val unansweredQuestions = questions.value.filterNot { it.id in _answers.value.keys }
                if (unansweredQuestions.isNotEmpty()) {
                    onResult("Unanswered questions: ${unansweredQuestions.size}", 0, emptyMap())
                    return@launch
                }

                var totalPoints = 0
                val correctness = mutableMapOf<String, Boolean>()
                for (question in questions.value) {
                    val userAnswer = _answers.value[question.id].orEmpty()
                    val isCorrect = userAnswer.equals(question.correctAnswer.toString(), true)
                    correctness[question.id] = isCorrect
                    if (isCorrect) totalPoints += question.points
                }

                onResult("All questions answered.", totalPoints, correctness)
            } catch (e: Exception) {
                Log.e(logTag, "Error checking answers: ${e.localizedMessage}")
                onResult("Error: ${e.localizedMessage}", 0, emptyMap())
            }
        }
    }
}
