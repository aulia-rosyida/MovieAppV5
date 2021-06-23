@file:Suppress("unused")

package com.dicoding.auliarosyida.moviesapp

import android.app.Application
import com.dicoding.auliarosyida.moviesapp.core.di.databaseModule
import com.dicoding.auliarosyida.moviesapp.core.di.networkModule
import com.dicoding.auliarosyida.moviesapp.core.di.repositoryModule
import com.dicoding.auliarosyida.moviesapp.di.useCaseModule
import com.dicoding.auliarosyida.moviesapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyMovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyMovieApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}