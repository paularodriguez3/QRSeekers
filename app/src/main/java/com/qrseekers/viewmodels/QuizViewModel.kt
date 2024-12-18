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

    // Using mutableStateOf to hold the list of questions
    private val _questions = mutableStateOf<List<Question>>(emptyList())
    val questions: State<List<Question>> get() = _questions

    // State to hold the user's answers
    private val _answers = mutableStateOf<Map<String, String>>(emptyMap())
    val answers: State<Map<String, String>> get() = _answers

    private val logTag = "QuizViewModel"


    // Function to load questions based on zone
    fun loadQuestions(zoneId: String) {
        viewModelScope.launch {
            try {
                // Fetch the Zone document from Firestore by its ID
                val zoneDoc = firestore.collection("Zones").document(zoneId).get().await()
                // Map the document data into a Zone object
                val zone = zoneDoc.toObject(Zone::class.java)

                // Get the list of question IDs from the Zone object
                val questionIds = zone?.questions ?: emptyList()

                // Prepare a list to store the fetched questions
                val questionsList = mutableListOf<Question>()

                // Loop through each question ID and fetch the corresponding question
                for (questionId in questionIds) {
                    val questionDoc =
                        firestore.collection("Questions").document(questionId).get().await()
                    val question = questionDoc.toObject(Question::class.java)


                    question?.let {
                        it.id = questionId // Assign the question ID from the loop to the Question object
                        questionsList.add(it)
                        Log.d(logTag, "loaded question: ${question.toString()}")
                    }
                    if (question == null || question.text.isNullOrEmpty()) {
                        val errorMessage = "Lost question with ID: $questionId (Question is empty or null)"
                        Log.d(logTag, errorMessage)
                        throw IllegalStateException(errorMessage)

                    }


                }

                // Update the state with the list of questions
                _questions.value = questionsList

            } catch (e: Exception) {
                // Handle error (for example, logging or showing an error message)
                Log.e(logTag, "Error loading questions: ${e.localizedMessage}")
            }
        }
    }

    // Update the answer for a specific question
    fun updateAnswer(questionId: String, answer: String) {
        Log.d(logTag, "Updating answer for question $questionId to $answer")

        _answers.value = _answers.value.toMutableMap().apply {
            this[questionId] = answer
        }
        Log.d(logTag, "Updated answers: ${_answers.value}")

    }
}
