package com.dicoding.auliarosyida.moviesapp.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.auliarosyida.moviesapp.core.BuildConfig
import com.dicoding.auliarosyida.moviesapp.core.databinding.ItemsMovieBinding
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.core.utils.loadFromUrl

class FavMovieAdapter : RecyclerView.Adapter<FavMovieAdapter.FavMovieViewHolder>() {

    private var listData = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setData(newListData: List<Movie>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FavMovieViewHolder {
        val itemsFavMoviesBinding = ItemsMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavMovieViewHolder(itemsFavMoviesBinding)
    }

    override fun onBindViewHolder(holder: FavMovieViewHolder, position: Int) {
        val movie = listData[position]
        holder.bind(movie)
    }

    inner class FavMovieViewHolder(private val binding: ItemsMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                movie.poster?.let {
                    imgPoster.loadFromUrl(BuildConfig.TMDB_URL_IMAGE + ConstHelper.SIZE_POSTER + it)
                }

                tvItemTitle.text = movie.title
                tvItemReleaseYear.text = movie.releaseYear?.subSequence(0,4)
                tvItemQuotes.text = movie.quote
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[absoluteAdapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = listData.size
}