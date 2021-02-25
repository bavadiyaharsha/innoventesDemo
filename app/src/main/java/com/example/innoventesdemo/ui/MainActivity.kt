package com.example.innoventesdemo.ui

import android.app.SearchManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ActivityMainBinding
import com.example.innoventesdemo.model.search.Search
import com.example.innoventesdemo.util.Constants
import com.example.roomdemo.adepter.MyRecycleview

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var adepter: MyRecycleview
    private var linearLayoutManager: GridLayoutManager? = null
    private var loading = false
    private var pageCount = 1
    var mSearchKey:String="Avenger"
    private lateinit var arrayList: ArrayList<Search>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        initRecyclerView()
        viewModel.loadinit(mSearchKey, pageCount, Constants.API_KEY)


        viewModel.isLoading.observe(this, Observer {
            if (!it) {
                binding.progressBar1.visibility = View.GONE
                binding.laylist.visibility = View.VISIBLE

                binding.llLoadMoreProgress.setVisibility(View.GONE)
                arrayList.addAll(viewModel.contentlist.value as ArrayList<Search>)
                if (arrayList.isNotEmpty()) {
                    try {

                        binding.laylist.visibility = View.VISIBLE
                        loading = false
                        if (pageCount <= 1) {
                            setAdapter(arrayList)

                        } else {
                            adepter.notifyDataSetChanged()
                        }
                        if ((viewModel.contentlist.value as ArrayList<Search>).size > 0) {
                            pageCount++
                        }
                    } catch (e: Exception) {
                        binding.laylist.visibility = View.GONE
                    }
                } else {
                    binding.laylist.visibility = View.GONE
                }

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val manager = getSystemService(SEARCH_SERVICE) as SearchManager
        val search = menu.findItem(R.id.action_search).actionView as SearchView
        search.setSearchableInfo(manager.getSearchableInfo(componentName))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i(
                    "MainActivity",
                    "Text Submitted $query"
                )
                mSearchKey = query
                if(mSearchKey.length>=3) {
                    pageCount=1
                    arrayList.clear()
                    viewModel.loadinit(mSearchKey, pageCount, Constants.API_KEY)
                }
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {

                // DO Nothing
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_search -> {


            }
            R.id.bookmark -> {

                binding.bookmarkLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }

        }
        return super.onOptionsItemSelected(item)

    }

    private fun setAdapter(arrayList: ArrayList<Search>) {
        adepter = MyRecycleview(arrayList, applicationContext)
        binding.recyclerView.adapter = adepter

    }
    private fun initRecyclerView() {
        arrayList = ArrayList()

        //for grid Partitioning
        if (resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager = GridLayoutManager(this, 3)
        } else {
            linearLayoutManager = GridLayoutManager(this, 7)
        }
        binding.recyclerView.setLayoutManager(linearLayoutManager)


    }
}