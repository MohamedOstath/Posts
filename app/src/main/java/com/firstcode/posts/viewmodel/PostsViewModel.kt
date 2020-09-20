package com.firstcode.posts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstcode.posts.data.Post
import com.firstcode.posts.data.Results
import com.firstcode.posts.repository.PostsRepository
import kotlinx.coroutines.launch

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {

    lateinit var mPosts: LiveData<Results<List<Post>?>>

    fun getPosts(start: Int) {
        mPosts = postsRepository.getPosts(start)
    }

    fun getPostsFromRoom() {
        mPosts = postsRepository.getPostsFromRoom()
    }

    fun addPosts(posts: List<Post?>) {
        viewModelScope.launch {
            postsRepository.addPostsToRoom(posts)
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            postsRepository.addPostToRoom(post)
        }
    }


    fun editPosts(id: Int, title: String) {
        viewModelScope.launch {
            postsRepository.editPost(id, title)
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postsRepository.deletePost(post)
        }
    }


}
