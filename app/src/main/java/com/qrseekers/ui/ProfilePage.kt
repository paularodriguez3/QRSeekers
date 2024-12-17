package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    // Estados para almacenar los datos del usuario
    var nickname by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }

    // Obtener datos del usuario desde Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nickname = document.getString("name") ?: "No Name"
                        email = document.getString("email") ?: "No Email"
                    }
                }
                .addOnFailureListener {
                    nickname = "Error loading data"
                    email = "Error loading data"
                }
        }
    }

    // UI con datos dinámicos
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            Text("QRseekers", style = MaterialTheme.typography.titleMedium)
        }

        // Imagen de perfil
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .padding(top = 8.dp, bottom = 16.dp)
        )

        // Nickname
        Text(
            text = nickname,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Correo electrónico
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Email, contentDescription = "Email")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
        }
    }
}
