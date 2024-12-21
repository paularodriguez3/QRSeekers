package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.AppRoute
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.R
import com.qrseekers.data.User

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    // Estados para almacenar los datos del usuario
    var nickname by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var participates by remember { mutableStateOf("Loading...") }
    var points by remember { mutableStateOf("Loading...") }



    // Obtener datos del usuario desde Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nickname = document.getString("nickname") ?: "No game"
                        email = document.getString("email") ?: "No Email"
                        participates = document.getString("gameName") ?: "None" //todo: set game name into dbs in join game/rules
                        //points =  document.getString("points") ?: "0"
                    }
                }
                .addOnFailureListener {
                    nickname = "Error loading data"
                    email = "Error loading data"
                    participates = "Error loading data"
                    //points = "Error loading data"



                }
        }
    }

    Button(
        onClick = {
            authViewModel.signout() // Cerrar sesión
            navController.navigate(AppRoute.WELCOME.route) {
                popUpTo(AppRoute.WELCOME.route) { inclusive = true }
            }
        }
    ) {
        Text("Logout")
    }


    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra Superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF1E88E5)
                    )
                }
                Text(
                    text = "QRseekers",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen y nombre del usuario
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_square),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = nickname,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp,
                    color = Color(0xFF1E88E5)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Color(0xFF1E88E5)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "Participates",
                            tint = Color(0xFF1E88E5)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = participates,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    /*Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Money, // todo: better icon for points
                            contentDescription = "Points",
                            tint = Color(0xFF1E88E5)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = points,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }*/

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de Logout
            Button(
                onClick = {
                    authViewModel.signout() // Llama a signout en AuthViewModel
                    navController.navigate(AppRoute.WELCOME.route) {
                        popUpTo(AppRoute.WELCOME.route) { inclusive = true } // Limpia el backstack
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Log Out",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {

    val context = LocalContext.current
    val mockNavController = object : NavController(context) {} // Mock NavController
    val mockAuthViewModel = AuthViewModel() // Replace with a mock or real instance if available

    ProfilePage(
        navController = mockNavController,
        authViewModel = mockAuthViewModel
    )
}