package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun WelcomeScreen(navController: NavController) {
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
            // Centrar logo y títulos
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
            // Botones y enlace de forgotten password
            Column(
                modifier = Modifier
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReusableSimpleButton(navController, AppRoute.LOGIN.route, "Login")
                Spacer(modifier = Modifier.height(32.dp))
                ReusableSimpleButton(navController, AppRoute.SIGNUP.route, "Sign Up")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Forgotten Password?",
                    fontSize = 14.sp,
                    color = Color(0xFF1E88E5),
                    modifier = Modifier
                        .clickable { navController.navigate(AppRoute.FORGOT_PASSWORD.route) }
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
