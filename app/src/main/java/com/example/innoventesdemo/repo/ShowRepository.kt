package com.example.innoventesdemo.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.innoventesdemo.api.ShowApi
import com.example.innoventesdemo.api.ShowApiService
import com.example.innoventesdemo.db.BookMarkDatabase
import com.example.innoventesdemo.model.ShowSearchDetails
import com.example.innoventesdemo.model.search.SearchResponse
import com.example.innoventesdemo.model.showdetails.ShowDetails
import com.example.innoventesdemo.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * class to handle bookmark db
 */
class ShowRepository private constructor(application: Context) {
    private val DB_NAME = "showdb"
    private val bookMarkDatabase: BookMarkDatabase
    var showDetailList: LiveData<List<ShowSearchDetails>>
    private val mService: ShowApiService

    /**
     * insert data in bookmark table
     */
    fun insertBookMark(showSearchDetails: ShowSearchDetails?): Boolean {
        var result = true
        try {
            bookMarkDatabase.dao!!.insertBookmark(showSearchDetails)
        } catch (e: Exception) {
            result = false
            Log.i(TAG, "Exception while inserting bookmark $e")
        }
        return result
    }

    /**
     * delete data from bookmark db
     */
    fun deleteBookMark(showSearchDetails: ShowSearchDetails?): Boolean {
        var result = true
        try {
            bookMarkDatabase.dao!!.deleteBookmark(showSearchDetails)
        } catch (e: Exception) {
            result = false
            Log.i(TAG, "Exception while inserting bookmark $e")
        }
        return result
    }

    /**
     * fetch all bookmarks from DB
     *
     * @return
     */
    val allBookMark: LiveData<List<ShowSearchDetails?>?>?
        get() = bookMarkDatabase.dao!!.allBookMarks

    /**
     * Get show search result
     *
     * @param key  key to search
     * @param page page to get in result // by default 10 results are received in one call
     */
    fun getSearchResult(key: String?, page: Int): Call<SearchResponse?>? {
        return mService.api.getSearchResults(key, page, Constants.API_KEY)
    }

    /**
     * Get details of a show
     *
     * @param imdbId id of show for which details needs to be found
     */
    fun getShowDetails(imdbId: String?): LiveData<ShowDetails?> {
        val mutableLiveData: MutableLiveData<ShowDetails?> = MutableLiveData<ShowDetails?>()
        val showApi: ShowApi = ShowApiService.instance!!.api
        val call: Call<ShowDetails?>? = showApi.getShowDetails(imdbId, Constants.API_KEY)
        call!!.enqueue(object : Callback<ShowDetails?> {
            override fun onResponse(call: Call<ShowDetails?>, response: Response<ShowDetails?>) {
                mutableLiveData.setValue(response.body())
            }

            override fun onFailure(call: Call<ShowDetails?>, t: Throwable) {
                mutableLiveData.setValue(null)
            }
        })
        return mutableLiveData
    }

    companion object {
        private val TAG = ShowRepository::class.java.simpleName

        @Volatile
        private var instance: ShowRepository? = null
        @JvmStatic
        fun getInstance(application: Context): ShowRepository? {
            if (instance == null) {
                synchronized(ShowRepository::class.java) {
                    if (instance == null) {
                        instance = ShowRepository(application)
                    }
                }
            }
            return instance
        }
    }

    init {
        bookMarkDatabase = Room.databaseBuilder<BookMarkDatabase>(
            application, BookMarkDatabase::class.java,
            DB_NAME
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        mService = ShowApiService.instance!!
        showDetailList = allBookMark as LiveData<List<ShowSearchDetails>>
    }
}