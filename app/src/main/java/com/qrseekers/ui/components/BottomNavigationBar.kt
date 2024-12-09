import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.navigation.compose.rememberNavController

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavController
import com.qrseekers.AppRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController : NavController
) {
    //val navController = rememberNavController()

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate(AppRoute.PROFILE.route)}

        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "Scan") },
            label = { Text("Scan") },
            selected = false,
            onClick = { navController.navigate(AppRoute.SCAN.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(AppRoute.HOME.route) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    //BottomNavigationBar()
}
