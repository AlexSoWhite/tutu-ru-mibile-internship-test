package com.nafanya.tuturutest.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nafanya.tuturutest.R
import com.nafanya.tuturutest.databinding.ActivityMainBinding
import com.nafanya.tuturutest.model.animeObjects.Anime
import com.nafanya.tuturutest.viewModel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getAll()
        val observer = Observer<List<Anime>> {
            binding.recycler.adapter = Adapter(it) { anime, view ->
                var bundle: Bundle? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        view,
                        getString(R.string.anime_item_detail_transition)
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
            binding.recycler.layoutManager = LinearLayoutManager(this)
            binding.recycler.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            )
        }
        viewModel.list.observe(this, observer)
    }
}
