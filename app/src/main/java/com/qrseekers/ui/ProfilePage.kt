package com.qrseekers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.qrseekers.AppRoute

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with Close Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Close, // Replace with a custom icon if needed
                contentDescription = "Close",
                modifier = Modifier.clickable {
                    // Navigate back
                    navController.popBackStack()
                }
            )
            Text(
                text = "QRseekers",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        // Profile Picture
        Image(
            painter = painterResource(id = android.R.drawable.ic_dialog_map), // Replace with your drawable
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        // Profile Name
        Text(
            text = "ELLA",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        // Participation and Team Info
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow(label = "participates:", value = "Prague game", navController, AppRoute.GAME.route, AppRoute.GAME.route)
            InfoRow(label = "TEAM:", value = "onions", navController, AppRoute.TEAM.route, AppRoute.TEAM.route)
        }

        // Options Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Place, contentDescription = "Organization View")
            Spacer(modifier = Modifier.width(8.dp))
            Text("enable org view track zones (i)", style = MaterialTheme.typography.bodyMedium)
        }

        // Email Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Email, contentDescription = "Email")
            Spacer(modifier = Modifier.width(8.dp))
            Text("ella@gmail.com", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, navController: NavController, editRoute: String, infoRoute: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label $value",
            style = MaterialTheme.typography.bodyMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = Icons.Default.Edit, // Replace with appropriate edit icon
                contentDescription = "Edit",
                modifier = Modifier.clickable {
                    navController.navigate(editRoute) // Pass the route as a String
                }
            )
            Icon(
                imageVector = Icons.Default.Info, // Replace with appropriate QR code icon
                contentDescription = "Info",
                modifier = Modifier.clickable {
                    navController.navigate(infoRoute) // Pass the route as a String
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfilePage(navController = rememberNavController())
}

