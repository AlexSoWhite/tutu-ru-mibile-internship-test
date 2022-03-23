package com.nafanya.tuturutest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.nafanya.tuturutest.model.Anime
import com.nafanya.tuturutest.model.LocalStorageProvider
import com.nafanya.tuturutest.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

enum class PageState {
    IS_FIRST_LOADING,
    IS_LOADING,
    IS_LOADING_MORE,
    IS_LOADED,
    IS_EMPTY,
    IS_ERROR
}

class MainActivityViewModel(
    application: Application
) : AndroidViewModel(application) {

    val pageState: MutableLiveData<PageState> by lazy {
        MutableLiveData<PageState>(PageState.IS_FIRST_LOADING)
    }

    private lateinit var lastQuery: String

    private lateinit var repository: Repository
    private lateinit var localStorageProvider: LocalStorageProvider

    fun setRepo() {
        repository = Repository()
        localStorageProvider = LocalStorageProvider(getApplication())
    }

    fun letAnimeFlow(searchText: String): Flow<PagingData<Anime>> {
        if (!this::lastQuery.isInitialized || searchText != lastQuery) {
            pageState.value = PageState.IS_LOADING
        }
        lastQuery = searchText
        return repository.letAnimeFlow(searchText, pageState)
    }

    fun putToCache(list: List<Anime>) {
        viewModelScope.launch {
            localStorageProvider.put(list)
        }
    }

    fun loadFromCache(callback: (List<Anime>) -> Unit) {
        viewModelScope.launch {
            val list = localStorageProvider.get()
            if (list.isEmpty()) {
                pageState.value = PageState.IS_EMPTY
            } else {
                pageState.value = PageState.IS_LOADED
            }
            callback(list)
        }
    }
}
