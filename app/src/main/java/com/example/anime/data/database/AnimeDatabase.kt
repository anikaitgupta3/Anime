package com.example.anime.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnimeEntity::class], version = 2, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        @Volatile
        private var Instance: AnimeDatabase? = null

        fun getDatabase(context: Context): AnimeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AnimeDatabase::class.java, "anime_database")
                    .fallbackToDestructiveMigration(true)
                    .build()

                    .also { Instance = it }
            }

        }
    }
}