package com.nafanya.tuturutest.model

import com.google.gson.annotations.SerializedName

data class ResultList(
    @SerializedName("data")
    val data: ArrayList<Anime>? = null
)