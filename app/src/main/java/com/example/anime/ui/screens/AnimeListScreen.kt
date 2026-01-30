package com.example.anime.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.anime.R
import com.example.anime.data.database.AnimeEntity

@Composable
fun MainScreen(viewModel: AnimeViewModel, onAnimeClick: (Int) -> Unit){
    val uiState =viewModel.animeUiState.collectAsStateWithLifecycle().value
    when(uiState){
        is AnimeState.Loading -> LoadingScreen()
        is AnimeState.Success -> {
            AnimeListScreen(uiState.list,onAnimeClick)
        }
        is AnimeState.Error -> ErrorScreen({ viewModel.getAndSyncAnime() })
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}
@Composable
fun ErrorScreen(retryAction: () -> Unit,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
@Composable
fun AnimeListScreen(list: List<AnimeEntity>, onAnimeClick: (Int) -> Unit) {
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