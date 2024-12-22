package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.AppRoute
import com.qrseekers.ui.components.*
import com.qrseekers.R
import com.qrseekers.viewmodels.AuthViewModel

@Composable
fun ResultsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    allCorrect: Boolean // Nuevo parámetro para determinar si todas las respuestas son correctas
) {
    val points = authViewModel.user.value.points

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
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "GAME OVER", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Mostrar "Congratulations!" solo si todas las respuestas son correctas
                    if (allCorrect) {
                        Text(text = "Congratulations!", fontSize = 20.sp, color = Color(0xFF1E88E5))
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Image(
                        painter = painterResource(id = R.drawable.logo_square),
                        contentDescription = "QRSeekers Logo",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "You scored $points points", fontSize = 18.sp)
                }
                ReusableSimpleButton(navController, AppRoute.JOINGAME.route, "Back to games")
            }
        }
    }
}

