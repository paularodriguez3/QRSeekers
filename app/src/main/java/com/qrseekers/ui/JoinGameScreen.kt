package com.qrseekers.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.qrseekers.AppRoute
import com.qrseekers.R

@Composable
fun JoinGameScreen(
    onGameSelected: (Game) -> Unit,
    navController: NavController
) {
    val games = listOf(
        Game(
            name = "Prague discovery",
            description = "Explore the hidden gems of Prague in this fun quiz!",
            imageRes = R.drawable.prague_image,
            color = Color(0xFFFDE68A), // Amarillo claro
            location = "Charles Bridge"
        ),
        Game(
            name = "Las Palmas discovery",
            description = "Uncover the vibrant culture of Las Palmas in this engaging quiz!",
            imageRes = R.drawable.las_palmas_image,
            color = Color(0xFFBBF7D0), // Verde claro
            location = "Santa Ana Cathedral"
        ),
        Game(
            name = "QRseekers indoor",
            description = "Challenge yourself with this exciting indoor quiz!",
            imageRes = R.drawable.indoor_image,
            color = Color(0xFFBFDBFE), // Azul claro
            location = "Student Union Hall"
        )
    )

    var selectedGame by remember { mutableStateOf<Game?>(null) }

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
            items(games.size) { index ->
                val game = games[index]
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
                selectedGame?.let { game ->
                    val encodedGameName = Uri.encode(game.name)
                    val encodedLocation = Uri.encode(game.location)
                    navController.navigate("${AppRoute.RULES.route}/$encodedGameName/$encodedLocation")
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
                    .background(game.color.copy(alpha = 0.1f))
                    .border(4.dp, Color(0xFF1E88E5), RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = game.color),
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
                painter = painterResource(id = game.imageRes),
                contentDescription = game.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Modelo de datos del juego
data class Game(
    val name: String,
    val description: String,
    val imageRes: Int,
    val color: Color, // Color de fondo de la tarjeta
    val location: String
)
