/*
not functional yet, only works with additional permissions that I guess we dont want in the app
so far questions added manualy for testing
might be a good idea to do this in separate project
 */
package com.qrseekers.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.Question


fun uploadQuestionsToFirebase(questions: List<Question>) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("Questions") // Centralized collection for all questions

    questions.forEach { question ->
        collectionRef.add(question)
            .addOnSuccessListener { documentReference ->
                println("Question added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding question: $e")
            }
    }
}
fun main() {

    val questions = listOf(
            // Las Palmas questions
            Question(
                id = "q1",
                text = "What is the name of the historic neighborhood in Las Palmas that is known as the birthplace of the city?",
                type = "multi",
                points = 10,
                options = listOf("Vegueta", "Triana", "La Isleta"),
                correctAnswer = "Vegueta"
            ),
    Question(
        id = "q2",
        text = "Which famous explorer made a stop in Las Palmas during his first voyage to the Americas?",
        type = "multi",
        points = 10,
        options = listOf("Vasco da Gama", "Christopher Columbus", "Ferdinand Magellan"),
        correctAnswer = "Christopher Columbus"
    ),
    Question(
        id = "q3",
        text = "What natural feature divides Las Palmas into two distinct areas?",
        type = "multi",
        points = 10,
        options = listOf("The Dunes of Maspalomas", "La Isleta", "The Teide Volcano"),
        correctAnswer = "La Isleta"
    ),
    // Prague questions
    Question(
        id = "q4",
        text = "What is the name of the famous bridge in Prague that connects the Old Town to Prague Castle?",
        type = "multi",
        points = 10,
        options = listOf("Charles Bridge", "Golden Gate Bridge", "Wenceslas Bridge"),
        correctAnswer = "Charles Bridge"
    ),
    Question(
        id = "q5",
        text = "Which famous astronomical artifact can you find on the Old Town Hall in Prague?",
        type = "multi",
        points = 10,
        options = listOf("Prague Astronomical Clock", "Sundial of Time", "Clock of Ages"),
        correctAnswer = "Prague Astronomical Clock"
    ),
    Question(
        id = "q6",
        text = "What is the name of the hill where you can find the Petřín Lookout Tower, resembling a mini Eiffel Tower?",
        type = "multi",
        points = 10,
        options = listOf("Castle Hill", "Petřín Hill", "Strahov Hill"),
        correctAnswer = "Petřín Hill"
    ),
    // Indoor questions
    Question(
        id = "q7",
        text = "What color is most commonly associated with QRseekers?",
        type = "multi",
        points = 10,
        options = listOf("Blue", "Green", "Red"),
        correctAnswer = "Blue"
    ),
    Question(
        id = "q8",
        text = "In what type of location is the Student Union Hall typically found?",
        type = "multi",
        points = 10,
        options = listOf("University Campus", "Shopping Mall", "Hotel Lobby"),
        correctAnswer = "University Campus"
    ),
    Question(
        id = "q9",
        text = "Which of the following is often used to create a QR code?",
        type = "multi",
        points = 10,
        options = listOf("Barcodes", "Black and White Squares", "Color Gradients"),
        correctAnswer = "Black and White Squares"
    )
    )



    // Use a unique ID for the game
    uploadQuestionsToFirebase(questions)
}
