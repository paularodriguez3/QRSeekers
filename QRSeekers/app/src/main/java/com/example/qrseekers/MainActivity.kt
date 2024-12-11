package com.example.qrseekers

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

//Esta aplicacion esta desarrollada usando la tecnologia de creacion de UI modernas Jetpack Compose
//y siguiendo una filosofia de Single Activity Architecture
class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: ItemsDatabase
    }

    //Se crea la actividad indicando una funcion que usa elementos de navegacion de Jetpack Compose
    //y se indica que se usan elementos de Material Design (libreria moderna de diseño)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            database = ItemsDatabase.getDatabase(this)
        }catch (ex: Exception){
            println(ex)
        }
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }

    //Esta funcion sirve a modo de "router" indicando las pantallas y que funciones las gestionan
    //recordando (remember) los estados de cada una para mejorar eficiencia

    //Todas las funciones de Jetpack Compose deben llevar esta anotacion para indicar que su
    //objetivo es crear elementos de UI
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "welcome_screen") {
            composable("welcome_screen") { WelcomeScreen(navController) }
            composable("quiz_location_screen") { QuizLocationScreen(navController) }
            composable("rules_screen") { RulesScreen(navController) }
            composable("qr_scanner_screen") { QRScannerScreen() }
            composable(
                "question_screen/{idLocation}",
                arguments = listOf(navArgument("idLocation") { type = NavType.StringType })
            ) { backStackEntry ->
                val idLocation = backStackEntry.arguments?.getString("idLocation")
                QuestionScreen(navController, idLocation)
            }
            composable(
                "game_over_screen/{points}",
                arguments = listOf(navArgument("points") { type = NavType.StringType })
            ) { backStackEntry ->
                val points = backStackEntry.arguments?.getString("points")
                GameOverScreen(navController, points)
            }
        }
    }

    //---------------------------SECCION COMPONENTES REUSABLES-----------------------------------
    //-------------------------------------------------------------------------------------------

    //Funcion reusable de un boton con fondo azul, letras blancas y esquinas redondeadas
    @Composable
    fun ReusableSimpleButton(navController: NavController, route: String, text: String) {
        Button(
            onClick = { navController.navigate(route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text(text = text, color = Color.White, fontSize = 16.sp)
        }
    }

    //Funcion reusable de la barra superior centrada
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReusableTitle() {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFc8eefe),
                titleContentColor = Color(0xff639de8),
            ),
            title = {
                Text(
                    text = "QRseekers",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        )
    }

    //-------------------------------------------------------------------------------------------
    //---------------------------FIN SECCION COMPONENTES REUSABLES-------------------------------

    //############################################################################################//
    //---------------------------FUNCIONES POR CADA PANTALLA--------------------------------------//
    //############################################################################################//

    //----------------------------Funcion que define la pantalla inicial--------------------------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun WelcomeScreen(navController: NavController) {
        //Surface es un elemento de Material Design que facilita la agrupacion de elementos de UI
        //Se le agrega un modificador para indicar que ocupe toda la pantalla y el color de fondo
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFE3F2FD) // Color de fondo
        ) {
            //El elemento Column agrupa subelementos y los alinea por defecto verticalmente
            //En este caso, se especifica que se haga horizontalmente y dejando espacio vertical
            //Este primer elemento Colum contiene otro Colum con textos e imagen y el boton
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Este segundo elemento Column contiene los textos y la imagen
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "QRseekers",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E88E5)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore the city with friends",
                        fontSize = 16.sp,
                        color = Color(0xFF1E88E5)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo_square), //Logo de la app
                        contentDescription = "QRSeekers Logo",
                        modifier = Modifier
                            .size(250.dp),
                        contentScale = ContentScale.None
                    )
                }
                //Boton Begin que cuando se hace click redirige a la pantalla de seleccion de quiz
                ReusableSimpleButton(navController, "quiz_location_screen", "Begin")
            }
        }
    }

    //--------Funcion que define la pantalla donde se muestran las localizaciones a elegir--------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun QuizLocationScreen(navController: NavController) {
        //Scaffold es una forma de Material Design 3 para estructurar la pantalla facilmente
        Scaffold(
            topBar = { ReusableTitle() } // Reutiliza la barra superior
        ) { innerPadding -> // Recibe el relleno del Scaffold
            //Superficie de la pantalla que es pasada al Scaffold
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Aplica el relleno del Scaffold
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEFF7FF)),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF34eb95))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Join a game",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Submit your game selection when ready",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    val locationsList = database.itemDao().getAllLocations()
                        .catch{e -> print(e)}
                        .collectAsState(initial = emptyList())
                    var boxSelected = -1
                    locationsList.value.forEach { location ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(location.locationColor.toInt()))
                                .padding(16.dp)
                                .clickable(onClick = { boxSelected = location.id})
                        ) {
                            Column{
                                Text(
                                    text = location.locationName,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = location.locationSubtitle,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    ReusableSimpleButton(navController, "question_screen/$boxSelected", "Submit your selection")
                }
            }
        }
    }

    //--------Funcion que define la pantalla donde se muestran las reglas al usuario--------------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun RulesScreen(navController: NavController) {
        Scaffold(
            topBar = { ReusableTitle() } // Reutiliza la barra superior
        ) { innerPadding -> // Recibe el relleno del Scaffold
            //Superficie de la pantalla que es pasada al Scaffold
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Aplica el relleno del Scaffold
                color = Color.White
            ) {
                //Columna que engloba a los textos y al boton inferior
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Columna que engloba a los textos
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "GAME RULES",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = """
                        1. Head to the designated location (We’ll provide the exact location in the next step)
                        
                        2. Look for the QR code and scan it
                        
                        3. Answer the questions
                        
                        4. Answer all questions correctly to win! If you get any wrong, the game is over, and you’ll need to start again.
                        
                        Good luck!
                    """.trimIndent(),
                            fontSize = 16.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }
                    //Boton inferior
                    ReusableSimpleButton(navController, "quiz_screen", "Continue")
                }
            }
        }
    }

    //--------Funcion que define la pantalla--------------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun QRScannerScreen() {
        TODO("Not yet implemented")
    }

    //--------Funcion que define la pantalla--------------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun QuestionScreen(navController: NavController, idLocation: String?) {
        TODO("Not yet implemented")
    }

    //------------------------Funcion que define la pantalla de Game Over-------------------------//
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    @Composable
    fun GameOverScreen(navController: NavController, pointsParam: String?) {
        var points = "0"
        if (!pointsParam.isNullOrEmpty()) points = pointsParam

        Scaffold(
            topBar = { ReusableTitle() } // Reutiliza la barra superior
        ) { innerPadding -> // Recibe el relleno del Scaffold
            //Superficie de la pantalla que es pasada al Scaffold
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Aplica el relleno del Scaffold
                color = Color.White
            ) {
                //Columna que engloba a los textos, la imagen y al boton inferior
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Columna que engloba a los textos y a la imagen
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "GAME OVER",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "CONGRATULATIONS!".trimIndent(),
                            fontSize = 20.sp,
                            color = Color(0xff639de8),
                            lineHeight = 22.sp
                        )
                        Image(
                            painter = painterResource(id = R.drawable.logo_square), //Logo de la app
                            contentDescription = "QRSeekers Logo",
                            modifier = Modifier
                                .size(250.dp),
                            contentScale = ContentScale.None
                        )
                        Text(
                            text = "You got $points points",
                            fontSize = 16.sp,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }
                    //Boton inferior
                    ReusableSimpleButton(navController, "quiz_location_screen", "Continue Playing")
                }
            }
        }
    }


    //############################################################################################//
    //---------------------------FUNCIONES PARA TRABAJAR CON BD-----------------------------------//
    //############################################################################################//


    //------------------BORRAR. PREVIEW PARA PODER PROBAR-------------
    /*@Preview
    @Composable
    fun WelcomeScreenPreview() {
        val navController = rememberNavController()
        WelcomeScreen(navController)
    }*/

