package com.qrseekers

import BottomNavigationBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qrseekers.ui.ForgotPasswordScreen
import com.qrseekers.ui.ResultsScreen
import com.qrseekers.ui.HomePage
import com.qrseekers.ui.JoinGameScreen
import com.qrseekers.ui.ZoneScreen
import com.qrseekers.ui.LoginPage
import com.qrseekers.ui.ProfilePage
import com.qrseekers.ui.QuizPage
import com.qrseekers.ui.RulesScreen
import com.qrseekers.ui.ScanPage
import com.qrseekers.ui.SignUpPage
import com.qrseekers.ui.WelcomeScreen
import com.qrseekers.viewmodels.AuthState
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.GameViewModel
import com.qrseekers.viewmodels.QuizViewModel
import com.qrseekers.viewmodels.ZoneViewModel

@Composable
fun AppNavigation(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    quizViewModel: QuizViewModel,
    gameViewModel: GameViewModel,
    zoneViewModel: ZoneViewModel,
) {
    val authState by authViewModel.authState.observeAsState(initial = AuthState.Loading)

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // State to track if bottom bar should be shown
    val showBottomBar = remember { mutableStateOf(false) }

    // Check authentication state
    LaunchedEffect(Unit) {
        authViewModel.checkUserAuthentication()
    }

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
            startDestination = AppRoute.WELCOME.route, // Siempre empieza en WelcomeScreen
            modifier = Modifier.padding(innerPadding)
        ) {
            // WelcomeScreen
            composable(AppRoute.WELCOME.route) {
                WelcomeScreen(navController, authState)
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

            composable(AppRoute.JOINGAME.route) {
                JoinGameScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    gameViewModel = gameViewModel,
                    zoneViewModel = zoneViewModel,
                )
            }

            composable(AppRoute.PROFILE.route) {
                ProfilePage(modifier, navController, authViewModel)
            }


            composable(AppRoute.RULES.route) {
                RulesScreen(
                    navController = navController,
                    gameViewModel = gameViewModel
                )
            }

            composable(AppRoute.ZONE.route) {
                ZoneScreen(navController = navController, zoneViewModel = zoneViewModel)
            }

            composable(AppRoute.QUIZ.route) {
                QuizPage(
                    authViewModel = authViewModel,
                    zoneViewModel = zoneViewModel,
                    quizViewModel = quizViewModel,
                    navController = navController,
                    )
            }

            composable(AppRoute.SCAN.route) {
                ScanPage(
                    modifier = Modifier,
                    navController = navController,
                    zoneViewModel = zoneViewModel,
                    authViewModel = authViewModel,
                )
            }

            composable(AppRoute.RESULTS.route + "?allCorrect={allCorrect}") { backStackEntry ->
                // Recuperar el valor del argumento "allCorrect" desde los argumentos de navegación
                val allCorrect = backStackEntry.arguments?.getString("allCorrect")?.toBoolean() ?: false
                ResultsScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    allCorrect = allCorrect // Pasar el valor a ResultsScreen
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
    WELCOME("welcome"),  // Nueva ruta para la pantalla de bienvenida
    LOGIN("login"),
    SIGNUP("signup"),
    FORGOT_PASSWORD("forgot_password"),
    SCAN("scan"),
    PROFILE("profile"),
    QUIZ("quiz"),
    JOINGAME("joingame"),
    RULES("rules"),
    ZONE("zone"),
    RESULTS("results");

    companion object {
        val bottomNavRoutes = values()
            .filterNot {
                it == WELCOME ||
                        it == LOGIN ||
                        it == SIGNUP ||
                        it == RULES ||
                        it == FORGOT_PASSWORD ||
                        it == PROFILE
            }
            .map { it.route }
            .toSet()
    }
}

