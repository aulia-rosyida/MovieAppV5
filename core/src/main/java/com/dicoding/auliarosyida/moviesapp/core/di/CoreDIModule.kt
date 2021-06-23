package com.dicoding.auliarosyida.moviesapp.core.di


import androidx.room.Room
import com.dicoding.auliarosyida.moviesapp.core.BuildConfig
import com.dicoding.auliarosyida.moviesapp.core.data.MovieRepository
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.LocalMovieDataSource
import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.room.MovieBuilderDatabase
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.RemoteMovieDataSource
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.network.ApiService
import com.dicoding.auliarosyida.moviesapp.core.domain.repository.InterfaceMovieRepository
import com.dicoding.auliarosyida.moviesapp.core.utils.AppThreadExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MovieBuilderDatabase>().movieDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieBuilderDatabase::class.java, "Movies.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalMovieDataSource(get()) }
    single { RemoteMovieDataSource(get()) }
    factory { AppThreadExecutors() }
    single<InterfaceMovieRepository> {
        MovieRepository(
            get(),
            get()
        )
    }
}