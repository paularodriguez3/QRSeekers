package com.qrseekers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.MainActivity.Companion.ReusableTitle

data class Question(
    val id: String = "",
    val questionText: String = "",
    val answers: List<String> = emptyList(),
    val correctAnswerIndex: Int = -1
)

@Composable
fun QuestionScreen(navController: NavController, idLocation: String?) {
    val firestore = FirebaseFirestore.getInstance()

    var questionsList by remember { mutableStateOf(listOf<Question>()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }

    // Fetch questions from Firestore
    LaunchedEffect(idLocation) {
        if (!idLocation.isNullOrEmpty()) {
            firestore.collection("locations")
                .document(idLocation)
                .collection("questions")
                .get()
                .addOnSuccessListener { snapshot ->
                    val questions = snapshot.documents.mapNotNull { document ->
                        document.toObject(Question::class.java)?.copy(id = document.id)
                    }
                    questionsList = questions
                }
                .addOnFailureListener { e ->
                    println("Error fetching questions: $e")
                }
        }
    }

    val currentQuestion = questionsList.getOrNull(currentQuestionIndex)

    Scaffold(topBar = { ReusableTitle() }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.White
        ) {
            if (currentQuestion != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.questionText,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Column {
                        currentQuestion.answers.forEachIndexed { index, answer ->
                            Button(
                                onClick = {
                                    if (index == currentQuestion.correctAnswerIndex) {
                                        score++
                                    }
                                    if (currentQuestionIndex < questionsList.size - 1) {
                                        currentQuestionIndex++
                                    } else {
                                        navController.navigate("game_over_screen/$score")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(text = answer)
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Loading questions...")
                }
            }
        }
    }
}
