package com.example.anime.data.network

import com.example.anime.data.network.response.CharactersResponse
import com.example.anime.data.network.response.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApi {
    @GET("top/anime")
    suspend fun getTopAnime(): TopAnimeResponse

    //@GET("anime/{id}")
    //suspend fun getDetails(@Path("id") id: Int): TopAnimeResponse
    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") id: Int): CharactersResponse
}