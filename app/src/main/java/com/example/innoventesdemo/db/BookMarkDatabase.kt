package com.example.innoventesdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.innoventesdemo.model.ShowSearchDetails

@Database(entities = [ShowSearchDetails::class], version = 1, exportSchema = false)
abstract class BookMarkDatabase : RoomDatabase() {
    abstract val dao: BookmarkDao?
    companion object {
        @Volatile
        private var INSTANCE: BookMarkDatabase? = null

        fun getInstance(context: Context): BookMarkDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookMarkDatabase::class.java,
                        "Subscriberdatabase"
                    ).build()
                }
                return instance
            }

        }
    }
}