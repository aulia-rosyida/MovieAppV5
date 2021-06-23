package com.dicoding.auliarosyida.moviesapp.core.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.auliarosyida.moviesapp.core.R

fun ImageView.loadFromUrl(path: String) {
    Glide.with(this).clear(this)
    Glide.with(this)
        .setDefaultRequestOptions(RequestOptions())
        .load(path)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
            .error(R.drawable.ic_error))
        .into(this)
}