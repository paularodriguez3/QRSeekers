package com.qrseekers.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.Game
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GameViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Current game for the user
    private val _currentGame = mutableStateOf<Game?>(null)
    val currentGame: State<Game?> get() = _currentGame




    // Using mutableStateOf to hold the list of games
    private val _games = mutableStateOf<List<Game>>(emptyList())
    val games: State<List<Game>> get() = _games

    private val logTag = "GamesViewModel"

    // Function to load games from Firestore
    fun loadGames() {
        viewModelScope.launch {
            try {
                // Fetch the collection of games from Firestore
                val gamesSnapshot = firestore.collection("Games").get().await()

                // Map the documents to Game objects
                val gamesList = gamesSnapshot.documents.mapNotNull { document ->
                    document.toObject(Game::class.java)?.apply {
                        id = document.id // Set the document ID as the game ID
                    }
                }

                // Update the state with the list of games
                _games.value = gamesList
                Log.d(logTag, "Loaded games: $gamesList")

            } catch (e: Exception) {
                // Handle error (for example, logging or showing an error message)
                Log.e(logTag, "Error loading games: ${e.localizedMessage}")
            }
        }
    }

    // Function to set the current game for the user
    fun setCurrentGame(gameId: String) {
        val game = _games.value.find { it.id == gameId }
        if (game != null) {
            _currentGame.value = game
            Log.d(logTag, "Current game set to: $game")
        } else {
            Log.e(logTag, "Game with ID $gameId not found")
        }
    }

    // Optionally, you can clear the current game when the user logs out or exits the game
    fun clearCurrentGame() {
        _currentGame.value = null
        Log.d(logTag, "Current game cleared")
    }

}
