package com.nafanya.tuturutest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.nafanya.tuturutest.model.Anime
import com.nafanya.tuturutest.model.Repository
import kotlinx.coroutines.flow.Flow

enum class PageState {
    IS_FIRST_LOADING,
    IS_LOADING,
    IS_LOADED,
    IS_EMPTY,
    IS_ERROR
}

class MainActivityViewModel : ViewModel() {

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>(PageState.IS_FIRST_LOADING)
    }

    private lateinit var lastQuery: String

    private lateinit var repository: Repository

    fun setRepo() {
        repository = Repository()
    }

    fun letAnimeFlow(searchText: String): Flow<PagingData<Anime>> {
        if (!this::lastQuery.isInitialized || searchText != lastQuery) {
            pageState.value = PageState.IS_LOADING
        }
        lastQuery = searchText
        return repository.letAnimeFlow(searchText, pageState)
    }

    fun execLastQuery(callback: () -> Unit) {
        letAnimeFlow(lastQuery)
        callback()
    }

}
