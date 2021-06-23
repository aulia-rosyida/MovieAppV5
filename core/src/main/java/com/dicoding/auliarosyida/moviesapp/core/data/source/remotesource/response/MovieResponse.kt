package com.dicoding.auliarosyida.moviesapp.core.data.source.remotesource.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("poster_path")
    var poster: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("tagline")
    var quote: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("release_date")
    var releaseYear: String = "",
    @SerializedName("genres")
    var genreIds: ArrayList<Genre>,
    @SerializedName("runtime")
    var duration: String = "0m",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("original_language")
    var originalLanguage: String = ""
)

data class Genre (
    @SerializedName("id")
    var id: String = "",

    @SerializedName("name")
    var name: String? = ""
)
