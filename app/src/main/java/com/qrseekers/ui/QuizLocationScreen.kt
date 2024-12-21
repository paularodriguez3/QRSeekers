package com.qrseekers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.ui.components.ReusableSimpleButton
import com.qrseekers.ui.components.ReusableTitle

data class Location(
    val id: String = "",
    val locationName: String = "",
    val locationSubtitle: String = "",
    val locationColor: String = "#FFFFFF"
)

@Composable
fun QuizLocationScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()

    // State to hold locations from Firebase
    var locationsList by remember { mutableStateOf(listOf<Location>()) }
    var selectedLocationId by remember { mutableStateOf<String?>(null) }

    // Fetch locations from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("locations")
            .get()
            .addOnSuccessListener { snapshot ->
                val locations = snapshot.documents.mapNotNull { document ->
                    document.toObject(Location::class.java)?.copy(id = document.id)
                }
                locationsList = locations
            }
            .addOnFailureListener { e ->
                println("Error fetching locations: $e")
            }
    }

    Scaffold(topBar = { ReusableTitle() }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFF7FF)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF34eb95))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Join a game",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Submit your game selection when ready",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display list of locations
                locationsList.forEach { location ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(android.graphics.Color.parseColor(location.locationColor)))
                            .padding(16.dp)
                            .clickable { selectedLocationId = location.id }
                    ) {
                        Column {
                            Text(
                                text = location.locationName,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            Text(
                                text = location.locationSubtitle,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ReusableSimpleButton(
                    navController,
                    route = "question_screen/${selectedLocationId ?: ""}",
                    text = "Submit your selection"
                )
            }
        }
    }
}
