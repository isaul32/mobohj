package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.ui.EventHandler
import li.sau.projectwork.view.adapters.BlogAdapter
import li.sau.projectwork.view.models.BlogViewModel
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogBinding

class BlogFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogBinding
    private lateinit var mAdapter: BlogAdapter

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mViewModel: BlogViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBlogBinding.inflate(inflater, container, false)

        // Set up on click navigation
        val onClickListener = object : EventHandler {
            override fun onClick(id: Long) {
                val action = BlogFragmentDirections.actionBlogFragmentToPostFragment(id.toString())
                findNavController().navigate(action)
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

            mViewModel = BlogViewModel(database.postDao())
            mViewModel.postList.observe(this, Observer { posts ->
                mAdapter.submitList(posts)
            })
        }
    }
}
