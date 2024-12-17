package com.qrseekers

import BottomNavigationBar
import android.net.Uri
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.qrseekers.ui.ForgotPasswordScreen
import com.qrseekers.ui.GamePage
import com.qrseekers.ui.HomePage
import com.qrseekers.ui.JoinGameScreen
import com.qrseekers.ui.LocationScreen
import com.qrseekers.ui.LoginPage
import com.qrseekers.ui.ProfilePage
import com.qrseekers.ui.QuizPage
import com.qrseekers.ui.RulesScreen
import com.qrseekers.ui.ScanPage
import com.qrseekers.ui.SignUpPage
import com.qrseekers.ui.TeamPage
import com.qrseekers.ui.WelcomeScreen
import com.qrseekers.viewmodels.AuthViewModel

@Composable
fun AppNavigation(
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
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
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.WELCOME.route,
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
                    onGameSelected = { game ->
                        navController.navigate("${AppRoute.RULES.route}/${game.name}")
                    },
                    navController = navController
                )
            }
            composable(
                route = "${AppRoute.RULES.route}/{gameName}/{location}",
                arguments = listOf(
                    navArgument("gameName") { type = NavType.StringType },
                    navArgument("location") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val gameName = Uri.decode(backStackEntry.arguments?.getString("gameName") ?: "Unknown Game")
                val locationName = Uri.decode(backStackEntry.arguments?.getString("location") ?: "Unknown Location")
                // Llamamos al RulesScreen correcto
                RulesScreen(
                    navController = navController,
                    gameName = gameName,
                    locationName = locationName // Pasa también la ubicación
                )
            }
            composable(
                route = "${AppRoute.LOCATION.route}/{location}",
                arguments = listOf(navArgument("location") { type = NavType.StringType })
            ) { backStackEntry ->
                val locationName = Uri.decode(backStackEntry.arguments?.getString("location") ?: "Unknown Location")
                LocationScreen(navController = navController, locationName = locationName)
            }

            composable(AppRoute.QUIZ.route) {
                QuizPage(
                    "Charles bridge",
                    onSubmit = { submitted -> /* Handle submission */ }
                )
            }

            composable(AppRoute.SCAN.route) {
                ScanPage(
                    modifier = Modifier,
                    navController = navController,
                    authViewModel = authViewModel
                )
            }

        }
    }
}

private fun ShowBottomBarCheck(navController: NavHostController): Boolean {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    return currentRoute?.let { route ->
        AppRoute.bottomNavRoutes.contains(route)
    } ?: false
}

enum class AppRoute(val route: String) {
    WELCOME("welcome"),
    LOGIN("login"),
    SIGNUP("signup"),
    FORGOT_PASSWORD("forgot_password"),
    HOME("home"),
    SCAN("scan"),
    PROFILE("profile"),
    TEAM("team"),
    QUIZ("quiz"),
    JOINGAME("joingame"),
    GAME("game"),
    RULES("rules"),
    LOCATION("location");

    companion object {
        val bottomNavRoutes = values()
            .filterNot {
                it == LOGIN ||
                        it == SIGNUP ||
                        it == WELCOME ||
                        it == RULES ||
                        it == FORGOT_PASSWORD // Excluir Forgot Password
            }
            .map { it.route }
            .toSet()
    }
}
