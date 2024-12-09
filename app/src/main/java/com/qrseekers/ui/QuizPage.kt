package com.qrseekers.ui

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
import coil.compose.AsyncImage
import com.qrseekers.data.Question


@Composable
fun QuizPage(
    zoneName: String,
    onSubmit: (Map<String, String>) -> Unit
) {
    // Mock questions
    val questions = listOf(
        Question("1", "How many statues are there on Charles Bridge?", "multi",3, listOf("96", "45", "46", "78"), "96", null),
        Question("2", "What year was Charles Bridge completed?", "multi",2, listOf("1342", "1402", "1357", "1410"), "1357", null),
        Question("3", "Enter the total length of Charles Bridge (in meters):", "text",4, null, "516", "https://en.wikipedia.org/wiki/Charles_Bridge#/media/File:Charles_Bridge_-_Prague,_Czech_Republic_-_panoramio.jpg")
    )

    var answers by remember { mutableStateOf(mutableMapOf<String, String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Zone title
        Text(
            text = "ZONE: $zoneName",
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
                    answer = answers[question.id],
                    onAnswerChange = { newAnswer ->
                        answers = answers.toMutableMap().apply { this[question.id] = newAnswer }
                    },
                    index = index + 1
                )
            }
        }

        // Submit button
        Button(
            onClick = { onSubmit(answers) },
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


@Composable
@Preview(showBackground = true)
fun QuizPagePreview() {
    QuizPage(
        zoneName = "Charles Bridge",
        onSubmit = { answers ->
            println(answers)
        }
    )
}
