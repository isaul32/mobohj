package li.sau.projectwork.view.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.ui.EventHandler
import li.sau.projectwork.view.models.PostViewModel

class BlogViewHolder(private val mBinding: ItemPostBinding) : RecyclerView.ViewHolder(mBinding.root) {

    fun bindTo(post: Post, eventListener: EventHandler) {
        mBinding.viewModel = PostViewModel(post.id,
                post.title.rendered,
                post.author_name,
                post.thumbnail_url)
        mBinding.handler = eventListener

        mBinding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): BlogViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemPostBinding.inflate(layoutInflater, parent, false)

            return BlogViewHolder(itemBinding)
        }
    }

}