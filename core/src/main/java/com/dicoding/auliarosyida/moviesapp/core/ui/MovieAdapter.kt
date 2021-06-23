package com.dicoding.auliarosyida.moviesapp.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.auliarosyida.moviesapp.core.BuildConfig
import com.dicoding.auliarosyida.moviesapp.core.databinding.ItemsMovieBinding
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.core.utils.loadFromUrl

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listMovie = ArrayList<Movie>()
    var onItemClick:((Movie) -> Unit)? = null

    fun setData(newListData: List<Movie>?) {
        if (newListData == null) return
        listMovie.clear()
        listMovie.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    inner class MovieViewHolder(private val binding: ItemsMovieBinding) : RecyclerView.ViewHolder(binding.root) {
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
        init{
            binding.root.setOnClickListener {
                onItemClick?.invoke(listMovie[bindingAdapterPosition])
            }
        }
    }

    override fun getItemCount(): Int= listMovie.size
}