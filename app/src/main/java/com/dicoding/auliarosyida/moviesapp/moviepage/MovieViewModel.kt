package com.dicoding.auliarosyida.moviesapp.moviepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.auliarosyida.moviesapp.core.domain.usecase.MovieUseCase

class MovieViewModel(movieUseCase: MovieUseCase) : ViewModel() {

    val movie = movieUseCase.getAllMovies().asLiveData()
}