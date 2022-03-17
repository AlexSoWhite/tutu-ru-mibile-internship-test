package com.nafanya.tuturutest.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.ActivityMainBinding
import com.nafanya.tuturutest.databinding.AnimeListItemBinding
import com.nafanya.tuturutest.model.animeObjects.Anime
import com.nafanya.tuturutest.viewModel.MainActivityViewModel
import com.nafanya.tuturutest.viewModel.PageState

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isEmpty() or it.isBlank()) {
                        viewModel.getAll()
                        true
                    } else {
                        viewModel.search(query)
                        true
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        viewModel.getAll()
                        return true
                    }
                }
                return false
            }
        })
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val pageStateObserver = Observer<PageState> {
            when(it) {
                PageState.IS_FIRST_LOADING -> onFirstLoading()
                PageState.IS_LOADING -> onLoading()
                PageState.IS_LOADED -> onLoaded()
                PageState.IS_ERROR -> onError()
                PageState.IS_EMPTY -> onEmpty()
                else -> {}
            }
        }
        viewModel.pageState.observe(this, pageStateObserver)
        val listObserver = Observer<List<Anime>> {
            binding.recycler.adapter = Adapter(it) { anime, listItemBinding ->
                startDetailActivity(anime, listItemBinding)
            }
            binding.recycler.layoutManager = LinearLayoutManager(this)
            binding.recycler.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            )
        }
        viewModel.list.observe(this, listObserver)
    }

    private fun startDetailActivity(anime: Anime, listItemBinding: AnimeListItemBinding) {
        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(listItemBinding.image, getString(R.string.anime_item_image_transition))
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
        viewModel.getAll()
    }

    private fun onLoading() {
        binding.loader.visibility = View.VISIBLE
        binding.recycler.visibility = View.INVISIBLE
        binding.error.visibility = View.INVISIBLE
    }

    private fun onLoaded() {
        binding.loader.visibility = View.INVISIBLE
        binding.recycler.visibility = View.VISIBLE
        binding.error.visibility = View.INVISIBLE
    }

    private fun onEmpty() {
        binding.loader.visibility = View.INVISIBLE
        binding.recycler.visibility = View.INVISIBLE
        binding.error.visibility = View.INVISIBLE
    }

    private fun onError() {
        binding.error.visibility = View.VISIBLE
        binding.loader.visibility = View.INVISIBLE
        binding.recycler.visibility = View.INVISIBLE
    }
}
