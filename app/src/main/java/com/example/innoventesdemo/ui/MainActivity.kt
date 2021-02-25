package com.example.innoventesdemo.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_search->{
                val manager = getSystemService(SEARCH_SERVICE) as SearchManager
                val search =item.itemId  as SearchView
                search.setSearchableInfo(manager.getSearchableInfo(componentName))
                search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        Log.i("MainActivity",
                            "Text Submitted $query"
                        )
                        /*  mSearchKey = query
                          refreshData()*/
                        return false
                    }

                    override fun onQueryTextChange(query: String): Boolean {

                        // DO Nothing
                        return true
                    }
                })

            }
            R.id.bookmark->{

                binding.bookmarkLayout.visibility=View.VISIBLE
                binding.recyclerView.visibility=View.GONE
            }

        }
        return super.onOptionsItemSelected(item)

    }
}