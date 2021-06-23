package com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.room

import androidx.room.*
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterfaceMovieDao {

    @Query("SELECT * FROM movieentities")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movieentities where favorited = 1")
    fun getFavoritedMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM movieentities WHERE id = :movieId")
    fun getMovieById(movieId: String): Flow<MovieEntity>

}