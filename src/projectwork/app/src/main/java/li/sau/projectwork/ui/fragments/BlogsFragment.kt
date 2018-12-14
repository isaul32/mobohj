package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.view.EventHandler
import li.sau.projectwork.view.adapters.BlogAdapter
import li.sau.projectwork.view.models.BlogViewModel
import li.sau.projectwork.R
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogsBinding

class BlogsFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogsBinding
    private lateinit var mAdapter: BlogAdapter

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mViewModel: BlogViewModel

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
        val onClickListener = object : EventHandler {
            override fun onClick(id: Long) {
                val action = BlogsFragmentDirections.actionBlogsFragmentToBlogFragment(id.toString())
                findNavController().navigate(action, options)
            }
        }

        // Create adatper and layout manager
        mAdapter = BlogAdapter(onClickListener)
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

            mViewModel = BlogViewModel(database.blogPostDao())
            mViewModel.postList.observe(this, Observer { posts ->
                mAdapter.submitList(posts)
            })
        }

        /*val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)*/
    }
}
