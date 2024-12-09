/*package com.qrseekers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qrseekers.data.Question
import com.qrseekers.viewmodels.QuizViewModel

@Composable
fun QuizPage(
    zoneName: String,
    quizViewModel: QuizViewModel,  // Accept the QuizViewModel as a parameter
    onSubmit: (List<Pair<String, String>>) -> Unit
) {
    println("Quiz page ANNA") // Debugging

    // Observe the questions and team score from the ViewModel
    val questions = quizViewModel.questions
    val teamScore = quizViewModel.teamScore.value
    var answers by remember { mutableStateOf(mutableMapOf<String, String>()) }

    // Load questions when the QuizPage is displayed (gameId could come from the previous screen)
    LaunchedEffect(Unit) {
        quizViewModel.loadQuestions(gameId = "PDG_LS_2024")  // Replace with actual game ID
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            //.background(Color.White)
    ) {
        // Zone Title
        Text(
            text = "ZONE: $zoneName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Questions list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            questions.forEach { question ->
                item {
                    QuestionItem(
                        question = question,
                        answer = answers[question.id],
                        onAnswerChange = { newAnswer ->
                            answers[question.id] = newAnswer
                        }
                    )
                }
            }
        }

        // Submit Button
        Button(
            onClick = { onSubmit(answers.toList()) },
            enabled = true,  // Always enabled now
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
    onAnswerChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Question text
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Question options (if multiple choice)
        question.options?.let { options ->
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAnswerChange(option) }
                        .padding(4.dp)
                ) {
                    RadioButton(
                        selected = option == answer,
                        onClick = { onAnswerChange(option) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option)
                }
            }
        } ?: run {
            // Text input for open-ended questions
            OutlinedTextField(
                value = answer ?: "",
                onValueChange = { onAnswerChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Your answer") }
            )
        }
    }
}

/*
// Mock data for preview
class MockQuizViewModel : QuizViewModel() {
    private val mockQuestions = mutableStateListOf(
        Question(
            id = "1",
            text = "How many statues are there on Charles Bridge?",
            type = "multiple_choice", // Example of type
            options = listOf("96", "45", "46", "78", "95"),
            points = 5,
            correctAnswer = "96" // Correct answer
        ),
        Question(
            id = "2",
            text = "What year was Charles Bridge completed?",
            type = "multiple_choice", // Example of type
            options = listOf("1342", "1402", "1357", "1410"),
            points = 3,
            correctAnswer = "1357" // Correct answer
        ),
        Question(
            id = "3",
            text = "Enter the total length of Charles Bridge (in meters):",
            type = "open_ended", // Example of type
            points = 4,
            correctAnswer = 50, // Correct answer
            imageUrl = "https://example.com/charles-bridge.jpg" // Optional image URL
        )
    )


    // Use a getter to return the mock questions
    override val questions: SnapshotStateList<Question>
        get() = mockQuestions

}

@Composable
@Preview(showBackground = true)
fun QuizPagePreview() {
    // Preview the QuizPage with a MockQuizViewModel
    QuizPage(
        zoneName = "Charles Bridge",
        quizViewModel = MockQuizViewModel(),  // Use the mock view model
        onSubmit = { answers ->
            // Print answers to the log (simulate saving answers)
            answers.forEach { (questionId, answer) ->
                println("Question ID: $questionId, Answer: $answer")
            }
        }
    )
}
*/
 */
