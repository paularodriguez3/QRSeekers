package com.qrseekers.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.R
import com.qrseekers.ui.components.FillInTextField

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Estado para almacenar el valor del email

    // Obtener el contexto local antes del onClick
    val context = LocalContext.current

    // State to focus on next field
    val focusRequesterEmail = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current // Get keyboard controller

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen superior
        Image(
            painter = painterResource(id = R.drawable.forgot_password_image), // Reemplaza con tu recurso de imagen
            contentDescription = "Forgot Password Illustration",
            modifier = Modifier
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Título
        Text(
            text = "Forgotten Password",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF1E88E5),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = "Enter your email to reset your password",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada de texto para el email

        FillInTextField(
            value = email,
            onValueChange = { email = it },
            label = "Enter your email",
            modifier = Modifier.fillMaxWidth(0.9f).focusRequester(focusRequesterEmail),
            imeAction = ImeAction.Done,
            onImeAction = {
                keyboardController?.hide()            }
        )

        // Botón para restablecer contraseña
        Button(
            onClick = {
                // Lógica para manejar el restablecimiento de contraseña con el email
                Toast.makeText(
                    context, // Usa el contexto aquí
                    "Password reset link sent to $email",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text(
                text = "Reset Password",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
