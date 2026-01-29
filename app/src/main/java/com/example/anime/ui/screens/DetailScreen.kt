package com.example.anime.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.anime.data.database.AnimeEntity

@Composable
fun DetailScreen(animeId: Int, viewModel: AnimeViewModel) {
    var anime by remember { mutableStateOf<AnimeEntity?>(null) }
    LaunchedEffect(animeId) {
        anime = viewModel.getAnimeById(animeId)
        viewModel.syncCast(animeId)
    }

    anime?.let { item ->
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (!item.trailerUrl.isNullOrEmpty()) {
                VideoPlayer(url = item.trailerUrl, modifier = Modifier.fillMaxWidth().height(250.dp))
            } else {
                AsyncImage(model = item.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(250.dp), contentScale = ContentScale.Fit)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                // Display Main Cast
                Text("Main Cast", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = item.cast ?: "Loading cast...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (item.cast == null) Color.Gray else Color.Black
                )
                Text("Rating: ⭐ ${item.score ?: 0.0} | Episodes: ${item.episodes ?: "N/A"}", color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Genres: ${item.genres}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Synopsis", style = MaterialTheme.typography.titleLarge)
                Text(item.synopsis ?: "No description.")
            }
        }
    }
}
@Composable
fun VideoPlayer(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }
    AndroidView(
        factory = { ctx -> PlayerView(ctx).apply { player = exoPlayer } },
        modifier = modifier
    )
    DisposableEffect(Unit) { onDispose { exoPlayer.release() } }
}