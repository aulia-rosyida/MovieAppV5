package com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response

import com.google.gson.annotations.SerializedName

data class ListMovieResponse(

    @field:SerializedName("results")
    val movies: List<MovieResponse>
)