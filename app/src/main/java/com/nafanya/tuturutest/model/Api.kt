package com.nafanya.tuturutest.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("https://kitsu.io/api/edge/anime")
    fun getAll(): Call<ResultList>

    @GET("https://kitsu.io/api/edge/anime")
    fun search(@Query("filter[text]=") query: String): Call<ResultList>
}
