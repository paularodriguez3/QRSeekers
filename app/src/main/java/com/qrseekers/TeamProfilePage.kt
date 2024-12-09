package com.qrseekers


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.Update


@Composable
fun TeamProfilePage(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Team Name
        Text(
            text = "ONIONS",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Points Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "60 POINTS",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.Red)
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Updated Time",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "updated 13:00:07",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Team Members Section
        Text(
            text = "it has 6 members: Anna, Filip, Paula, Roger, Lena and Ella",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Team ID
        Text(
            text = "123 456",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Current Ranking Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "And you are currently 14th out of 60 teams.",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "updated 14.7. 14:00",
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Updated Time"
                )
            }
        }

        // QR Code Image
        Image(
            painter = painterResource(id =  android.R.drawable.ic_dialog_map), // Replace with your QR code image
            contentDescription = "Team QR Code",
            modifier = Modifier
                .size(120.dp)
                .padding(vertical = 16.dp)
        )

        // Team Status Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Prague Discovery Game",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Indicator (Green)
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Text(
                    text = "02:04:05",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamProfilePagePreview() {
    // Simulate navigation using rememberNavController
    val navController = rememberNavController()

    // Provide the TeamProfilePage composable with the necessary parameters
    TeamProfilePage(
        modifier = Modifier.fillMaxSize(),
        navController = navController
    )
}