/*    @Preview
    @Composable
    fun RulesScreenPreview() {
        val navController = rememberNavController()
        RulesScreen(navController)
    }*/
/*     @Preview
    @Composable
    fun QuizLocationScreenPreview() {
        val navController = rememberNavController()
        QuizLocationScreen(navController)
    }*/

    /*    @Preview
    @Composable
    fun GameOverScreenPreview() {
        val navController = rememberNavController()
        val points = "60"
        GameOverScreen(navController, points)
    }*/

}



    //############################################################################################//
    //############################################################################################//
    //############################################################################################//
    //------------------INTEGRACION CON BASE DE DATOS SQLITE (LIBRERÍA ROOM)----------------------//
    //############################################################################################//
    //############################################################################################//
    //############################################################################################//

    //--------------------------------------CLASES ENTITY-----------------------------------------//


    //CLASE CON LAS LOCALIZACIONES
    @Entity(tableName = "locations")
    data class Locations(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val locationName: String,
        val locationSubtitle: String,
        val locationColor: String
    )

    //CLASE CON LAS PREGUNTAS
    @Entity(tableName = "questions",
            foreignKeys = [
                ForeignKey(
                    entity = Locations::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("idLocation"),
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
                )
            ]
    )
    data class Questions(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val idLocation: Int,
        val questionText: String
    )

    //CLASE CON LAS RESPUESTAS
    @Entity(tableName = "answers",
            foreignKeys = [
                ForeignKey(
                    entity = Questions::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("idQuestion"),
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
                )
            ]
    )
    data class Answers(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val idQuestion: Int,
        val answerText: String,
        val isCorrectAnswer: Boolean
    )

    //---------------------------------------CLASE DAO--------------------------------------------//
    @Dao
    interface ItemsDao {

        @Query("SELECT * from locations ORDER BY id ASC")
        fun getAllLocations(): Flow<List<Locations>>

        @Query("SELECT * from questions ORDER BY id ASC")
        fun getAllQuestions(): Flow<List<Questions>>

        @Query("SELECT * from answers ORDER BY id ASC")
        fun getAllAnswers(): Flow<List<Answers>>

        @Query("SELECT questions.id, questions.idLocation, questions.questionText from questions " +
                "JOIN locations ON questions.idLocation = locations.id" +
                " WHERE locations.id = :idLocation")
        fun getQuestionsFromLocation(idLocation: Int): Flow<List<Questions>>

        @Query("SELECT answers.id, answers.idQuestion, answers.answerText, answers.isCorrectAnswer" +
                " from answers JOIN questions ON answers.idQuestion = questions.id" +
                " WHERE questions.id = :idQuestion")
        fun getAnswersFromQuestion(idQuestion: Int): Flow<List<Answers>>

    }
    //---------------------------------------CLASE ROOM DATABASE----------------------------------//
    @Database(entities = [Locations::class, Questions::class, Answers::class], version = 1, exportSchema = false)
    abstract class ItemsDatabase : RoomDatabase() {

        abstract fun itemDao(): ItemsDao

        companion object {
            @Volatile
            private var Instance: ItemsDatabase? = null

            fun getDatabase(context: Context): ItemsDatabase {
                //Si la instancia no es nula, se devuelve. En caso contrario, se crea nueva Instancia
                return Instance ?: synchronized(this) {
                    Room.databaseBuilder(context, ItemsDatabase::class.java, "item_database")
                        .createFromAsset("QRSeekersData.db")
                        .build()
                        .also { Instance = it }
                }
            }
        }
    }
