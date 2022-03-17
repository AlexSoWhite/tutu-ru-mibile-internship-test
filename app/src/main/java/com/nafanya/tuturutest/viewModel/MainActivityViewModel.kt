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

    private var lastQuery: String = ""

    fun search(query: String, callback: (() -> Unit)? = null) {
        pageState.value = PageState.IS_LOADING
        execCall(makeCall(query), callback)
    }

    private fun makeCall(query: String) : Call<ResultList> {
        lastQuery = query
        return if (query.isNotEmpty()) {
            Client.getInstance().getApi().search(query)
        } else {
            Client.getInstance().getApi().getAll()
        }
    }

    private fun execCall(call: Call<ResultList>, callback: (() -> Unit)? = null) {
        call.enqueue(object : Callback<ResultList> {
            override fun onResponse(call: Call<ResultList>, response: Response<ResultList>) {
                val body = response.body()?.data
                list.value = body
                if (body!!.isNotEmpty()) {
                    pageState.value = PageState.IS_LOADED
                } else {
                    pageState.value = PageState.IS_EMPTY
                }
                if (callback != null) callback()
            }

            override fun onFailure(call: Call<ResultList>, t: Throwable) {
                list.value = listOf()
                pageState.value = PageState.IS_ERROR
                if (callback != null) callback()
            }
        })
    }

    fun execLastQuery(callback: () -> Unit) {
        execCall(makeCall(lastQuery), callback)
    }
}
