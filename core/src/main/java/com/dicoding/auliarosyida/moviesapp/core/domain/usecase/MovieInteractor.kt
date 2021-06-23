package com.dicoding.auliarosyida.moviesapp.core.domain.usecase

import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.domain.repository.InterfaceMovieRepository

class MovieInteractor(private val movieRepository: InterfaceMovieRepository): MovieUseCase {

    override fun getAllMovies() = movieRepository.getAllMovies()

    override fun getDetailMovie(movieId: String) = movieRepository.getDetailMovie(movieId)

    override fun getFavoritesMovies() = movieRepository.getFavoritesMovies()

    override suspend fun setFavoriteMovie(movie: Movie, state: Boolean) = movieRepository.setFavoriteMovie(movie, state)
}