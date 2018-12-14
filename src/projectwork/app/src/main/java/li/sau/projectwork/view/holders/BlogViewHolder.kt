package li.sau.projectwork.view.holders

import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.view.EventHandler
import li.sau.projectwork.view.models.PostViewModel

class BlogViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(post: Post, eventListener: EventHandler) {
        binding.viewModel = PostViewModel(post.id,
                post.title.rendered,
                post.author_name,
                post.thumbnail_url)
        binding.handler = eventListener

        binding.executePendingBindings()
    }

}