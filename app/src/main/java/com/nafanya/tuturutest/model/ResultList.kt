package com.nafanya.tuturutest.model

import com.google.gson.annotations.SerializedName
import com.nafanya.tuturutest.model.animeObjects.Anime

data class ResultList(
    @SerializedName("data")
    val data: ArrayList<Anime>? = null
)