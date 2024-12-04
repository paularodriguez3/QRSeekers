package com.qrseekers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qrseekers.ui.Game
import com.qrseekers.ui.HomePage
import com.qrseekers.ui.JoinGameScreen
import com.qrseekers.ui.LoginPage
import com.qrseekers.ui.ProfilePage
import com.qrseekers.ui.QuizPage
import com.qrseekers.ui.ScanPage
import com.qrseekers.ui.SignUpPage
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.QuizViewModel

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
        composable("scan"){
            ScanPage(modifier, navController, authViewModel)
        }
        composable("profile") {
            ProfilePage(modifier, navController)
        }
        composable("quiz"){
            QuizPage("Prague castle", QuizViewModel(), onSubmit = {/* */})
        }

        composable("joingame") {
            val games = listOf(
                Game(name = "Game 1", description = "Discover the heart of the city", imageRes = android.R.drawable.ic_dialog_map),
                Game(name = "Game 2", description = "Explore historic landmarks", imageRes = android.R.drawable.ic_dialog_map),
                Game(name = "Game 3", description = "Solve the city mysteries", imageRes = android.R.drawable.ic_dialog_map)
            )

            JoinGameScreen(
                games = games,
                onGameSelected = { game -> /* Handle game selection */ },
                onImportGame = { /* Handle importing game */ }
            )
        }
    }

    )

}

