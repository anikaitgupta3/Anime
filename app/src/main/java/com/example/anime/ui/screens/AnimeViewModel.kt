package com.example.anime.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.anime.AnimeApp
import com.example.anime.data.AnimeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    val animeList = repository.getAllAnime().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { repository.syncAnime() }
    }
    fun syncCast(id: Int) {
        viewModelScope.launch {
            repository.fetchAndStoreCast(id)
        }
    }

    suspend fun getAnimeById(id: Int) = repository.getAnime(id)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AnimeApp)
                val animeRepository = application.container.animeRepository
                AnimeViewModel(repository = animeRepository)
            }
        }
    }
}