package com.dicoding.auliarosyida.moviesapp.core.data.source.localsource

import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.entity.MovieEntity
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.room.InterfaceMovieDao
import kotlinx.coroutines.flow.Flow

class LocalMovieDataSource(private val mMovieDao: InterfaceMovieDao) {

    fun getAllMovies(): Flow<List<MovieEntity>> = mMovieDao.getMovies()

    suspend fun insertMovies(movies: List<MovieEntity>) = mMovieDao.insertMovies(movies)

    fun getDetailMovie(movieId: String): Flow<MovieEntity> = mMovieDao.getMovieById(movieId)

    fun getFavoritedMovies(): Flow<List<MovieEntity>> = mMovieDao.getFavoritedMovies()

    suspend fun setFavoriteMovie(aMovie: MovieEntity, favState: Boolean) {
        aMovie.favorited = favState
        mMovieDao.updateMovie(aMovie)
    }

    suspend fun updateMovie(aMovie: MovieEntity) = mMovieDao.updateMovie(aMovie)
}