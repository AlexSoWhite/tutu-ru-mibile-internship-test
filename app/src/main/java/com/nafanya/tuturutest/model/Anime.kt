package com.nafanya.tuturutest.model

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
    @SerializedName("attributes")
    @Expose
    val attributes: Attributes
)
