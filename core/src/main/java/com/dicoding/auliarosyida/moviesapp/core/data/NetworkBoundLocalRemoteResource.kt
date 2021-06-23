package com.dicoding.auliarosyida.moviesapp.core.data

import kotlinx.coroutines.flow.*
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network.ApiResponse

abstract class NetworkBoundLocalRemoteResource <ResultType, RequestType> {

    private var result: Flow<StatusData<ResultType>> = flow {
        emit(StatusData.Loading())
        val sourceDb = loadFromDB().first()
        if (shouldFetch(sourceDb)) {
            emit(StatusData.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { StatusData.Success(it) })
                }
                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { StatusData.Success(it) })
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emitAll(loadFromDB().map { StatusData.Error(it, apiResponse.errorMessage) })
                }
            }
        } else {
            emitAll(loadFromDB().map { StatusData.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend  fun saveCallResult(data: RequestType)

    fun asFlow() = result
}