package com.example.anime.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AnimeListScreen(viewModel: AnimeViewModel, onAnimeClick: (Int) -> Unit) {
    val list by viewModel.animeList.collectAsState()


    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(list) { anime ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onAnimeClick(anime.mal_id) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.height(120.dp)) {
                    AsyncImage(
                        model = anime.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.width(90.dp).fillMaxHeight(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = android.R.drawable.ic_menu_report_image)
                    )
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(anime.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                        Text("Episodes: ${anime.episodes ?: "N/A"}")
                        Text("Rating: ⭐ ${anime.score ?: "N/A"}")
                    }
                }
            }
        }
    }
}