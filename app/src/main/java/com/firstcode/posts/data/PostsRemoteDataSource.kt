package com.firstcode.posts.data

import com.firstcode.posts.api.ApiService

class PostsRemoteDataSource(private val apiService: ApiService) :
    BaseDataSource() {

    suspend fun getPosts(start: Int) = getResult {
        apiService.getPosts(start)
    }

}