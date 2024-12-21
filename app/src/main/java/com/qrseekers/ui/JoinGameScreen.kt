package com.qrseekers.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.qrseekers.AppRoute
import com.qrseekers.data.Game
import com.qrseekers.viewmodels.AuthState
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.GameViewModel
import com.qrseekers.viewmodels.ZoneViewModel

@Composable
fun JoinGameScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    gameViewModel: GameViewModel,
    zoneViewModel: ZoneViewModel
) {


    var selectedGame by remember { mutableStateOf<Game?>(null) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Observe authentication state
    val authState by authViewModel.authState.observeAsState(AuthState.Loading)


    // Handle navigation based on authentication state
    LaunchedEffect(authState) {

        when (authState) {
            is AuthState.Unauthenticated -> {
                navController.navigate(AppRoute.LOGIN.route) {
                    popUpTo(AppRoute.JOINGAME.route) { inclusive = true }
                }
            }
            is AuthState.Authenticated -> {
                // Load game and zone data
                gameViewModel.loadGames()
                zoneViewModel.loadZones()
            }
            else -> Unit // Handle Loading or Error states if needed
        }
    }

    // Optional: Display error message if needed
    errorMessage?.let {
        Text(text = it, color = MaterialTheme.colorScheme.error)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Encabezado
        Text(
            text = "QRseekers",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Join a game\nSubmit your game selection when ready",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color(0xFF6AB7FF),
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Lista de juegos
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gameViewModel.games.value.size) { index ->
                val game = gameViewModel.games.value[index]
                GameCard(
                    game = game,
                    isSelected = selectedGame == game,
                    onClick = { selectedGame = game }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de envío estilizado
        Button(
            onClick = {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                selectedGame?.let {
                    gameViewModel.setCurrentGame(it.id)
                    Log.d("GameViewModel", "Set current game with ID: ${it.id}")

                    authViewModel.setUserGameParticipation(userId, it.id, it.name)
                    Log.d("AuthViewModel", "User $userId set to participate in game with ID: ${it.id}")

                    zoneViewModel.setCurrentZone(gameViewModel.currentGame.value?.zones?.get(0) ?: "errorr")
                    Log.d("ZoneViewModel", "Set current zone for game with ID: ${it.id}, Zone: ${gameViewModel.currentGame.value!!.zones.get(0) ?: "errorr"}")

                    navController.navigate(AppRoute.RULES.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            enabled = selectedGame != null,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedGame != null) Color(0xFF1E88E5) else Color(0xFFB0BEC5),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
        ) {
            Text(
                text = "Submit your selection",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}



@Composable
fun GameCard(
    game: Game,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier
                    .background(game.getColor().copy(alpha = 0.1f))
                    .border(4.dp, Color(0xFF1E88E5), RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = game.getColor()),
        elevation = if (isSelected) CardDefaults.cardElevation(16.dp) else CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del juego
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = game.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                )
            }
            // Imagen del juego
            Image(
                painter = painterResource(id = game.getImageResId()),
                contentDescription = game.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
