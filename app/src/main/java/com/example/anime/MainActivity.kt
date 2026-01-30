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
import com.example.anime.ui.screens.MainScreen
import com.example.anime.ui.theme.AnimeTheme
import retrofit2.Retrofit
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AnimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen1()
                }
            }
        }
    }
}
@Composable
fun MainScreen1(){
    val vm: AnimeViewModel = viewModel(factory = AnimeViewModel.Factory)
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") { MainScreen(vm) { id -> navController.navigate("detail/$id") } }
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