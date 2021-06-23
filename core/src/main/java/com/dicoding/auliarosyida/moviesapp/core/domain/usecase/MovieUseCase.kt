package com.dicoding.auliarosyida.moviesapp.core.domain.usecase

import com.dicoding.auliarosyida.moviesapp.core.data.StatusData
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getAllMovies(): Flow<StatusData<List<Movie>>>
    fun getDetailMovie(movieId: String): Flow<StatusData<Movie>>
    fun getFavoritesMovies(): Flow<List<Movie>>
    suspend fun setFavoriteMovie(movie: Movie, state: Boolean)
}