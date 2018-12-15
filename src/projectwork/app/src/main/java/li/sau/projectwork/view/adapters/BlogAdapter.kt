package li.sau.projectwork.view.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import li.sau.projectwork.ui.EventHandler
import li.sau.projectwork.view.holders.BlogViewHolder
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.model.wp.blog.Post.Companion.DIFF_CALLBACK


class BlogAdapter(
        private val mEventListener: EventHandler
) : PagedListAdapter<Post, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return BlogViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { post ->
            (holder as BlogViewHolder).bindTo(post, mEventListener)
        }
    }

}
