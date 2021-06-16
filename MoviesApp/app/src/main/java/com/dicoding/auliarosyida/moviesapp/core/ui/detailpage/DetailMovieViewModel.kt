package com.dicoding.auliarosyida.moviesapp.core.ui.detailpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dicoding.auliarosyida.moviesapp.core.data.MovieRepository
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.domain.usecase.MovieUseCase
import com.dicoding.auliarosyida.moviesapp.valueobject.ResourceWrapData

class DetailMovieViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    private val detailId = MutableLiveData<String>()

    fun setSelectedDetail(detailId: String) {
        this.detailId.value = detailId
    }

    var detailMovie: LiveData<ResourceWrapData<Movie>> = Transformations.switchMap(detailId) { mDetailId ->
        detailId.value?.let { movieUseCase.getDetailMovie(mDetailId) }
    }

    fun setFavoriteMovie() {
        val movieResource = detailMovie.value
        if (movieResource != null) {
            val movieDomain = movieResource.data
            if (movieDomain != null) {
                val newState = !movieDomain.favorited
                movieUseCase.setFavoriteMovie(movieDomain, newState)
            }
        }
    }
}