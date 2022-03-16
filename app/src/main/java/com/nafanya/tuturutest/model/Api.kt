package com.nafanya.tuturutest.model

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("https://kitsu.io/api/edge/anime")
    fun getAll(): Call<ResultList>
}
