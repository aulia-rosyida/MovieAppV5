package com.dicoding.auliarosyida.moviesapp.core.ui.detailpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.auliarosyida.moviesapp.BuildConfig
import com.dicoding.auliarosyida.moviesapp.R
import com.dicoding.auliarosyida.moviesapp.databinding.ActivityDetailBinding
import com.dicoding.auliarosyida.moviesapp.databinding.ContentDetailMovieBinding
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.core.utils.loadFromUrl
import com.dicoding.auliarosyida.moviesapp.valueobject.IndicatorStatus
import com.dicoding.auliarosyida.moviesapp.viewmodel.VMAppFactory

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var activityDetailMovieBinding: ActivityDetailBinding
    private lateinit var detailContentBinding: ContentDetailMovieBinding
    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private var menu: Menu? = null

    companion object {
        const val extraMovie = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailMovieBinding = ActivityDetailBinding.inflate(layoutInflater)
        detailContentBinding = activityDetailMovieBinding.detailContent

        setContentView(activityDetailMovieBinding.root)

        setSupportActionBar(activityDetailMovieBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailFactory = VMAppFactory.getInstance(this)
        detailMovieViewModel = ViewModelProvider(this, detailFactory)[DetailMovieViewModel::class.java]

        val extras = intent.extras
        if (extras != null) {

            val tempId = extras.getString(extraMovie)
            if (tempId != null ) {

                detailMovieViewModel.detailMovie.observe(this, { detailDomain ->

                    if (detailDomain != null) {
                        detailContentBinding.apply{
                            when (detailDomain.status) {
                                IndicatorStatus.LOADING -> progressbarDetailContent.visibility = View.VISIBLE
                                IndicatorStatus.SUCCESS ->
                                    if (detailDomain.data != null) {
                                        progressbarDetailContent.visibility = View.VISIBLE
                                        populateCard(detailDomain.data)
                                    }
                                IndicatorStatus.ERROR -> {
                                    progressbarDetailContent.visibility = View.GONE
                                    Toast.makeText(applicationContext, getString(R.string.error_occured), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    private fun populateCard(entity: Movie) {

        entity.poster?.let {
            detailContentBinding.imagePoster.loadFromUrl(BuildConfig.TMDB_URL_IMAGE + ConstHelper.SIZE_POSTER + it)
        }

        detailContentBinding.apply {
            progressbarDetailContent.visibility = View.GONE
            textYear.text = entity.releaseYear?.subSequence(0,4)
            textDuration.text = "${entity.duration}m"
            textTitle.text = entity.title
            textGenre.text = entity.genre

            textQuote.text = entity.quote
            textOverview.text = entity.overview
            textStatus.text = entity.status
            textLang.text = entity.originalLanguage
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        detailMovieViewModel.detailMovie.observe(this, { detailMovie ->
            if (detailMovie != null) {
                detailContentBinding.apply{
                    when (detailMovie.status) {
                        IndicatorStatus.LOADING -> progressbarDetailContent.visibility = View.VISIBLE
                        IndicatorStatus.SUCCESS -> if (detailMovie.data != null) {
                            progressbarDetailContent.visibility = View.GONE
                            val state = detailMovie.data.favorited
                            setFavoriteState(detailMovie.data, state)
                        }
                        IndicatorStatus.ERROR -> {
                            progressbarDetailContent.visibility = View.GONE
                            Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
        return true
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.action_favorite) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
    private fun setFavoriteState(movie: Movie, state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_favorite)
        detailMovieViewModel.setFavoriteMovie(movie, state)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_full_favorite_24)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24)
        }
    }
}