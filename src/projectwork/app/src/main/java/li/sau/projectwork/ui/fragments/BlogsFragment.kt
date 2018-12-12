package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import li.sau.projectwork.PostAdapter
import li.sau.projectwork.PostViewModel
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogsBinding
import li.sau.projectwork.workers.blog.PostWorker

class BlogsFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBlogsBinding.inflate(inflater, container, false)

        val context = activity?.applicationContext

        context?.let {
            val postAdapter = PostAdapter()

            // Set up recycler view
            binding.recyclerView.layoutManager = LinearLayoutManager(context,
                    RecyclerView.VERTICAL, false)
            binding.recyclerView.adapter = postAdapter

            val database = AppDatabase.getInstance(context)
            val viewModel = PostViewModel(database.blogPostDao())
            viewModel.postList.observe(this, Observer { posts ->
                postAdapter.submitList(posts)
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)

        /*val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        navigateButton.setOnClickListener {
            findNavController().navigate(R.id.blogFragment, null, options)
        }*/
    }
}
