package li.sau.projectwork

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import li.sau.projectwork.databinding.ItemPostBinding
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.utils.BASE_URI

class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(post: Post, onClickListener: OnItemClickListener) {
        binding.title.text = post.title.rendered
        binding.author.text = post.id.toString()

        // Bind onClick listener
        itemView.setOnClickListener { onClickListener.onItemClick(post) }

        // Load and set up thumbnail
        val featuredmedia = post.embedded.featuredmedia
        if (featuredmedia.isNotEmpty()) {
            val url = BASE_URI + "/" + featuredmedia.first().source_url
            Picasso.get()
                    .load(url)
                    .into(binding.thumbnail)
        }
    }

}