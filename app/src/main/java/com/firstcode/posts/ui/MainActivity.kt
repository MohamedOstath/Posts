package com.firstcode.posts.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstcode.posts.R
import com.firstcode.posts.data.Post
import com.firstcode.posts.data.Results
import com.firstcode.posts.repository.PostsRepository
import com.firstcode.posts.utils.AppUtils
import com.firstcode.posts.utils.EndlessRecyclerViewScrollListener
import com.firstcode.posts.utils.OnItemClickListener
import com.firstcode.posts.viewmodel.PostsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var postsViewModel: PostsViewModel
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var posts: ArrayList<Post?>
    private lateinit var manager: LinearLayoutManager
    private lateinit var endlessScrollListener: EndlessRecyclerViewScrollListener

    private var startingPageIndex = 1
    private val pageCount = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postsViewModel = PostsViewModel(PostsRepository.instance)
        setAdapter()

        if (AppUtils.isNetworkAvailable()) {
            getPosts(0)
        } else {
            getRoomPosts()
        }

    }

    private fun getRoomPosts() {

        postsViewModel.getPostsFromRoom()

        postsViewModel.mPosts.observe(this, {
            when (it.status) {
                Results.Status.LOADING -> {
                }

                Results.Status.SUCCESS -> {

                    posts.clear()
                    posts.addAll(it.data!!)
                    postsAdapter.notifyDataSetChanged()
                }

                Results.Status.ERROR -> {

                }
            }
        })
    }

    private fun setAdapter() {
        posts = arrayListOf()
        postsAdapter = PostsAdapter(this, posts)
        postsAdapter.onItemClickListener = this
        manager = LinearLayoutManager(this)
        endlessScrollListener =
            object : EndlessRecyclerViewScrollListener(manager, startingPageIndex) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (AppUtils.isNetworkAvailable() && page <= pageCount && posts[posts.size - 1] != null) {
                        posts.add(null)
                        rv_posts.post { postsAdapter.notifyItemInserted(posts.size - 1) }
                        getPosts((page - 1) * 10)
                    }
                }
            }
        rv_posts.apply {
            adapter = postsAdapter
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(this@MainActivity, RecyclerView.VERTICAL))
            addOnScrollListener(endlessScrollListener)
        }
    }

    private fun getPosts(start: Int) {
        Log.e("onLoadMore: ", "$start")

        if (AppUtils.isNetworkAvailable())
            postsViewModel.getPosts(start)
        else
            postsViewModel.getPostsFromRoom()

        postsViewModel.mPosts.observe(this, {
            when (it.status) {
                Results.Status.LOADING -> {
                    Log.e("GET_POSTS", "LOADING")

                    if (start == 0)
                        showLoading()
                }

                Results.Status.SUCCESS -> {
                    Log.e("GET_POSTS", "SUCCESS")

                    // remove pagination last loading item
                    if (posts.size > 0 && posts[posts.size - 1] == null) {
                        posts.removeAt(posts.size - 1)
                        postsAdapter.notifyItemRemoved(posts.size - 1)
                    }

                    showContent()

                    posts.addAll(it.data!!)
                    postsAdapter.notifyDataSetChanged()
                    postsViewModel.addPosts(posts)

                }

                Results.Status.ERROR -> {
                    Log.e("GET_POSTS", "ERROR")

                }
            }
        })
    }

    private fun showContent() {
        prb_posts.visibility = View.GONE
        rv_posts.visibility = View.VISIBLE

    }

    private fun showLoading() {
        prb_posts.visibility = View.VISIBLE
        rv_posts.visibility = View.GONE

    }

    override fun onItemClick(view: View, pos: Int) {

        if (view.id == R.id.img_more) {
            showMoreDialog(pos)
        } else
            startActivity(
                Intent(this, PostDetailsActivity::class.java).putExtra(
                    Post::class.java.name,
                    posts[pos]
                )
            )
    }

    private fun showMoreDialog(pos: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_options)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<TextView>(R.id.tv_edit).setOnClickListener {
            dialog.dismiss()
            showEditDialog(pos)
        }

        dialog.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
            if (!AppUtils.isNetworkAvailable())
                postsViewModel.deletePost(posts[pos]!!)
            else {
                posts.removeAt(pos)
                postsAdapter.notifyDataSetChanged()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditDialog(pos: Int) {

        val txtTitle: EditText

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        txtTitle = dialog.findViewById(R.id.txt_title)
        txtTitle.setText(posts[pos]!!.title)

        dialog.findViewById<TextView>(R.id.btn_edit).setOnClickListener {
            if (txtTitle.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "add title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            postsViewModel.editPosts(posts[pos]!!.id, txtTitle.text.toString().trim())
            posts[pos]!!.title = txtTitle.text.toString().trim()
            postsAdapter.notifyItemChanged(pos)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAddDialog() {

        val txtAddTitle: EditText

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        txtAddTitle = dialog.findViewById(R.id.txt_add_title)

        dialog.findViewById<TextView>(R.id.btn_add).setOnClickListener {
            if (txtAddTitle.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "add title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!AppUtils.isNetworkAvailable())
                postsViewModel.addPost(
                    Post(
                        txtAddTitle.text.toString().trim(),
                        "https://via.placeholder.com/600/2cd88b",
                        "https://via.placeholder.com/600/2cd88b"
                    )
                )
            else {
                posts.add(
                    Post(
                        txtAddTitle.text.toString().trim(),
                        "https://via.placeholder.com/600/2cd88b",
                        "https://via.placeholder.com/600/2cd88b"
                    )
                )
                postsAdapter.notifyDataSetChanged()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            showAddDialog()
        }
        return super.onOptionsItemSelected(item)
    }
}
