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
import li.sau.projectwork.workers.blog.PostWorker

class BlogsFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogsBinding
    private lateinit var mAdapter: PostAdapter

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mViewModel: PostViewModel

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
            override fun onItemClick(id: Long) {
                val action = BlogsFragmentDirections.actionBlogsFragmentToBlogFragment(id.toString())
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

            mViewModel = PostViewModel(database.blogPostDao())
            mViewModel.postList.observe(this, Observer { posts ->
                mAdapter.submitList(posts)
            })
        }

        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)
    }
}
