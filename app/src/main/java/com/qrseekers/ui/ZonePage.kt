package com.qrseekers.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import com.qrseekers.viewmodels.AuthViewModel


@Composable
fun ZonePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    /* show a list of already visited zone names and point obtained there */
    Text(text = "Zone page")

}