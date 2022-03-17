package com.nafanya.tuturutest.model

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("https://kitsu.io/api/edge/anime")
    suspend fun getAll(@Query("page[offset]") offset: Int): ResultList

    @GET("https://kitsu.io/api/edge/anime")
    suspend fun search(@Query("filter[text]") query: String, @Query("page[offset]") offset: Int): ResultList
}
