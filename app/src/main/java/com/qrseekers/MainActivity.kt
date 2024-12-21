package com.qrseekers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.ui.theme.QRSeekersTheme
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.GameViewModel
import com.qrseekers.viewmodels.QuizViewModel
import com.qrseekers.viewmodels.ZoneViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        val quizViewModel: QuizViewModel by viewModels()
        val gameViewModel: GameViewModel by viewModels()
        val zoneViewModel: ZoneViewModel by viewModels()



        setContent {
            QRSeekersTheme {
                AppNavigation(
                    modifier = Modifier.fillMaxSize(),
                    authViewModel = authViewModel,
                    quizViewModel = quizViewModel,
                    gameViewModel = gameViewModel,
                    zoneViewModel = zoneViewModel,
                )
            }
        }
    }
}
