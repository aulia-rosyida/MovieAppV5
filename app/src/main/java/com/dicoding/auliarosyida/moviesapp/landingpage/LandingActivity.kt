package com.dicoding.auliarosyida.moviesapp.landingpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.auliarosyida.moviesapp.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityLandingBinding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(activityLandingBinding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        activityLandingBinding.viewPager.adapter = sectionsPagerAdapter
        activityLandingBinding.tabs.setupWithViewPager(activityLandingBinding.viewPager)

        supportActionBar?.elevation = 0f

        activityLandingBinding.fab.setOnClickListener {
            try {
                moveToChatActivity()
            } catch (e: Exception){
                Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToChatActivity() {
        startActivity(Intent(this, Class.forName("com.dicoding.auliarosyida.moviesapp.fav.FavActivity")))
    }
}