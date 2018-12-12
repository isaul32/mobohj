package li.sau.projectwork

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.model.wp.blog.Post
import android.view.LayoutInflater
import li.sau.projectwork.databinding.ItemPostBinding


class PostAdapter : PagedListAdapter<Post, RecyclerView.ViewHolder>(Post.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bindTo(getItem(position))
    }

}