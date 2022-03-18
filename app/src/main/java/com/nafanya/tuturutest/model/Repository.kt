package com.nafanya.tuturutest.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nafanya.tuturutest.viewModel.PageState
import kotlinx.coroutines.flow.Flow

class Repository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun letAnimeFlow(
        query: String,
        pageState: MutableLiveData<PageState>,
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<Anime>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { AnimePagingSource(query, pageState) }
        ).flow
    }
}
