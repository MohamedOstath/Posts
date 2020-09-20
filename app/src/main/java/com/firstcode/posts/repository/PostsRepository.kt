package com.firstcode.posts.repository

import androidx.lifecycle.distinctUntilChanged
import com.firstcode.posts.api.ApiService
import com.firstcode.posts.api.ServiceGenerator
import com.firstcode.posts.base.BaseApplication
import com.firstcode.posts.data.Post
import com.firstcode.posts.data.PostsDao
import com.firstcode.posts.data.PostsRemoteDataSource
import com.firstcode.posts.data.resultLiveData
import com.firstcode.posts.database.PostsDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostsRepository private constructor(
    private val postsRemoteDataSource: PostsRemoteDataSource,
    private val postsDao: PostsDao
) {

    companion object {
        val instance =
            PostsRepository(
                PostsRemoteDataSource(ServiceGenerator.client.create(ApiService::class.java)),
                PostsDataBase.getInstance(BaseApplication.instance).cartDao()
            )
    }

    fun getPosts(start: Int) = resultLiveData(
        networkCall = {
            postsRemoteDataSource.getPosts(start)
        }
    ).distinctUntilChanged()

    suspend fun addPostsToRoom(posts: List<Post?>) = withContext(Dispatchers.IO) {
        postsDao.insertPosts(posts)
    }

    suspend fun addPostToRoom(post: Post) = withContext(Dispatchers.IO) {
        postsDao.insertPost(post)
    }

    suspend fun editPost(id: Int, title: String) = withContext(Dispatchers.IO) {
        postsDao.editPost(id, title)
    }

    suspend fun deletePost(post:Post) = withContext(Dispatchers.IO) {
        postsDao.deletePost(post)
    }

    fun getPostsFromRoom() = resultLiveData(
        databaseQuery = { postsDao.getAllPosts() }
    ).distinctUntilChanged()

}
