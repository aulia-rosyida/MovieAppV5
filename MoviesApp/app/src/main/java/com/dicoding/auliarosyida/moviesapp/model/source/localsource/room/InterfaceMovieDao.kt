package com.dicoding.auliarosyida.moviesapp.model.source.localsource.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.auliarosyida.moviesapp.model.source.localsource.entity.MovieEntity
import com.dicoding.auliarosyida.moviesapp.model.source.remotesource.response.MovieResponse

@Dao
interface InterfaceMovieDao {

    @Query("SELECT * FROM movieentities")
    fun getMovies(): LiveData<List<MovieResponse>>

    @Query("SELECT * FROM movieentities where favorited = 1")
    fun getFavoritedMovies(): LiveData<List<MovieResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(courses: List<MovieResponse>)

    @Update
    fun updateMovie(course: MovieEntity)

    @Query("SELECT * FROM tvshowentities")
    fun getTvShows(): LiveData<List<MovieResponse>>

    @Query("SELECT * FROM tvshowentities where favorited = 1")
    fun getFavoritedTvShows(): LiveData<List<MovieResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShows(courses: List<MovieResponse>)

    @Update
    fun updateTvShow(course: MovieEntity)


}