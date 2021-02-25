package com.example.innoventesdemo.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.innoventesdemo.model.ShowSearchDetails

/**
 * Data source factory class
 */
class ShowDataSourceFactory(val repository: ShowRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowSearchDetails::class.java)) {
            return ShowSearchDetails(repository) as T
        }
        throw IllegalArgumentException("view model class pass illegal argument")
    }
}