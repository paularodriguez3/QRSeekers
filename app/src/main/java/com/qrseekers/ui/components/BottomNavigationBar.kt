import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.qrseekers.AppRoute

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFE3F2FD), // Fondo suave azul
        tonalElevation = 8.dp, // Sombra ligera
        modifier = Modifier.background(Color(0xFFE3F2FD)) // Fondo completo igual
    ) {
        // Profile Item
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF1E88E5) // Azul principal
                )
            },
            label = {
                Text(
                    "Profile",
                    color = Color(0xFF1E88E5), // Texto azul principal
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            selected = navController.currentDestination?.route == AppRoute.PROFILE.route,
            onClick = { navController.navigate(AppRoute.PROFILE.route) },
            alwaysShowLabel = true, // Asegura que las etiquetas siempre se muestran
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFBBDEFB) // Color de selección más claro
            )
        )

        // Quizzes Item
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.List,
                    contentDescription = "Quizzes",
                    tint = Color(0xFF1E88E5) // Azul principal
                )
            },
            label = {
                Text(
                    "Quizzes",
                    color = Color(0xFF1E88E5),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            selected = navController.currentDestination?.route == AppRoute.JOINGAME.route,
            onClick = { navController.navigate(AppRoute.JOINGAME.route) },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFBBDEFB)
            )
        )
    }
}
