package com.example.anime

import android.app.Application
import com.example.anime.data.AppContainer
import com.example.anime.data.DefaultAppContainer

class AnimeApp : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}