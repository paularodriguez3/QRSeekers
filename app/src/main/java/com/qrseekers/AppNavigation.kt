package com.qrseekers

import BottomNavigationBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qrseekers.ui.ForgotPasswordScreen
import com.qrseekers.ui.Game
import com.qrseekers.ui.GamePage
import com.qrseekers.ui.HomePage
import com.qrseekers.ui.JoinGameScreen
import com.qrseekers.ui.LoginPage
import com.qrseekers.ui.ProfilePage
import com.qrseekers.ui.QuizPage
import com.qrseekers.ui.ScanPage
import com.qrseekers.ui.SignUpPage
import com.qrseekers.ui.TeamPage
import com.qrseekers.ui.WelcomeScreen
import com.qrseekers.viewmodels.AuthViewModel

@Composable
fun AppNavigation (
    modifier: Modifier,
    authViewModel: AuthViewModel){
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntry?.destination?.route
    println("Current route: $currentRoute") // Debugging

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // State to track if bottom bar should be shown
    val showBottomBar = remember { mutableStateOf(true) }

    LaunchedEffect(navBackStackEntry) {
        showBottomBar.value = ShowBottomBarCheck(navController)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar.value) {
                BottomNavigationBar(navController) // Show the bottom bar if true
            }
        }
    ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRoute.WELCOME.route, // Start at WelcomeScreen
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(AppRoute.WELCOME.route) {
                            WelcomeScreen(navController)
                        }
                        composable(AppRoute.LOGIN.route) {
                            LoginPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.SIGNUP.route) {
                            SignUpPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.FORGOT_PASSWORD.route) {
                            ForgotPasswordScreen(navController)
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
                        composable(AppRoute.TEAM.route) {
                            TeamPage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.GAME.route) {
                            GamePage(modifier, navController, authViewModel)
                        }
                        composable(AppRoute.JOINGAME.route) {
                            JoinGameScreen(
                                games = listOf(
                                    Game(name = "Prague Discovery game", description = "Discover the heart of the city", imageRes = android.R.drawable.ic_dialog_map),
                                    Game(name = "Las Palmas Game", description = "Explore historic landmarks", imageRes = android.R.drawable.ic_dialog_map),
                                    Game(name = "ESN Indoor activity", description = "Solve the murder of our mascot", imageRes = android.R.drawable.ic_dialog_map)
                                ),
                                onGameSelected = { game -> /* Handle game selection */ },
                                onImportGame = { /* Handle importing game */ }
                            )
                        }
                        composable(AppRoute.QUIZ.route) {
                            QuizPage(
                                "Charles bridge",
                                onSubmit = { submitted -> /* Handle submission */ }
                            )
                        }
                    }

    }


}


private fun ShowBottomBarCheck(
    navController: NavHostController
): Boolean {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    return currentRoute?.let { route ->
        AppRoute.bottomNavRoutes.contains(route)
    } ?: false
}

// Enum for routes
enum class AppRoute(val route: String) {
    WELCOME("welcome"), // Add this route
    LOGIN("login"),
    SIGNUP("signup"),
    FORGOT_PASSWORD("forgot_password"),
    HOME("home"),
    SCAN("scan"),
    PROFILE("profile"),
    TEAM("team"),
    QUIZ("quiz"),
    JOINGAME("joingame"),
    GAME("game");

    companion object {
        // Include routes with bottom navigation (modify as needed)
        val bottomNavRoutes = values()
            .filterNot { it == LOGIN || it == SIGNUP || it == WELCOME }
            .map { it.route }
            .toSet()
    }
}


