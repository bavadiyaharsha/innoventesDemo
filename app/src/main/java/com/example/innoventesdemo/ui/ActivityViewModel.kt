package com.example.innoventesdemo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innoventesdemo.api.ShowApiService
import com.example.innoventesdemo.model.search.SearchResponse
import com.example.innoventesdemo.repo.ShowDataSourceFactory
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ActivityViewModel(val showDataSourceFactory: ShowDataSourceFactory):ViewModel() {

    var message= MutableLiveData<String>()
    var errormsg= MutableLiveData<String>()
    var isLoading=MutableLiveData<Boolean>()


    fun loadinit( key:String, page:Int,s:String){
        isLoading.value=true
        val userDataService = ShowApiService.instance?.api
        val call: Call<SearchResponse?>? = userDataService!!.getSearchResults(s,page,key)
        call!!.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>?,
                response: Response<SearchResponse?>
            ) {
                if (response.isSuccessful) {
                    val electronicParam = response.body()

                } else {
                    val errorbody= response.errorBody()

                }
                isLoading.value = false

            }

            override fun onFailure(
                call: Call<SearchResponse?>?,
                t: Throwable?
            ) {
                errormsg.value = t!!.message
                isLoading.value = false
            }
        })
    }



}