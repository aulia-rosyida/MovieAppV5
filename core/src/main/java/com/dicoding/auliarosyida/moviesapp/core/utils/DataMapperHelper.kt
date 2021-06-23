package com.dicoding.auliarosyida.moviesapp.core.utils

import com.dicoding.auliarosyida.moviesapp.core.data.source.localsource.entity.MovieEntity
import com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response.MovieResponse
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie

object DataMapperHelper {
    fun mapResponsesToEntities(input: List<MovieResponse>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()

        input.map {
            val genreBuilder = StringBuilder()
            var tempQuote = it.title
            if (it.duration != null ) {
                tempQuote = it.quote
                it.genreIds.forEachIndexed { idx, g ->
                    if(idx == 0) genreBuilder.append("${g.name}")
                    else genreBuilder.append(", ${g.name}")
                }
            }

            val movie = MovieEntity(
                movieId = it.id,
                poster = it.poster,
                title = it.title,
                quote = tempQuote,
                overview = it.overview,
                releaseYear = it.releaseYear,
                genre = genreBuilder.toString(),
                duration = it.duration,
                status = it.status,
                originalLanguage = it.originalLanguage,
                favorited = false
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntitiesToDomain(input: List<MovieEntity>): List<Movie> {
        val movieDomain = ArrayList<Movie>()

        input.map {
            val movie = Movie(
                    id = it.movieId,
                    poster = it.poster,
                    title = it.title,
                    quote = it.quote,
                    overview = it.overview,
                    releaseYear = it.releaseYear,
                    genre = it.genre,
                    duration = it.duration,
                    status = it.status,
                    originalLanguage = it.originalLanguage,
                    favorited = it.favorited
            )
            movieDomain.add(movie)
        }

        return movieDomain
    }

    fun mapEntityToDomain(input: MovieEntity): Movie = Movie(
        id = input.movieId,
        poster = input.poster,
        title = input.title,
        quote = input.quote,
        overview = input.overview,
        releaseYear = input.releaseYear,
        genre = input.genre,
        duration = input.duration,
        status = input.status,
        originalLanguage = input.originalLanguage,
        favorited = input.favorited
    )

    fun mapDomainToEntity(input: Movie) = MovieEntity(
        movieId = input.id,
        poster = input.poster,
        title = input.title,
        quote = input.quote,
        overview = input.overview,
        releaseYear = input.releaseYear,
        genre = input.genre,
        duration = input.duration,
        status = input.status,
        originalLanguage = input.originalLanguage,
        favorited = input.favorited
    )
}