package com.example.innoventesdemo.ui

import android.app.SearchManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.innoventesdemo.R
import com.example.innoventesdemo.adepter.BookMarkAdepter
import com.example.innoventesdemo.databinding.ActivityMainBinding
import com.example.innoventesdemo.model.ShowSearchDetails
import com.example.innoventesdemo.model.search.Search
import com.example.innoventesdemo.repo.ShowRepository
import com.example.innoventesdemo.ui.details.SowDetailsActivity
import com.example.innoventesdemo.util.Constants
import com.example.innoventesdemo.util.Utils
import com.example.roomdemo.adepter.MyRecycleview

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var adepter: MyRecycleview
    lateinit var bookMarkAdepter: BookMarkAdepter
    private var linearLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager1: GridLayoutManager? = null
    private var loading = false
    private var mBookmarkList: LiveData<List<ShowSearchDetails>>? = null
    private var mShowRepository: ShowRepository? = null

    private var pageCount = 1
    var mSearchKey: String = "Avenger"
    private lateinit var arrayList: ArrayList<Search>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        mShowRepository = ShowRepository.getInstance(application)

        initRecyclerView()
        supportActionBar!!.title = ""
        if (Utils.checkInternetConnection(applicationContext))
            viewModel.loadinit(mSearchKey, pageCount, Constants.API_KEY)
        viewModel.isLoading.observe(this, Observer {
            if (!it) {
                binding.progressBar1.visibility = View.GONE
                binding.laylist.visibility = View.VISIBLE

                binding.llLoadMoreProgress.setVisibility(View.GONE)
                if (!viewModel.contentlist.value.isNullOrEmpty()) {
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
                    }
                } else {
                    binding.laylist.visibility = View.GONE
                }

            }
        })


    }

    private fun bookMarkerlistItemClick(selsectItem: ShowSearchDetails) {
        var intent = Intent(this@MainActivity, SowDetailsActivity::class.java)
        intent.putExtra("id", selsectItem.imdbID)
        startActivity(intent)
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
                if (mSearchKey.length >= 3) {
                    pageCount = 1
                    arrayList.clear()
                    binding.bookmarkLayout.visibility = View.GONE
                    binding.laylist.visibility = View.VISIBLE
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

        when (item.itemId) {
            R.id.action_search -> {


            }
            R.id.bookmark -> {
                mBookmarkList = mShowRepository!!.allBookMark as LiveData<List<ShowSearchDetails>>

                binding.bookmarkLayout.visibility = View.VISIBLE
                binding.laylist.visibility = View.GONE

                mBookmarkList!!.observe(this, Observer {
                    bookMarkAdepter = BookMarkAdepter(
                        it as List<ShowSearchDetails>,
                        applicationContext,
                        { selsectItem: ShowSearchDetails ->
                            bookMarkerlistItemClick(
                                selsectItem
                            )
                        })
                    binding.bookmarkRecylerView.adapter = bookMarkAdepter
                })
            }

        }
        return super.onOptionsItemSelected(item)

    }

    private fun setAdapter(arrayList: ArrayList<Search>) {
        adepter = MyRecycleview(arrayList, applicationContext, { selsectItem: Search ->
            listItemClick(
                selsectItem
            )
        })
        binding.recyclerView.adapter = adepter

    }

    private fun listItemClick(selsectItem: Search) {
        var intent = Intent(this@MainActivity, SowDetailsActivity::class.java)
        intent.putExtra("id", selsectItem.imdbID)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        arrayList = ArrayList()

        //for grid Partitioning
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager = GridLayoutManager(this, 3)
        } else {
            linearLayoutManager = GridLayoutManager(this, 7)
        }
        binding.recyclerView.setLayoutManager(linearLayoutManager)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager1 = GridLayoutManager(this, 3)
        } else {
            linearLayoutManager1 = GridLayoutManager(this, 7)
        }
        binding.bookmarkRecylerView.setLayoutManager(linearLayoutManager1)


    }


    private fun setScrollListener() {
        //if user scroll the load more item from next page
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastvisibleitemposition: Int? =
                    linearLayoutManager?.findLastVisibleItemPosition()

                if (adepter != null) {

                    if (lastvisibleitemposition == adepter.getItemCount() - 1) {

                        if (!loading) {
                            loading = true
                            viewModel.isLoading.value = true
                            callApi()
                        }
                    }
                }
            }

        })
    }

    private fun callApi() {
        try {
            if (pageCount == 1) {
                arrayList.clear()
                binding.progressBar1.visibility = View.VISIBLE
                binding.laylist.visibility = View.GONE
                binding.llLoadMoreProgress.setVisibility(View.VISIBLE)
            } else {
                binding.progressBar1.visibility = View.GONE
                binding.laylist.visibility = View.VISIBLE
                binding.llLoadMoreProgress.setVisibility(View.VISIBLE)
                binding.loadmsg.setVisibility(View.GONE)

                viewModel.loadinit(mSearchKey, pageCount, Constants.API_KEY)


            }

            /*    if () {

                    } else {
                    binding.llLoadMoreProgress.setVisibility(View.GONE)
                    binding.loadmsg.setVisibility(View.VISIBLE)

                }*/

        } catch (e: java.lang.Exception) {
        }
    }


    override fun onResume() {
        super.onResume()


        callApi()
        setScrollListener()


    }

}