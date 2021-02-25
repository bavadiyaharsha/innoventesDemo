package com.example.innoventesdemo.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innoventesdemo.api.ShowApiService
import com.example.innoventesdemo.model.showdetails.ShowDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SowDetailsViewModel:ViewModel() {
    var errormsg= MutableLiveData<String>()
    var isLoading= MutableLiveData<Boolean>()
    var contents = MutableLiveData<ShowDetails>()


    fun details(s: String, key: String){
        isLoading.value=true
        val userDataService = ShowApiService.instance?.api
        val call: Call<ShowDetails?>? = userDataService!!.getShowDetails(s, key)
        call!!.enqueue(object : Callback<ShowDetails?> {
            override fun onResponse(
                call: Call<ShowDetails?>?,
                response: Response<ShowDetails?>
            ) {
                try {
                    if (response.isSuccessful) {
                        val list = response.body()
                        if (list != null) {
                            contents.value = list
                        }
                    }

                } catch (e: Exception) {
                }
                isLoading.value = false

            }

            override fun onFailure(
                call: Call<ShowDetails?>?,
                t: Throwable?
            ) {
                errormsg.value = t!!.message
                isLoading.value = false
            }
        })
    }

}