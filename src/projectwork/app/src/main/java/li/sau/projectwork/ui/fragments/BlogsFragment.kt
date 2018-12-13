package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import li.sau.projectwork.OnItemClickListener
import li.sau.projectwork.PostAdapter
import li.sau.projectwork.PostViewModel
import li.sau.projectwork.R
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogsBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.utils.LIST_STATE_KEY
import li.sau.projectwork.workers.blog.PostWorker

class BlogsFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogsBinding
    private lateinit var mAdapter: PostAdapter

    private lateinit var mLayoutManager: LinearLayoutManager
    private var mListState: Parcelable? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBlogsBinding.inflate(inflater, container, false)

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        // Set up on click navigation
        val onClickListener = object : OnItemClickListener {
            override fun onItemClick(post: Post) {
                val action = BlogsFragmentDirections.actionBlogsFragmentToBlogFragment(post.id.toString())
                findNavController().navigate(action, options)
            }
        }

        // Create adatper and layout manager
        mAdapter = PostAdapter(onClickListener)
        mLayoutManager = LinearLayoutManager(context,
                RecyclerView.VERTICAL, false)


        // Set up recycler view
        mBinding.recyclerView.layoutManager = mLayoutManager
        mBinding.recyclerView.adapter = mAdapter


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { context ->
            val database = AppDatabase.getInstance(context)
            val viewModel = PostViewModel(database.blogPostDao())
            viewModel.postList.observe(this, Observer { posts ->
                mAdapter.submitList(posts)

                // Scroll to previous position
                mListState?.let { state ->
                    mLayoutManager.onRestoreInstanceState(state)
                }
            })
        }

        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mListState = mLayoutManager.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, mListState)
    }

    override fun onPause() {
        super.onPause()

        mListState = mLayoutManager.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()

        mListState?.let { state ->
            mLayoutManager.onRestoreInstanceState(state)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            mListState = it.getParcelable(LIST_STATE_KEY)
        }
    }
}
