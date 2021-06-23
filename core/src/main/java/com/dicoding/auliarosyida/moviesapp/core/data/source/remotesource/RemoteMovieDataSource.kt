package com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource

import android.util.Log
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network.ApiResponse
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network.ApiService
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteMovieDataSource(private val apiService: ApiService) {

    suspend fun getAllMovies() : Flow<ApiResponse<List<MovieResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getList()
                val dataArray =  response.movies
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.movies))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailMovie(movieId: String): Flow<ApiResponse<MovieResponse>> {

        //get data from remote api
        return flow {
            try {
                val response = apiService.getDetail(movieId)
                emit(ApiResponse.Success(response))
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}


