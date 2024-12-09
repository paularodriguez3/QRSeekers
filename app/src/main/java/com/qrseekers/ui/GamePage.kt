package com.qrseekers.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.qrseekers.viewmodels.AuthViewModel

@Composable
fun GamePage(modifier: Modifier, navController: NavHostController, authViewModel: AuthViewModel) {
    Text(text = "Team page")

}