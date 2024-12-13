package com.qrseekers.ui

import androidx.compose.foundation.Image
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
import com.qrseekers.MainActivity.Companion.ReusableSimpleButton
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ReusableSimpleButton(navController, AppRoute.LOGIN.route, "Login")
                Spacer(modifier = Modifier.height(16.dp))
                ReusableSimpleButton(navController, AppRoute.SIGNUP.route, "Sign Up")
            }
        }
    }
}
