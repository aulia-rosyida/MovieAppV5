package com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network

import com.dicoding.auliarosyida.moviesapp.core.BuildConfig
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response.ListMovieResponse
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun getList(
            @Query("sort_by") sortBy: String = "popularity.desc",
            @Query("api_key") apiKey: String = BuildConfig.API_TOKEN
    ): ListMovieResponse

    @GET("movie/{id}")
    suspend fun getDetail(
        @Path("id") id: String,
        @Query("api_key") apiKey: String = BuildConfig.API_TOKEN
    ): MovieResponse
}