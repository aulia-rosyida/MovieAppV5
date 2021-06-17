package com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.entity.MovieEntity

@Database(entities = [MovieEntity::class],
    version = 1,
    exportSchema = false)
abstract class MovieBuilderDatabase : RoomDatabase(){

    abstract fun movieDao(): InterfaceMovieDao
}