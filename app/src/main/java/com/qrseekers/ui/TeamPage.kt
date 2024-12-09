package com.qrseekers.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import com.qrseekers.viewmodels.AuthViewModel


@Composable
fun TeamPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    Text(text = "Team page")

}