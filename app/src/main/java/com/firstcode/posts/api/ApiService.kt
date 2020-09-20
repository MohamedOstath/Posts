package com.firstcode.posts.api

import com.firstcode.posts.data.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos?_limit=10")
    suspend fun getPosts(
        @Query("_start") start: Int,
//        @Query("_limit") limit: Int,
    ): Response<List<Post>>
}
