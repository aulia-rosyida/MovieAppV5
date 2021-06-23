package com.dicoding.auliarosyida.moviesapp.detailpage

import androidx.lifecycle.*
import com.dicoding.auliarosyida.moviesapp.core.data.StatusData
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.runBlocking

class DetailMovieViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    private val detailId = MutableLiveData<String>()

    fun setSelectedDetail(detailId: String) {
        this.detailId.value = detailId
    }

    var detailMovie: LiveData<StatusData<Movie>> = Transformations.switchMap(detailId) { mDetailId ->
        detailId.value.let { movieUseCase.getDetailMovie(mDetailId).asLiveData() }
    }

    fun setFavoriteMovie() {
        val movieResource = detailMovie.value
        if (movieResource != null) {
            val movieDomain = movieResource.data
            if (movieDomain != null) {
                val newState = !movieDomain.favorited
                runBlocking{
                    movieUseCase.setFavoriteMovie(movieDomain, newState)
                }
            }
        }
    }
}