package com.example.anime.data.network.response

data class AnimeDto(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    val images: ImagesDto?,
    val trailer: TrailerDto?,
    val genres: List<GenreDto>?
)