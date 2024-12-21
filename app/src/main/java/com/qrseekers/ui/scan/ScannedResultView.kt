package com.qrseekers.ui.scan

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qrseekers.AppRoute
import com.qrseekers.viewmodels.AuthViewModel
import com.qrseekers.viewmodels.ZoneViewModel

@Composable
fun ScanResultView(
    navController: NavController,
    zoneViewModel: ZoneViewModel,
    scannedUrl: String?,
    isCameraVisible: Boolean,
    onRescan: () -> Unit
) {
    // Check if the passkey is valid
    val isPasskeyValid = scannedUrl?.let { zoneViewModel.comparePasskey(it) } ?: false

    // Results View
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scanned Result",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = scannedUrl ?: "No URL found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if the passkey is invalid
        if (!isPasskeyValid) {
            Text(
                text = "Wrong passkey! Please try scanning again.",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                ),
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Open Browser Button (only enable if passkey matches)
            Button(
                onClick = {
                    if (isPasskeyValid) {
                        navController.navigate(AppRoute.QUIZ.route)
                    } else {
                        // Optionally, show an error or message if the passkey doesn't match
                    }
                },
                enabled = isPasskeyValid, // Enable the button only if passkey matches
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Icon(Icons.Default.Search, contentDescription = "Open Browser")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Open quiz")
            }

            // Rescan Button
            Button(
                onClick = {
                    onRescan()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text("Rescan")
            }
        }
    }
}
