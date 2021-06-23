package com.dicoding.auliarosyida.moviesapp.fav

import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel

val favModelModule = module {
    viewModel { FavMovieViewModel(get()) }
}