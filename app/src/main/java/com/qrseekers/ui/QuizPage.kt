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
import com.qrseekers.data.Question
import com.qrseekers.viewmodels.QuizViewModel
import coil.compose.AsyncImage
import com.qrseekers.viewmodels.ZoneViewModel


@Composable
fun QuizPage(
    quizViewModel: QuizViewModel,
    zoneViewModel: ZoneViewModel,
    onSubmit: (Map<String, String>) -> Unit
) {
    var zoneId = zoneViewModel.currentZone.value?.id
    var zoneName = zoneViewModel.currentZone.value?.name

    // remember the chosen answers
    var answers by remember { mutableStateOf(mutableMapOf<String, String>()) }
    // todo: check answers in quizview model route to screen with results after submit button (not if an answer for question is missing)


    // Get the QuizViewModel instance
    //val quizViewModel: QuizViewModel = viewModel()

    // Get the questions from the ViewModel
    val questions by remember { quizViewModel.questions }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch questions for the zone when the composable is first launched
    LaunchedEffect(zoneId) {
        if (zoneId != null) {
            try {
                // Attempt to load the questions
                quizViewModel.loadQuestions(zoneId)
            } catch (e: Exception) {
                // Handle errors (e.g., network issues, Firebase fetch issues)
                errorMessage = "Error loading questions: ${e.message}"
                Log.e("QuizScreen", "Error loading questions for zone $zoneId: ${e.message}")
            }
        } else {
            // Handle case where zoneId is null
            errorMessage = "Invalid zone ID"
            Log.e("QuizScreen", "Invalid zone ID: $zoneId")
        }
    }

    // Display questions or an error message
    errorMessage?.let { Text(text = it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Zone title
        Text(
            text = "$zoneName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // List of questions
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                QuestionItem(
                    question = question,
                    answer = quizViewModel.answers.value[question.id],
                    onAnswerChange = { newAnswer ->
                        quizViewModel.updateAnswer(question.id, newAnswer)
                    },
                    index = index + 1
                )
            }
        }

        // Submit button
        Button(
            onClick = { onSubmit(quizViewModel.answers.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    answer: String?,
    onAnswerChange: (String) -> Unit,
    index: Int
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
                onOptionSelected = onAnswerChange
            )
        } ?: OpenEndedQuestion(
            answer = answer,
            onAnswerChange = onAnswerChange
        )
    }
}

@Composable
fun MultipleChoiceOptions(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .padding(4.dp)
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun OpenEndedQuestion(
    answer: String?,
    onAnswerChange: (String) -> Unit
) {
    OutlinedTextField(
        value = answer.orEmpty(),
        onValueChange = onAnswerChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Your answer") }
    )
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
