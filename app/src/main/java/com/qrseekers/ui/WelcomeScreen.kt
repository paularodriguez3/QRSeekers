package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.AppRoute
import com.qrseekers.ui.components.ReusableSimpleButton
import com.qrseekers.R
import com.qrseekers.viewmodels.AuthState

@Composable
fun WelcomeScreen(navController: NavController, authState: AuthState) {
    LaunchedEffect(authState) {
        kotlinx.coroutines.delay(1000) // Esperar 2 segundos
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(AppRoute.JOINGAME.route) {
                    popUpTo(AppRoute.WELCOME.route) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated, is AuthState.Error -> {
                navController.navigate(AppRoute.LOGIN.route) {
                    popUpTo(AppRoute.WELCOME.route) { inclusive = true }
                }
            }
            else -> Unit // No hacer nada si el estado es Loading
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE3F2FD)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QRseekers",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E88E5)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore the city with friends",
                    fontSize = 16.sp,
                    color = Color(0xFF1E88E5)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_square),
                    contentDescription = "QRSeekers Logo",
                    modifier = Modifier.size(250.dp)
                )
            }
        }
    }
}


