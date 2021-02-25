package com.example.innoventesdemo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innoventesdemo.model.search.SearchResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ActivityViewModel():ViewModel() {

    var message= MutableLiveData<String>()
    var errormsg= MutableLiveData<String>()
    var isLoading=MutableLiveData<Boolean>()

}