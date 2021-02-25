package com.example.innoventesdemo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.innoventesdemo.model.ShowSearchDetails

@Dao
interface BookmarkDao {
    @Insert
    fun insertBookmark(bookMark: ShowSearchDetails?)

    @get:Query("SELECT * FROM bookmarkdata order by _id desc")
    val allBookMarks: LiveData<List<ShowSearchDetails?>?>?

    @Delete
    fun deleteBookmark(bookMark: ShowSearchDetails?)
}