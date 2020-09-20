package com.firstcode.posts.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firstcode.posts.R
import com.firstcode.posts.data.Post
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*
import java.lang.Exception

class PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        val post: Post = intent.getSerializableExtra(Post::class.java.name) as Post

        Log.e("get_post_serializable", Gson().toJson(post))

        setUpActionBar()
        updateViews(post)
    }

    private fun setUpActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        title = "Post Details"
    }

    private fun updateViews(post: Post) {
        tv_title.text = post.title

        Log.e("get_image_url", post.url)

        Picasso.get()
            .load(post.url)
            .into(img_post, object : Callback {
                override fun onSuccess() {
                    placeholder.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            })

//        Glide.with(this)
//            .load(post.url).listener(object :
//                RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    placeholder.visibility = View.GONE
//                    return false
//                }
//
//            }).into(img_post)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}