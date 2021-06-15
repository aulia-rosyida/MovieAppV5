package com.dicoding.auliarosyida.moviesapp.model

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.auliarosyida.moviesapp.model.source.InterfaceMovieDataSource
import com.dicoding.auliarosyida.moviesapp.model.source.localsource.LocalMovieDataSource
import com.dicoding.auliarosyida.moviesapp.model.source.localsource.entity.MovieEntity
import com.dicoding.auliarosyida.moviesapp.model.source.remotesource.network.NetworkApiResponse
import com.dicoding.auliarosyida.moviesapp.model.source.remotesource.RemoteMovieDataSource
import com.dicoding.auliarosyida.moviesapp.model.source.remotesource.network.ApiResponse
import com.dicoding.auliarosyida.moviesapp.model.source.remotesource.response.MovieResponse
import com.dicoding.auliarosyida.moviesapp.utils.AppThreadExecutors
import com.dicoding.auliarosyida.moviesapp.valueobject.ResourceWrapData

/**
 *  MovieRepository sebagai filter antara remote dan local
 *  agar apa yang ada di View tidak banyak berubah
 * */
class MovieRepository private constructor(private val remoteMovieDataSource: RemoteMovieDataSource,
                                          private val localMovieDataSource: LocalMovieDataSource,
                                          private val appThreadExecutors: AppThreadExecutors
)  : InterfaceMovieDataSource {

    companion object {
        @Volatile
//        @JvmStatic
        private var instance: MovieRepository? = null

        // filter antara remote dan local
        fun getInstance(remoteData: RemoteMovieDataSource, localData: LocalMovieDataSource, appExecutors: AppThreadExecutors): MovieRepository =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(remoteData, localData, appExecutors).apply { instance = this }
            }
    }

    override fun getAllMovies(): LiveData<ResourceWrapData<PagedList<MovieEntity>>> {

        return object : NetworkBoundLocalRemoteResource<PagedList<MovieEntity>, List<MovieResponse>>(appThreadExecutors) {

            public override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localMovieDataSource.getAllMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean =
                    data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                    remoteMovieDataSource.getAllMovies()

            public override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<MovieEntity>()
                for (response in data) {
                    val movie = MovieEntity(response.id,
                            response.poster,
                            response.title,
                            response.title,
                            response.overview,
                            response.releaseYear,
                            "",
                            response.duration,
                            response.status,
                            response.originalLanguage)
                    movieList.add(movie)
                }

                localMovieDataSource.insertMovies(movieList)
            }
        }.asLiveData()
    }

    override fun getDetailMovie(movieId: String): LiveData<ResourceWrapData<MovieEntity>> {

        return object : NetworkBoundLocalRemoteResource<MovieEntity, MovieResponse>(appThreadExecutors) {

            public override fun loadFromDB(): LiveData<MovieEntity> =
                localMovieDataSource.getDetailMovie(movieId)

            override fun shouldFetch(data : MovieEntity?): Boolean =
                data == null

            public override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                remoteMovieDataSource.getDetailMovie(movieId)

            override fun saveCallResult(data: MovieResponse) {
                val genreBuilder = StringBuilder()
//                data.genreIds?.forEach { g -> genreBuilder.append(g) }

                val movie = MovieEntity(data.id,
                        data.poster,
                        data.title,
                        data.quote,
                        data.overview,
                        data.releaseYear,
                        genreBuilder.toString(),
                        data.duration,
                        data.status,
                        data.originalLanguage)
                localMovieDataSource.updateMovie(movie)
            }

        }.asLiveData()
    }

    override fun getFavoritesMovies(): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localMovieDataSource.getFavoritedMovies(), config).build()
    }

    override fun setFavoriteMovie(movie: MovieEntity, favState: Boolean) {
        appThreadExecutors.diskIO().execute { localMovieDataSource.setFavoriteMovie(movie, favState) }
    }
}