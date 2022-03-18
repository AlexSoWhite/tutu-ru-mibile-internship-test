package com.nafanya.tuturutest.view

import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.AnimeItemDetailBinding
import com.nafanya.tuturutest.model.Anime

class AnimeDetailActivity : AppCompatActivity() {

    private lateinit var binding: AnimeItemDetailBinding
    private lateinit var anime: Anime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting displaying anime
        anime = Gson().fromJson(intent.getStringExtra("anime"), Anime::class.java)
        // setting support action bar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // inflate activity
        binding = DataBindingUtil.setContentView(this, R.layout.anime_item_detail)
        binding.anime = anime
        binding.userCount.text = getString(R.string.users, anime.attributes.userCount.toString())
        binding.episodes.text = getString(R.string.episodes, anime.attributes.episodeCount.toString())
        // animate text
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 400L
        alphaAnimation.startOffset = 300L
        binding.descriptionDetail.startAnimation(alphaAnimation)
        binding.info.startAnimation(alphaAnimation)
        binding.descriptionHeader.startAnimation(alphaAnimation)
        Glide.with(this)
            .load("https://media.kitsu.io/anime/poster_images/${anime.id}/large.jpg")
            .into(binding.image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}
