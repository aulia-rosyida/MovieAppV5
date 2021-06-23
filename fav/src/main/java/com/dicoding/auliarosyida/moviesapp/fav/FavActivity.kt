package com.dicoding.auliarosyida.moviesapp.fav

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.moviesapp.core.ui.FavMovieAdapter
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.detailpage.DetailMovieActivity
import com.dicoding.auliarosyida.moviesapp.fav.databinding.ActivityFavBinding
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules


class FavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavBinding
    private lateinit var adapter: FavMovieAdapter
    private val viewModel: FavMovieViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.fav_title)

        loadKoinModules(favModelModule)

        adapter = FavMovieAdapter()
        adapter.onItemClick = { selectedData ->
            val intent = Intent(this@FavActivity, DetailMovieActivity::class.java)
            intent.putExtra(ConstHelper.EXTRA_MOVIE, selectedData)
            startActivity(intent)
        }
        binding.progressbarFavmovie.visibility = View.VISIBLE
        viewModel.favoriteMovie.observe(this, { movies ->
            binding.progressbarFavmovie.visibility = View.GONE
            adapter.setData(movies)
        })

        with(binding){
            rvFavMovie.layoutManager = LinearLayoutManager(applicationContext)
            rvFavMovie.setHasFixedSize(true)
            rvFavMovie.adapter = adapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}