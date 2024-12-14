package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun JoinGameScreen(
    onGameSelected: (Game) -> Unit,
    onImportGame: () -> Unit
) {
    val games = listOf(
        Game(
            name = "Prague discovery",
            description = "Explore the hidden gems of Prague in this fun quiz!",
            imageRes = android.R.drawable.ic_dialog_map
        ),
        Game(
            name = "Las Palmas discovery",
            description = "Uncover the vibrant culture of Las Palmas in this engaging quiz!",
            imageRes = android.R.drawable.ic_dialog_map
        ),
        Game(
            name = "QRseekers indoor",
            description = "Challenge yourself with this exciting indoor quiz!",
            imageRes = android.R.drawable.ic_dialog_map
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
        Text(
            text = "Join a Game",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

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

        Button(
            onClick = {
                selectedGame?.let { onGameSelected(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedGame != null
        ) {
            Text("Submit your selection")
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
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E88E5)
                    )
                )
                Text(
                    text = game.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = game.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

data class Game(
    val name: String,
    val description: String,
    val imageRes: Int
)
