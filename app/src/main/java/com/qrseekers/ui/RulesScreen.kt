package com.qrseekers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.AppRoute

@Composable
fun RulesScreen(navController: NavController, gameName: String) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Game Rules for ${gameName.ifBlank { "Unknown Game" }}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = """
                        1. Head to the designated location.
                        2. Look for the QR code and scan it.
                        3. Answer the questions.
                        4. Answer all questions correctly to win!
                    """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            }
            Button(
                onClick = { navController.navigate(AppRoute.QUIZ.route) }
            ) {
                Text("Continue")
            }
        }
    }
}
