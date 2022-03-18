package com.nafanya.tuturutest.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nafanya.tuturutest.viewModel.PageState
import retrofit2.HttpException
import java.io.IOException

class AnimePagingSource(
    private val query: String,
    private val pageState: MutableLiveData<PageState>
) : PagingSource<Int, Anime>() {
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        val page = params.key ?: 0
        return try {
            val response = if (query.isEmpty()) {
                Client.getInstance().getApi().getAll(page).data
            } else {
                Client.getInstance().getApi().search(query, page).data
            }
            when {
                response == null -> {
                    pageState.value = PageState.IS_ERROR
                }
                response.isEmpty() && pageState.value != PageState.IS_LOADED -> {
                    pageState.value = PageState.IS_EMPTY
                }
                else -> {
                    pageState.value = PageState.IS_LOADED
                }
            }
            LoadResult.Page(
                response!!, prevKey = if (page == 0) null else page - 10,
                nextKey = if (response.isEmpty()) null else page + 10
            )
        } catch (exception: IOException) {
            pageState.value = PageState.IS_ERROR
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            pageState.value = PageState.IS_ERROR
            LoadResult.Error(exception)
        }
    }
}
