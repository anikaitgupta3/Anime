package com.example.anime.data

import android.util.Log
import androidx.sqlite.SQLiteException
import com.example.anime.data.database.AnimeDao
import com.example.anime.data.database.AnimeEntity
import com.example.anime.data.network.JikanApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import okio.IOException

interface AnimeRepository{
    fun getAllAnime(): Flow<List<AnimeEntity>>
    suspend fun syncAnime()
    suspend fun getAnime(id: Int): AnimeEntity?
    suspend fun fetchAndStoreCast(id: Int)

}
class AnimeRepositoryImpl(private val api: JikanApi, private val dao: AnimeDao): AnimeRepository {
    //val allAnime = dao.getAllAnime()
    /*override fun getAllAnime(): Flow<List<AnimeEntity>> {
        return dao.getAllAnime()
    }*/
    override suspend fun getAnime(id: Int): AnimeEntity? {
        return try {
            dao.getAnimeById(id)
        } catch (e: Exception) {
            // Log the error for debugging
            Log.e("DatabaseError", "Failed to fetch anime $id: ${e.message}")
            // Return null or handle based on your UI needs
            null
        }
    }


    override suspend fun syncAnime() {
        try {
            val response = api.getTopAnime()
            val entities = response.data.map { dto ->
                AnimeEntity(
                    mal_id = dto.mal_id,
                    title = dto.title,
                    episodes = dto.episodes,
                    score = dto.score,
                    synopsis = dto.synopsis,
                    imageUrl = dto.images.jpg.image_url,
                    trailerUrl = dto.trailer.embed_url,
                    genres = dto.genres.joinToString { it.name ?:" " }
                )
            }
            dao.insertAll(entities)
        } catch (e: Exception) {
            e.printStackTrace() // Handle network error (Offline First: user sees cached data)
        }
    }
    // Fetch cast only when needed and save to Room
    override suspend fun fetchAndStoreCast(id: Int) {
        try {
            val characterResponse = api.getAnimeCharacters(id)
            // Take top 5 actors/characters
            val castString = characterResponse.data.take(5).joinToString { it.character.name }

            // Update specific record in Room
            val currentAnime = dao.getAnimeById(id)
            currentAnime?.let {
                dao.insertAll(listOf(it.copy(cast = castString)))
            }
        } catch (e: Exception) {
            handleDatabaseError(e)
        }
    }
    private fun handleDatabaseError(e: Exception) {
        when (e) {
            is IOException -> {
                // Network failure: No action needed for offline-first
                // as Room still has the data.
            }
            is SQLiteException -> {
                // Disk full or database corruption
                Log.e("DB_ERROR", "Database access failed: ${e.message}")
            }
            else -> {
                Log.e("GENERAL_ERROR", "An unknown error occurred: ${e.message}")
            }
        }
    }

    /*override suspend fun getAnime(id: Int) = dao.getAnimeById(id)*/
    override fun getAllAnime(): Flow<List<AnimeEntity>> {
        return dao.getAllAnime()
            .catch { e ->
                Log.e("DatabaseError", "Error reading anime list from Room", e)

                emit(emptyList())
            }
    }
}