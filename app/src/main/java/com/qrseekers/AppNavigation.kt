package com.qrseekers

import BottomNavigationBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
fun AppNavigation (
    modifier: Modifier,
    authViewModel: AuthViewModel){
    val navController = rememberNavController()

    val bottomNavRoutes = listOf("home", "scan", "profile", "joingame")

    val currentRoute = navController.currentBackStackEntry?.destination?.route
    println("Current route: $currentRoute") // Debugging



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            val showBottomBar = currentRoute == null || currentRoute in bottomNavRoutes
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRoute.LOGIN.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(AppRoute.LOGIN.route) {
                            LoginPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.SIGNUP.route) {
                            SignUpPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.HOME.route) {
                            HomePage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.SCAN.route) {
                            ScanPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.PROFILE.route) {
                            ProfilePage(modifier, navController)
                        }
                        composable(AppRoute.JOINGAME.route) {
                            JoinGameScreen(
                                games = listOf(
                                    Game(name = "Game 1", description = "Discover the heart of the city", imageRes = android.R.drawable.ic_dialog_map),
                                    Game(name = "Game 2", description = "Explore historic landmarks", imageRes = android.R.drawable.ic_dialog_map),
                                    Game(name = "Game 3", description = "Solve the city mysteries", imageRes = android.R.drawable.ic_dialog_map)
                                ),
                                onGameSelected = { game -> /* Handle game selection */ },
                                onImportGame = { /* Handle importing game */ }
                            )
                        }
                        composable(AppRoute.QUIZ.route) {
                            QuizPage("Prague Castle", QuizViewModel(), onSubmit = { /* Handle quiz submission */ })
                        }
        }
    }


}

// Enum for routes
enum class AppRoute(val route: String, val showBottomNav: Boolean = false) {
    LOGIN("login"),
    SIGNUP("signup"),
    HOME("home", true),
    SCAN("scan", true),
    PROFILE("profile", true),
    JOINGAME("joingame", true),
    QUIZ("quiz", true)
}

