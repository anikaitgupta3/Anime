package com.example.anime.data

import android.content.Context
import com.example.anime.data.database.AnimeDatabase
import com.example.anime.data.network.JikanApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val animeRepository: AnimeRepository
}
class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl =
        "https://api.jikan.moe/v4/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    //private val retroJson = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        //.addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: JikanApi by lazy {
        retrofit.create(JikanApi::class.java)
    }

    override val animeRepository: AnimeRepository by lazy {
        AnimeRepositoryImpl(retrofitService, AnimeDatabase.getDatabase(context).animeDao())
    }
}