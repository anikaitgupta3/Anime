package com.example.anime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.anime.data.AnimeRepository
import com.example.anime.data.database.AnimeDatabase
import com.example.anime.data.network.JikanApi
import com.example.anime.ui.screens.AnimeListScreen
import com.example.anime.ui.screens.AnimeViewModel
import com.example.anime.ui.screens.DetailScreen
import com.example.anime.ui.theme.AnimeTheme
import retrofit2.Retrofit

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Manual DI for simplicity
        /*val db = Room.databaseBuilder(applicationContext, AnimeDatabase::class.java, "anime_db").build()
        val api = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(JikanApi::class.java)
        val repo = AnimeRepository(api, db.animeDao())*/
        //val vm = AnimeViewModel()


        setContent {
            AnimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}
@Composable
fun MainScreen(){
    val vm: AnimeViewModel = viewModel(factory = AnimeViewModel.Factory)
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") { AnimeListScreen(vm) { id -> navController.navigate("detail/$id") } }
        composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            DetailScreen(it.arguments?.getInt("id") ?: 0, vm)
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
    AnimeTheme {
        Greeting("Android")
    }
}