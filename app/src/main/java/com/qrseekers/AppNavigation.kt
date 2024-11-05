package com.qrseekers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qrseekers.ui.HomePage
import com.qrseekers.ui.LoginPage
import com.qrseekers.ui.SignUpPage

@Composable
fun AppNavigation (modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination="login", builder = {
        composable("login") {
            LoginPage (modifier, navController, authViewModel)
        }
        composable("signup") {
            SignUpPage (modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage (modifier, navController, authViewModel)
        }
    }

    )

}

