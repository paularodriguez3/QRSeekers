package com.qrseekers.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.qrseekers.data.Question
import com.qrseekers.viewmodels.QuizViewModel
import coil.compose.AsyncImage
import com.qrseekers.AppRoute
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.ZoneViewModel
import okhttp3.Route


@Composable
fun QuizPage(
    authViewModel: AuthViewModel,
    quizViewModel: QuizViewModel,
    zoneViewModel: ZoneViewModel,
    navController: NavController,
    ) {
    val zoneId = zoneViewModel.currentZone.value?.id
    val zoneName = zoneViewModel.currentZone.value?.name

    val questions by remember { quizViewModel.questions }
    val answers by remember { quizViewModel.answers }
    var totalPoints by remember { mutableStateOf(0) }
    var correctness by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    LaunchedEffect(zoneId) {
        if (zoneId != null) {
            quizViewModel.loadQuestions(zoneId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = zoneName.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                QuestionItem(
                    question = question,
                    answer = answers[question.id],
                    onAnswerChange = { newAnswer -> quizViewModel.updateAnswer(question.id, newAnswer) },
                    index = index + 1,
                    isSubmitted = isSubmitted,
                    isCorrect = correctness[question.id]
                )
            }
        }

        // Total points displayed after submission
        if (isSubmitted) {
            Text(
                text = "Total Points: $totalPoints",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Submit/Continue button
        Button(
            onClick = {
                if (!isSubmitted) {
                    val unansweredQuestions = questions.any { question -> answers[question.id].isNullOrEmpty() }
                    if (unansweredQuestions) {
                        showPopup = true
                    } else {
                        quizViewModel.checkAnswers { _, points, correctnessMap ->
                            totalPoints = points
                            correctness = correctnessMap
                            isSubmitted = true
                        }
                    }
                } else {
                    // Handle continue action
                    Log.d("QuizPage", "Continue to next step")
                    authViewModel.addPoints(totalPoints)

                    // invoked passed function that goes back to navigation
                    navController.navigate(AppRoute.RESULTS.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSubmitted) "Continue" else "Submit")
        }
    }

    // Popup for unanswered questions
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text("Incomplete Answers") },
            text = { Text("Please answer all questions before submitting.") },
            confirmButton = {
                TextButton(onClick = { showPopup = false }) {
                    Text("OK")
                }
            }
        )
    }
}


@Composable
fun QuestionItem(
    question: Question,
    answer: String?,
    onAnswerChange: (String) -> Unit,
    index: Int,
    isSubmitted: Boolean,
    isCorrect: Boolean?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question text
        Text(
            text = "$index. (${question.points} pts) ${question.text}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Optional image
        question.imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Question Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        // Options or input field
        question.options?.let { options ->
            MultipleChoiceOptions(
                options = options,
                selectedOption = answer,
                onOptionSelected = onAnswerChange,
                isSubmitted = isSubmitted,
                correctAnswer = (if (isSubmitted) question.correctAnswer else null).toString()
            )
        } ?: OpenEndedQuestion(
            answer = answer,
            onAnswerChange = onAnswerChange,
            isSubmitted = isSubmitted,
            correctAnswer = (if (isSubmitted) question.correctAnswer else null).toString()
        )

        // Display correctness feedback
        if (isSubmitted && isCorrect != null) {
            Text(
                text = if (isCorrect) "Correct!" else "Incorrect",
                color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun MultipleChoiceOptions(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    isSubmitted: Boolean,
    correctAnswer: String?
) {
    Column {
        options.forEach { option ->
            val isCorrect = isSubmitted && option == correctAnswer
            val isSelected = option == selectedOption
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isSubmitted) { onOptionSelected(option) }
                    .padding(4.dp)
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { if (!isSubmitted) onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option,
                    color = if (isSubmitted) {
                        when {
                            isCorrect -> MaterialTheme.colorScheme.primary
                            isSelected -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

@Composable
fun OpenEndedQuestion(
    answer: String?,
    onAnswerChange: (String) -> Unit,
    isSubmitted: Boolean,
    correctAnswer: String?
) {
    Column {
        OutlinedTextField(
            value = answer.orEmpty(),
            onValueChange = onAnswerChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Your answer") },
            enabled = !isSubmitted
        )
        if (isSubmitted && correctAnswer != null) {
            Text(
                text = "Correct Answer: $correctAnswer",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QuizPagePreview() {
    /*zoneId = "6lkp5c174aJFdccLItuA",
    zoneName = "Las aaa"*/
    /*QuizPage(
        quizViewModel = viewModel(),
        onSubmit = { answers ->
            println("Answers submitted: $answers")
        }
    )*/
}
