package com.qrseekers.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun JoinGameScreen(
    games: List<Game>,
    onGameSelected: (Game) -> Unit,
    onImportGame: () -> Unit
) {
    // State to track the currently selected game
    var selectedGame by remember { mutableStateOf(games.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Scrollable game list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(games.size) { index ->
                val game = games[index]
                GameItem(
                    game = game,
                    isSelected = game == selectedGame,
                    onSelect = { selectedGame = game }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Import Game button with subtle styling
        ClickableText(
            text = AnnotatedString("Import game from file"),
            onClick = { onImportGame() },
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit selection button
        Button(
            onClick = { onGameSelected(selectedGame) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Submit Selection")
        }
    }
}

@Composable
fun GameItem(
    game: Game,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // Game item with selectable functionality
    val borderModifier = if (isSelected) {
        Modifier.border(BorderStroke(4.dp, Color.Black), shape = RoundedCornerShape(8.dp))
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .selectable(selected = isSelected, onClick = onSelect)
            .then(borderModifier)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Game information (name and description)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = game.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = game.description, style = MaterialTheme.typography.bodyMedium)
        }

        // Game image (right side)
        Image(
            painter = painterResource(id = game.imageRes),
            contentDescription = game.name,
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
    }
}

// Data model for a game
data class Game(
    val name: String,
    val description: String,
    val imageRes: Int
)

// Usage example (you can replace this with actual data in your app)
@Composable
@Preview
fun JoinGameScreenPreview() {
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