package com.dicoding.auliarosyida.moviesapp.core.data

sealed class StatusData<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : StatusData<T>(data)
    class Loading<T> : StatusData<T>()
    class Error<T>(data: T, error: String) : StatusData<T>(data, error)
}