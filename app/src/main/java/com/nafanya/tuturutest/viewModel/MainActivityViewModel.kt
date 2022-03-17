package com.nafanya.tuturutest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafanya.tuturutest.model.animeObjects.Anime
import com.nafanya.tuturutest.model.Client
import com.nafanya.tuturutest.model.ResultList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class PageState {
    IS_FIRST_LOADING,
    IS_LOADING,
    IS_LOADED,
    IS_EMPTY,
    IS_ERROR
}

class MainActivityViewModel : ViewModel() {

    val list: MutableLiveData<List<Anime>> by lazy {
        MutableLiveData<List<Anime>>()
    }

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>(PageState.IS_FIRST_LOADING)
    }

    fun getAll() {
        pageState.value = PageState.IS_LOADING
        val call = Client.getInstance().getApi().getAll()
        call.enqueue(object : Callback<ResultList> {
            override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                val body = response.body()?.data
                list.value = body
                if (body!!.isNotEmpty()) {
                    pageState.value = PageState.IS_LOADED
                } else {
                    pageState.value = PageState.IS_EMPTY
                }
            }

            override fun onFailure(call: Call<ResultList>, t: Throwable) {
                pageState.value = PageState.IS_ERROR
            }
        })
    }

    fun search(query: String) {
        pageState.value = PageState.IS_LOADING
        val call = Client.getInstance().getApi().search(query)
        call.enqueue(object : Callback<ResultList> {
            override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                val body = response.body()?.data
                list.value = body
                if (body!!.isNotEmpty()) {
                    pageState.value = PageState.IS_LOADED
                } else {
                    pageState.value = PageState.IS_EMPTY
                }
            }

            override fun onFailure(call: Call<ResultList>, t: Throwable) {
                pageState.value = PageState.IS_ERROR
            }
        })
    }
}
