package com.firstcode.posts.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firstcode.posts.data.Post
import com.firstcode.posts.databinding.ItemLoadingBinding
import com.firstcode.posts.databinding.ItemPostBinding
import com.firstcode.posts.utils.OnItemClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.*
import java.lang.Exception

class PostsAdapter(val context: Context, private val list: List<Post?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_LOADING = 2
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        if (list[position] != null) {
            return TYPE_ITEM
        }
        return TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_ITEM) {
            return PostViewHolder(
                ItemPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
        return LoadingViewHolder(
            ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ITEM) {
            (holder as PostViewHolder).bind(list[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PostViewHolder(private val itemPostBinding: ItemPostBinding) :
        RecyclerView.ViewHolder(itemPostBinding.root) {

        private val clickListener = View.OnClickListener {
            onItemClickListener?.onItemClick(it, adapterPosition)
        }

        fun bind(post: Post) {
            itemPostBinding.post = post
            itemPostBinding.clickListener = clickListener

            Picasso.get()
                .load(post.thumbnailUrl)
                .into(itemPostBinding.imgPost, object : Callback {
                    override fun onSuccess() {
                        itemPostBinding.placeholder.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        e?.printStackTrace()
                    }
                })

        }
    }

    class LoadingViewHolder(itemLoadingBinding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(itemLoadingBinding.root)
}