package li.sau.projectwork

import androidx.recyclerview.widget.RecyclerView
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post

class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(post: Post?) {
        post?.let {
            binding.title.text = post.title.rendered
            binding.author.text = post.id.toString()
        }
    }

}