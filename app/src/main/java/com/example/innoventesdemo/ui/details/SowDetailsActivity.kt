package com.example.innoventesdemo.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ActivityMainBinding
import com.example.innoventesdemo.databinding.ActivitySowDetailsBinding
import com.example.innoventesdemo.databinding.ShowlistItemBinding
import com.example.innoventesdemo.ui.ActivityViewModel
import com.example.innoventesdemo.util.Constants
import com.example.innoventesdemo.util.Utils

class SowDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: SowDetailsViewModel
    private lateinit var binding: ActivitySowDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title=""
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sow_details)
        viewModel = ViewModelProvider(this).get(SowDetailsViewModel::class.java)

        if(Utils.checkInternetConnection(applicationContext))
            viewModel.details(intent.getStringExtra("id").toString(),  Constants.API_KEY)

        viewModel.isLoading.observe(this, Observer {
            if(it==false){
                binding.progressBar.visibility=View.GONE
            }
        })
        viewModel.contents.observe(this, Observer {
            try {



                    Glide.with(applicationContext)
                        .load(it.Poster)
                        .placeholder(R.drawable.placeholder_for_missing_posters)
                        .error(R.drawable.placeholder_for_missing_posters)
                        .into(binding.posterimg)


            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.actors.text=it.Actors
            binding.director.text=it.Director



        })


    }
}