package com.nafanya.tuturutest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.AnimeItemDetailBinding
import com.nafanya.tuturutest.model.animeObjects.Anime

class AnimeDetailActivity : AppCompatActivity() {

    private lateinit var binding: AnimeItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anime = Gson().fromJson(intent.getStringExtra("anime"), Anime::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.anime_item_detail)
        binding.anime = anime
        binding.userCount.text = getString(R.string.users, anime.attributes.userCount.toString())
        binding.episodes.text = getString(R.string.episodes, anime.attributes.episodeCount.toString())
        Glide.with(this).load("https://media.kitsu.io/anime/poster_images/${anime.id}/large.jpg").into(binding.image)
    }
}
