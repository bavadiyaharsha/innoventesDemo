package com.example.innoventesdemo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innoventesdemo.api.ShowApiService
import com.example.innoventesdemo.model.search.Search
import com.example.innoventesdemo.model.search.SearchResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ActivityViewModel:ViewModel() {

    var message= MutableLiveData<String>()
    var errormsg= MutableLiveData<String>()
    var isLoading=MutableLiveData<Boolean>()
    var contentlist = MutableLiveData<List<Search>>()


    fun loadinit(s:String, page:Int,key:String){
        isLoading.value=true
        val userDataService = ShowApiService.instance?.api
        val call: Call<SearchResponse?>? = userDataService!!.getSearchResults(s,page,key)
        call!!.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>?,
                response: Response<SearchResponse?>
            ) {
                try {
                    if (response.isSuccessful) {
                        val list = response.body()
                        if (list != null) {
                            contentlist.value = list.Search
                        }
                    } else {

                    }

                }catch (e:Exception){}
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