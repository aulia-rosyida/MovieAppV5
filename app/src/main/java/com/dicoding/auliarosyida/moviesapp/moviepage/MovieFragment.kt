package com.dicoding.auliarosyida.moviesapp.moviepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.moviesapp.R
import com.dicoding.auliarosyida.moviesapp.core.data.StatusData
import com.dicoding.auliarosyida.moviesapp.core.ui.MovieAdapter
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.databinding.MovieFragmentBinding
import com.dicoding.auliarosyida.moviesapp.detailpage.DetailMovieActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MovieFragment : Fragment() {

    private lateinit var movieFragmentBinding: MovieFragmentBinding
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieFragmentBinding = MovieFragmentBinding.inflate(layoutInflater, container, false)
        return movieFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            val movieAdapter = MovieAdapter()
            movieAdapter.onItemClick = { selectedData ->
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(ConstHelper.EXTRA_MOVIE, selectedData)
                startActivity(intent)
            }
            movieFragmentBinding.progressbarMovie.visibility = View.VISIBLE
            movieViewModel.movie.observe(this, { movies ->
                if (movies != null) {
                    when (movies) {
                        is StatusData.Loading -> movieFragmentBinding.progressbarMovie.visibility = View.VISIBLE
                        is StatusData.Success -> {
                            movieFragmentBinding.progressbarMovie.visibility = View.GONE
                            movieAdapter.setData(movies.data)
                        }
                        is StatusData.Error -> {
                            movieFragmentBinding.progressbarMovie.visibility = View.GONE
                            Toast.makeText(context, getString(R.string.failed_occured), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(movieFragmentBinding.rvMovie){
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
    }

}