package com.nafanya.tuturutest.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    private var api: Api

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kitsu.io/api/edge/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)
    }

    fun getApi(): Api {
        return api
    }

    companion object {
        private var instance: Client? = null

        fun getInstance(): Client {
            return if (instance == null) {
                Client()
            } else {
                instance!!
            }
        }
    }
}
