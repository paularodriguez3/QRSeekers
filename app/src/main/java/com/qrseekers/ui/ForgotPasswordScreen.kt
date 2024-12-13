package com.qrseekers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Estado para almacenar el valor del email

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgotten Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E88E5)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Enter your email to reset your password",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de texto para el email
        TextField(
            value = email,
            onValueChange = { email = it }, // Actualiza el estado con el texto ingresado
            placeholder = { Text("Add your email here") }, // Placeholder
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Lógica para manejar el restablecimiento de contraseña con el email
                // Por ejemplo, puedes enviar el email a tu backend
            }
        ) {
            Text("Reset Password")
        }
    }
}
