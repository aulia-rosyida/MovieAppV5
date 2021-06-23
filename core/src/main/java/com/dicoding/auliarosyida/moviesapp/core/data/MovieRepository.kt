package com.dicoding.auliarosyida.moviesapp.core.data

import com.dicoding.auliarosyida.moviesapp.core.domain.repository.InterfaceMovieRepository
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.LocalMovieDataSource
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.RemoteMovieDataSource
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network.ApiResponse
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response.MovieResponse
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.utils.DataMapperHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

/**
 *  MovieRepository sebagai filter antara remote dan local
 *  agar apa yang ada di View tidak banyak berubah
 * */
class MovieRepository(
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val localMovieDataSource: LocalMovieDataSource
)  : InterfaceMovieRepository {

    override fun getAllMovies(): Flow<StatusData<List<Movie>>> {

        return object : NetworkBoundLocalRemoteResource<List<Movie>, List<MovieResponse>>() {

            override fun loadFromDB(): Flow<List<Movie>> {
                return localMovieDataSource.getAllMovies().map {
                    DataMapperHelper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                    data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> =
                    remoteMovieDataSource.getAllMovies()

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieEntities = DataMapperHelper.mapResponsesToEntities(data)
                localMovieDataSource.insertMovies(movieEntities)
            }
        }.asFlow()
    }

    override fun getDetailMovie(movieId: String): Flow<StatusData<Movie>> {

        return object :NetworkBoundLocalRemoteResource<Movie, MovieResponse>() {

            public override fun loadFromDB(): Flow<Movie> {
                return localMovieDataSource.getDetailMovie(movieId).map {
                    DataMapperHelper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data : Movie?): Boolean =
                    data?.duration == null

            override suspend fun createCall(): Flow<ApiResponse<MovieResponse>> =
                remoteMovieDataSource.getDetailMovie(movieId)

            override suspend fun saveCallResult(data: MovieResponse) {
                val tempArrayResponse = ArrayList<MovieResponse>()
                tempArrayResponse.add(data)
                val movieEntities = DataMapperHelper.mapResponsesToEntities(tempArrayResponse)
                localMovieDataSource.updateMovie(movieEntities[0])
            }

        }.asFlow()
    }

    override fun getFavoritesMovies(): Flow<List<Movie>> {
        return localMovieDataSource.getFavoritedMovies().map {
            DataMapperHelper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun setFavoriteMovie(movie: Movie, favState: Boolean) {
        val tourismEntity = DataMapperHelper.mapDomainToEntity(movie)
        localMovieDataSource.setFavoriteMovie(tourismEntity, favState) 
    }
}