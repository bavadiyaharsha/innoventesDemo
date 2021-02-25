package com.example.innoventesdemo.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.innoventesdemo.R
import com.example.innoventesdemo.databinding.ActivityMainBinding
import com.example.innoventesdemo.databinding.ShowlistItemBinding
import com.example.innoventesdemo.ui.ActivityViewModel

class SowDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: SowDetailsViewModel
    private lateinit var binding: ShowlistItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sow_details)
        viewModel = ViewModelProvider(this).get(SowDetailsViewModel::class.java)
    }
}