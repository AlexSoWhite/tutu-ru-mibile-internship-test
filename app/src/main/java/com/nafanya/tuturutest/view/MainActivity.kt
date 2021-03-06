package com.nafanya.tuturutest.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.ActivityMainBinding
import com.nafanya.tuturutest.databinding.AnimeListItemBinding
import com.nafanya.tuturutest.model.Anime
import com.nafanya.tuturutest.viewModel.MainActivityViewModel
import com.nafanya.tuturutest.viewModel.PageState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var pagedAdapter: AnimeListAdapter
    private lateinit var staticListAdapter: StaticListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // setting search behavior
        binding.search.setOnKeyListener { view, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                val text = (view as EditText).text.toString()
                search(text)
                // clearing focus from input
                view.clearFocus()
            }
            true
        }
        // set refresh behavior
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = true
            onRefresh()
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        // observing page state
        val pageStateObserver = Observer<PageState> {
            when(it) {
                PageState.IS_FIRST_LOADING -> onFirstLoading()
                PageState.IS_LOADING -> onLoading()
                PageState.IS_LOADING_MORE -> onLoadingMore()
                PageState.IS_LOADED -> onLoaded()
                PageState.IS_ERROR -> onError()
                PageState.IS_EMPTY -> onEmpty()
                else -> {}
            }
        }
        viewModel.pageState.observe(this, pageStateObserver)
        viewModel.setRepo()
        pagedAdapter = AnimeListAdapter {anime, listItemBinding ->
            startDetailActivity(anime, listItemBinding)
        }
        binding.recycler.adapter = pagedAdapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            viewModel.letAnimeFlow(query).distinctUntilChanged().collectLatest {
                pagedAdapter.submitData(it)
                if (binding.recycler.adapter is StaticListAdapter) {
                    binding.recycler.adapter = pagedAdapter
                }
                val list = pagedAdapter.snapshot().items
                if (list.isNotEmpty()) {
                    viewModel.putToCache(list)
                }
            }
        }
    }

    private fun startDetailActivity(anime: Anime, listItemBinding: AnimeListItemBinding) {
        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(listItemBinding.image, getString(R.string.anime_transition))
            )
            bundle = options.toBundle()
        }
        val detailIntent = Intent(this, AnimeDetailActivity::class.java)
        val jsonString = Gson().toJson(anime)
        detailIntent.putExtra("anime", jsonString)
        if (bundle == null) {
            startActivity(detailIntent)
        } else {
            startActivity(detailIntent, bundle)
        }
    }

    private fun onFirstLoading() {
        search("")
    }

    private fun onLoading() {
        binding.loader.visibility = View.VISIBLE
        binding.recycler.visibility = View.GONE
        binding.empty.visibility = View.GONE
        binding.loadMore.visibility = View.GONE
    }

    private fun onLoaded() {
        binding.loader.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        binding.empty.visibility = View.GONE
        binding.loadMore.visibility = View.GONE
    }

    private fun onEmpty() {
        binding.loader.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.empty.visibility = View.VISIBLE
        binding.loadMore.visibility = View.GONE
    }

    private fun onError() {
        binding.loader.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.empty.visibility = View.GONE
        viewModel.loadFromCache {
            staticListAdapter = StaticListAdapter(it) { anime, binding ->
                startDetailActivity(anime, binding)
            }
            binding.recycler.adapter = staticListAdapter
        }
        Toast.makeText(
            this,
            "An error occurred",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLoadingMore() {
        binding.loadMore.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        pagedAdapter.refresh()
        binding.refresh.isRefreshing = false
    }
}
