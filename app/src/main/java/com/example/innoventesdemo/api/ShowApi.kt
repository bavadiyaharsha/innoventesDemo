package com.example.innoventesdemo.api

import com.example.innoventesdemo.model.search.SearchResponse
import com.example.innoventesdemo.model.showdetails.ShowDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApi {
    @GET("?")
    fun getSearchResults(
        @Query("s") title: String?,
        @Query("page") pages: Int,
        @Query("apikey") apikey: String?
    ): Call<SearchResponse?>?

    @GET("?")
    fun getShowDetails(
        @Query("i") imdbId: String?,
        @Query("apikey") apiKey: String?
    ): Call<ShowDetails?>?
}