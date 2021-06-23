package com.dicoding.auliarosyida.moviesapp.detailpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.auliarosyida.moviesapp.BuildConfig
import com.dicoding.auliarosyida.moviesapp.R
import com.dicoding.auliarosyida.moviesapp.core.data.StatusData
import com.dicoding.auliarosyida.moviesapp.databinding.ActivityDetailBinding
import com.dicoding.auliarosyida.moviesapp.databinding.ContentDetailMovieBinding
import com.dicoding.auliarosyida.moviesapp.core.domain.model.Movie
import com.dicoding.auliarosyida.moviesapp.core.utils.ConstHelper
import com.dicoding.auliarosyida.moviesapp.core.utils.loadFromUrl
import org.koin.android.viewmodel.ext.android.viewModel

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var activityDetailMovieBinding: ActivityDetailBinding
    private lateinit var detailContentBinding: ContentDetailMovieBinding
    private val detailMovieViewModel: DetailMovieViewModel by viewModel()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailMovieBinding = ActivityDetailBinding.inflate(layoutInflater)
        detailContentBinding = activityDetailMovieBinding.detailContent

        setContentView(activityDetailMovieBinding.root)

        setSupportActionBar(activityDetailMovieBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailMovie = intent.getParcelableExtra<Movie>(ConstHelper.EXTRA_MOVIE)
        if (detailMovie != null) {
            detailMovieViewModel.setSelectedDetail(detailMovie.id)
            setFavoriteState(detailMovie.favorited)

            detailMovieViewModel.detailMovie.observe(this, { detailDomain ->

                if (detailDomain != null) {
                    detailContentBinding.apply{
                        when (detailDomain) {
                            is StatusData.Loading -> progressbarDetailContent.visibility = View.VISIBLE
                            is StatusData.Success ->
                                if (detailDomain.data != null) {
                                    progressbarDetailContent.visibility = View.VISIBLE
                                    populateCard(detailDomain.data!!)
                                }
                            is StatusData.Error -> {
                                progressbarDetailContent.visibility = View.GONE
                                Toast.makeText(applicationContext, getString(R.string.error_occured), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
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
                    when (detailMovie) {
                        is StatusData.Loading -> progressbarDetailContent.visibility = View.VISIBLE
                        is StatusData.Success -> if (detailMovie.data != null) {
                            progressbarDetailContent.visibility = View.GONE
                            val state = detailMovie.data!!.favorited
                            setFavoriteState(state)
                        }
                        is StatusData.Error -> {
                            progressbarDetailContent.visibility = View.GONE
                            Toast.makeText(applicationContext, getString(R.string.failed_occured), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            detailMovieViewModel.setFavoriteMovie()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setFavoriteState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_favorite)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_full_favorite_24)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24)
        }
    }
}