package com.qrseekers.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.AppRoute
import com.qrseekers.R
import com.qrseekers.ui.components.FillInPasswordField
import com.qrseekers.ui.components.FillInTextField
import com.qrseekers.viewmodels.AuthState
import com.qrseekers.viewmodels.AuthViewModel

@Composable
fun SignUpPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) } // State to toggle password visibility

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val focusRequesterNickname = FocusRequester()
    val focusRequesterEmail = FocusRequester()
    val focusRequesterPassword = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current // Get keyboard controller


    // Monitor authentication state
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate(AppRoute.JOINGAME.route) {
                // Clear backstack to prevent going back to signup
                //popUpTo(AppRoute.JOINGAME.route) { inclusive = true }
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section with an image and title
        Image(
            painter = painterResource(id = R.drawable.signup_image), // Replace with your drawable
            contentDescription = "Sign Up Illustration",
            modifier = Modifier
                .height(200.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nickname input field
        FillInTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = "Enter your nickname",
            modifier = Modifier.fillMaxWidth(0.9f).focusRequester(focusRequesterNickname),
            imeAction = ImeAction.Next,
            onImeAction = { focusRequesterEmail.requestFocus() }
        )

        // Email input field
        FillInTextField(
            value = email,
            onValueChange = { email = it },
            label = "Enter your email",
            modifier = Modifier.fillMaxWidth(0.9f).focusRequester(focusRequesterEmail),
            imeAction = ImeAction.Next,
            onImeAction = {focusRequesterPassword.requestFocus()}
        )

        // Password input field
        FillInPasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Enter your password",
            modifier = Modifier.fillMaxWidth(0.9f).focusRequester(focusRequesterPassword),
            imeAction = ImeAction.Done,
            onImeAction = {
                keyboardController?.hide()
            }
        )

        // Register button
        Button(
            onClick = { authViewModel.signup(email, password, nickname) }, // Ahora incluye nickname
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text(
                text = "Register",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Text(
            text = "Back to login page",
            color = Color.Blue.copy(alpha = 0.7f), // Blue color to indicate link
            fontSize = 14.sp, // Slightly smaller font size
            modifier = Modifier
                .padding(top = 8.dp) // Add space above
                .clickable {
                    // Handle the click action for forgotten password
                    navController.navigate(AppRoute.LOGIN.route) // Navigate to forgotten password screen
                }

        )

    }
}
