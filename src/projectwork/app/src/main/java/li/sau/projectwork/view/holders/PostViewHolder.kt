package li.sau.projectwork.view.holders

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.ui.OnItemClickListener
import li.sau.projectwork.utils.BASE_URI

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(post: Post, onClickListener: OnItemClickListener) {
        binding.title.text = post.title.rendered
        binding.author.text = post.author_name

        // Bind onClick listener
        itemView.setOnClickListener { onClickListener.onItemClick(post.id) }

        // Load and set up thumbnail
        post.thumbnail_url?.let {
            val url = BASE_URI + it
            Picasso.get()
                    .load(url)
                    .into(binding.thumbnail)
        }
    }

}