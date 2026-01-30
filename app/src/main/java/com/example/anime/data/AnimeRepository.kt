package com.example.anime.data

import android.util.Log
import androidx.sqlite.SQLiteException
import com.example.anime.data.database.AnimeDao
import com.example.anime.data.database.AnimeEntity
import com.example.anime.data.network.JikanApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okio.IOException

interface AnimeRepository {
    suspend fun syncAndGetAnime(): Flow<Result>
    fun getAnime(id: Int): Flow<AnimeEntity?>
    suspend fun fetchAndStoreCast(id: Int)

}

sealed class Result {
    data class Success(val listOfAllAnime: List<AnimeEntity>) : Result()
    object Loading : Result()
    object Error : Result()
}

class AnimeRepositoryImpl(private val api: JikanApi, private val dao: AnimeDao) : AnimeRepository {
    //This will emit the anime for the detail screen
    override fun getAnime(id: Int): Flow<AnimeEntity?> {
        return dao.getAnimeById(id)
            .catch { e ->
                Log.e("DatabaseError", "Error observing anime $id", e)
                emit(null)
            }
    }
    //This will be emitting the list of anime for the initial screen and error/loading states to show

    override suspend fun syncAndGetAnime(): Flow<Result> = flow {
        val listFromDao = dao.getAllAnime().first()
        if (!listFromDao.isEmpty()) {
            emit(Result.Success(listFromDao))
        } else {
            emit(Result.Loading)
        }
        try {
            val response = api.getTopAnime()
            if (response.isSuccessful && response.body() != null) {
                val entities = response.body()!!.data.map { dto ->
                    AnimeEntity(
                        mal_id = dto.mal_id,
                        title = dto.title,
                        episodes = dto.episodes,
                        score = dto.score,
                        synopsis = dto.synopsis,
                        imageUrl = dto.images?.jpg?.image_url,
                        trailerUrl = dto.trailer?.embed_url,
                        genres = dto.genres?.joinToString { it.name ?: " " }
                    )
                }
                dao.insertAll(entities)
            } else if (listFromDao.isEmpty()) {
                emit(Result.Error)
            }
            val newListFromDao = dao.getAllAnime().first()
            if (!newListFromDao.isEmpty()) {
                emit(Result.Success(newListFromDao))
            } else {
                emit(Result.Error)
            }
        } catch (e: Exception) {
            val newListFromDao = dao.getAllAnime().first()
            if (!newListFromDao.isEmpty()) {
                emit(Result.Success(newListFromDao))
            } else {
                emit(Result.Error)
            }
        }
    }
    //This will fetch the cast values and store them in room database when user navigates to detail screen
    override suspend fun fetchAndStoreCast(id: Int) {
        try {
            if (dao.getAnimeById(id).first()?.cast.isNullOrEmpty()) {
                val characterResponse = api.getAnimeCharacters(id)
                if (characterResponse.isSuccessful && characterResponse.body() != null) {
                    // Take top 5 actors/characters
                    val castString =
                        characterResponse.body()!!.data.take(5)
                            .joinToString { (it.character?.name ?: "Loading cast...") }

                    val currentAnime = dao.getAnimeById(id).first()
                    currentAnime?.let {
                        dao.insertAll(listOf(it.copy(cast = castString)))
                    }
                }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception) {
        when (e) {
            is IOException -> {

            }

            is SQLiteException -> {
                Log.e("DB_ERROR", "Database access failed: ${e.message}")
            }

            else -> {
                Log.e("GENERAL_ERROR", "An unknown error occurred: ${e.message}")
            }
        }
    }
}