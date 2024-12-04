package com.qrseekers.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController) {
    Text(text = "Profile page")

    /* var name by remember { mutableStateOf("John Doe") }
     var username by remember { mutableStateOf("johndoe") }
     var newPassword by remember { mutableStateOf("") }

     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(16.dp),
         verticalArrangement = Arrangement.Top,
         horizontalAlignment = Alignment.Start
     ) {
         Text("Profile", style = MaterialTheme.typography.headlineMedium)

         Spacer(modifier = Modifier.height(16.dp))

         ProfileField(label = "Name", value = name) { name = it }
         ProfileField(label = "Username", value = username) { username = it }
         ProfileField(label = "New Password", value = newPassword) { newPassword = it }

         Spacer(modifier = Modifier.height(16.dp))

         Button(onClick = { /* Logic to change password */ }) {
             Text("Change Password")
         }

         Spacer(modifier = Modifier.height(16.dp))

         Text("Current Game: Chess")
         ActionRow("Modify Game", "View Game") { /* Logic for game actions */ }

         Spacer(modifier = Modifier.height(16.dp))

         Text("Current Team: Team A")
         ActionRow("Modify Team", "View Team") { /* Logic for team actions */ }
     }*/
}

@Composable
fun ProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(value = value, onValueChange = onValueChange, label = { Text(label) })
}

@Composable
fun ActionRow(action1: String, action2: String, onActionClick: () -> Unit) {
    Row {
        Button(onClick = onActionClick) { Text(action1) }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onActionClick) { Text(action2) }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfilePage(navController = rememberNavController())
}
