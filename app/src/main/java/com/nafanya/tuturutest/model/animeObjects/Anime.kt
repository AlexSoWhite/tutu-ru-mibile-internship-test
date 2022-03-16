package com.nafanya.tuturutest.model.animeObjects

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("synopsis")
    @Expose
    val synopsis: String,
    @SerializedName("canonicalTitle")
    @Expose
    val canonicalTitle: String,
    @SerializedName("averageRating")
    @Expose
    val averageRating: String,
    @SerializedName("userCount")
    @Expose
    val userCount: Int,
    @SerializedName("ageRatingGuide")
    @Expose
    val ageRatingGuide: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("episodeCount")
    @Expose
    val episodeCount: Int
)

data class Anime(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("attributes")
    @Expose
    val attributes: Attributes,
    @SerializedName("favoritesCount")
    @Expose
    val favoritesCount: Int,
    @SerializedName("popularityRank")
    @Expose
    val popularityRank: Int,
    @SerializedName("episodeLength")
    @Expose
    val episodeLength: Int,
    @SerializedName("totalLength")
    @Expose
    val totalLength: Int
)
