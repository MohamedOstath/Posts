package com.firstcode.posts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.firstcode.posts.data.Post
import com.firstcode.posts.data.PostsDao

@Database(entities = [Post::class], version = 1)
abstract class PostsDataBase : RoomDatabase() {

    abstract fun cartDao(): PostsDao

    companion object {

        @Volatile
        private var instance: PostsDataBase? = null

        fun getInstance(context: Context): PostsDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PostsDataBase {
            return Room.databaseBuilder(context, PostsDataBase::class.java, "Posts")
                .addCallback(object : RoomDatabase.Callback() {
                })
                .build()
        }
    }


}