package com.qrseekers

import BottomNavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.qrseekers.ui.theme.QRSeekersTheme
import com.qrseekers.viewmodels.AuthViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            QRSeekersTheme {
                    AppNavigation(
                        modifier = Modifier.fillMaxSize(),
                        authViewModel = authViewModel
                    )

            }
        }
    }
}