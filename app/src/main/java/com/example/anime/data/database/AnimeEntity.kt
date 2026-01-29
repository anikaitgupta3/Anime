package com.example.anime.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    val imageUrl: String,
    val trailerUrl: String?,
    val genres: String?,
    val cast: String? = null // New Field
)