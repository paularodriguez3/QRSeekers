package com.qrseekers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.qrseekers.ui.Game

import com.qrseekers.ui.theme.QRSeekersTheme
import com.qrseekers.ui.LoginScreen
import com.qrseekers.ui.RegisterScreen
import com.qrseekers.ui.JoinGameScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRSeekersTheme {
                //LoginScreen(onLogin = {_,_->})
                //RegisterScreen(onRegister = {_,_->})
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
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QRSeekersTheme {
        //LoginScreen(onLogin = {_,_->})
        RegisterScreen(onRegister = {_,_->})

    }
}