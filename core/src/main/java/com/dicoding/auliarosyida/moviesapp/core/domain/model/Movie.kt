package com.dicoding.auliarosyida.moviesapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
        var id: String = "",
        var poster: String? = "",
        var title: String? = "",
        var quote: String? = "",
        var overview: String? = "",
        var releaseYear: String? = "",
        var genre: String? = "",
        var duration: String? = "0m",
        var status: String? = "",
        var originalLanguage: String? = "",
        var favorited: Boolean
) : Parcelable