package com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movieentities")
data class MovieEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var movieId: String,

    @ColumnInfo(name = "poster_path")
    var poster: String?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "tagline")
    var quote: String?,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "release_date")
    var releaseYear: String?,

    @ColumnInfo(name = "genres")
    var genre: String?,

    @ColumnInfo(name = "runtime")
    var duration: String?,

    @ColumnInfo(name = "status")
    var status: String?,

    @ColumnInfo(name = "original_language")
    var originalLanguage: String?,

    @ColumnInfo(name = "favorited")
    var favorited: Boolean = false
)
