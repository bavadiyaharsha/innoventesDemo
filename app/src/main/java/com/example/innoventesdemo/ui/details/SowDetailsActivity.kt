package com.example.innoventesdemo.ui.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ActivitySowDetailsBinding
import com.example.innoventesdemo.model.ShowSearchDetails
import com.example.innoventesdemo.repo.ShowRepository
import com.example.innoventesdemo.util.Constants
import com.example.innoventesdemo.util.Utils

class SowDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: SowDetailsViewModel
    private lateinit var binding: ActivitySowDetailsBinding
    private var mShowRepository: ShowRepository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title = ""
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sow_details)
        viewModel = ViewModelProvider(this).get(SowDetailsViewModel::class.java)
        mShowRepository = ShowRepository.getInstance(application)

        if (Utils.checkInternetConnection(applicationContext))
            viewModel.details(intent.getStringExtra("id").toString(), Constants.API_KEY)

        viewModel.isLoading.observe(this, Observer {
            if (it == false) {
                binding.progressBar.visibility = View.GONE
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
            supportActionBar!!.title = it.Title

            binding.actors.text = "Actors : " + it.Actors
            binding.director.text = "Director: " + it.Director
            binding.genre.text = "Language: " + it.Language
            binding.imdbRating.text = "Rating: " + it.imdbRating
            binding.awards.text = "Awards: " + it.Awards


        })

        binding.bookmarkButton.setOnClickListener {
            val s= ShowSearchDetails(viewModel.contents.value!!.Title,
                viewModel.contents.value!!.Year,
                viewModel.contents.value!!.imdbID,
                viewModel.contents.value!!.Poster)
            mShowRepository!!.insertBookMark(s)
        }
    }
}