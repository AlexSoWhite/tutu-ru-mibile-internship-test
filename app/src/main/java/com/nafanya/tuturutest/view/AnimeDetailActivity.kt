package com.nafanya.tuturutest.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
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
        inflateText(binding.title, R.string.title, anime.attributes.canonicalTitle)
        inflateText(binding.rating, R.string.rating, anime.attributes.averageRating)
        inflateText(binding.ageRating, R.string.age_rating, anime.attributes.ageRatingGuide, )
        inflateText(binding.userCount, R.string.users, anime.attributes.userCount.toString())
        inflateText(binding.status, R.string.status, anime.attributes.status)
        inflateText(binding.episodes, R.string.episodes, anime.attributes.episodeCount.toString())
        // animate text
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 400L
        alphaAnimation.startOffset = 300L
        binding.descriptionDetail.startAnimation(alphaAnimation)
        binding.info.startAnimation(alphaAnimation)
        binding.descriptionHeader.startAnimation(alphaAnimation)
        Glide.with(this)
            .load("https://media.kitsu.io/anime/poster_images/${anime.id}/large.jpg")
            .placeholder(R.drawable.default_placeholder)
            .into(binding.image)
    }

    private fun inflateText(
        view: TextView,
        stringResource: Int,
        text: String?
    ) {
        if (text != null) {
            view.text = getString(stringResource, text)
        } else {
            view.visibility = View.GONE
        }
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
