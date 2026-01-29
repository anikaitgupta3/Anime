package com.example.anime.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime_table")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(anime: List<AnimeEntity>)

    @Query("SELECT * FROM anime_table WHERE mal_id = :id")
    suspend fun getAnimeById(id: Int): AnimeEntity?
}