package com.nafanya.tuturutest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.AnimeListItemBinding
import com.nafanya.tuturutest.model.Anime

class AnimeListAdapter(
    private val callback: (Anime, AnimeListItemBinding) -> Unit
) : PagingDataAdapter<Anime, AnimeListAdapter.AnimeViewHolder>(AnimeComparator) {

    companion object {
        private val AnimeComparator = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_list_item, parent, false)
        return AnimeViewHolder(itemView, callback)
    }


    class AnimeViewHolder(
        itemView: View,
        private val callback: (Anime, AnimeListItemBinding) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private var binding = AnimeListItemBinding.bind(itemView)

        fun bind(anime: Anime?) {
            anime?.let {
                binding.anime = anime
                if (anime.attributes.averageRating != null) {
                    binding.rating.text = itemView.context.getString(R.string.rating, anime.attributes.averageRating)
                } else {
                    binding.rating.visibility = View.GONE
                }
                Glide.with(itemView)
                    .load("https://media.kitsu.io/anime/poster_images/${anime.id}/large.jpg")
                    .placeholder(R.drawable.default_placeholder)
                    .into(binding.image)
                binding.item.setOnClickListener {
                    callback(anime, binding)
                }
            }
        }
    }
}
