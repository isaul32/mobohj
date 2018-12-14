package li.sau.projectwork.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import li.sau.projectwork.ui.OnItemClickListener
import li.sau.projectwork.view.holders.PostViewHolder
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.model.wp.blog.Post.Companion.DIFF_CALLBACK


class PostAdapter(
        private val mOnClickListener: OnItemClickListener
) : PagedListAdapter<Post, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = getItem(position)
        post?.let {
            (holder as PostViewHolder).bindTo(it, mOnClickListener)
        }
    }

}
