package com.example.anime.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.anime.AnimeApp
import com.example.anime.data.AnimeRepository
import com.example.anime.data.Result
import com.example.anime.data.database.AnimeEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
sealed class AnimeState {
    object Loading : AnimeState()
    object Error: AnimeState()
    data class Success(val list:List<AnimeEntity>): AnimeState()
}

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _animeUiState:MutableStateFlow<AnimeState> = MutableStateFlow(AnimeState.Loading)
    val animeUiState: StateFlow<AnimeState> = _animeUiState
    init{
        getAndSyncAnime()
    }
    private var syncJob: Job? = null
    fun getAndSyncAnime(){
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            repository.syncAndGetAnime().collect {result ->
                when(result){
                    is Result.Success ->{
                        _animeUiState.value= AnimeState.Success(result.listOfAllAnime)
                    }
                    is Result.Error -> {
                        _animeUiState.value= AnimeState.Error
                    }
                    is Result.Loading -> {
                        _animeUiState.value= AnimeState.Loading
                    }
                }
            }
        }
    }
    private val _detailState = MutableStateFlow<AnimeEntity?>(null)
    val detailState = _detailState.asStateFlow()
    private var fetchJob: Job? = null
    fun loadAnimeDetails(id: Int) {
        fetchJob?.cancel()

        _detailState.value = null
        fetchJob = viewModelScope.launch {
            repository.getAnime(id).collect { anime ->
                _detailState.value = anime
            }
        }
    }
    fun syncCast(id: Int) {
        viewModelScope.launch {
            repository.fetchAndStoreCast(id)
        }
    }

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