package com.firstcode.posts.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostsDao {
    @Query("SELECT * FROM  post")
    fun getAllPosts(): LiveData<List<Post>?>

    @Delete
    fun deletePost(post: Post)

    @Query("update post set title = :title where id = :id")
    fun editPost(id: Int, title: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post?>)

    @Insert
    fun insertPost(post: Post)
}
