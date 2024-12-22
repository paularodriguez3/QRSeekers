package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    allCorrect: Boolean // Determinar si todas las respuestas son correctas
) {
    val points = authViewModel.user.value.points

    Scaffold(topBar = { ReusableTitle() }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Text(
                        text = "GAME OVER",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E88E5),
                        textAlign = TextAlign.Center
                    )

                    // Congratulations message if all correct
                    if (allCorrect) {
                        Text(
                            text = "Congratulations!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50), // Verde para éxito
                            textAlign = TextAlign.Center
                        )
                    }

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo_square),
                        contentDescription = "QRSeekers Logo",
                        modifier = Modifier.size(150.dp)
                    )

                    // Points scored
                    Text(
                        text = "You scored $points points",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E88E5),
                        textAlign = TextAlign.Center
                    )
                }

                // Back to games button
                Button(
                    onClick = { navController.navigate(AppRoute.JOINGAME.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E88E5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Back to games",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


