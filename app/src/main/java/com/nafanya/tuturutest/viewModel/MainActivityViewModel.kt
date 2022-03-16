package com.nafanya.tuturutest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafanya.tuturutest.model.animeObjects.Anime
import com.nafanya.tuturutest.model.Client
import com.nafanya.tuturutest.model.ResultList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    val list: MutableLiveData<List<Anime>> by lazy {
        MutableLiveData<List<Anime>>()
    }

    fun getAll() {
        val call = Client.getInstance().getApi().getAll()
        call.enqueue(object : Callback<ResultList> {
            override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                val body = response.body()?.data
                list.value = body
            }

            override fun onFailure(call: Call<ResultList>, t: Throwable) {
                throw t
            }

        })
    }
}
