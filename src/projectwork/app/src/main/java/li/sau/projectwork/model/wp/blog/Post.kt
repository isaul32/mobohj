package li.sau.projectwork.model.wp.blog

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import androidx.room.Embedded
import com.squareup.moshi.Json

@Entity
data class Post(
        val author: Long,
        //val categories: List<Long>,
        val comment_status: String,
        @Embedded(prefix = "content_")
        val content: HTMLPart,
        val date: String,
        val date_gmt: String,
        @Embedded(prefix = "excerpt_")
        val excerpt: HTMLPart,
        val featured_media: Long,
        val format: String,
        @Embedded(prefix = "guid_")
        val guid: HTMLPart,
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val lang: String,
        val link: String,
        //val meta: List<Any>,
        val modified: String,
        val modified_gmt: String,
        val ping_status: String,
        val slug: String,
        val status: String,
        val sticky: Boolean,
        //val tags: List<Long>,
        val template: String,
        @Embedded(prefix = "title_")
        val title: HTMLPart,
        @Embedded
        val translations: Translations,
        val type: String,
        val wps_subtitle: String
        //@Ignore
        //var featuredmedia: List<Featuredmedia> = emptyList()
        //@Embedded(prefix = "embedded_")
        //@Json(name = "_embedded")
        //val embedded: li.sau.projectwork.model.wp.blog.Embedded
) {

    @Ignore
    @Json(name = "_embedded")
    //@field:Json(name = "_embedded")
    var embedded: li.sau.projectwork.model.wp.blog.Embedded
            = li.sau.projectwork.model.wp.blog.Embedded()
    //var featuredmedia: List<Featuredmedia> = emptyList()

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {

            override fun areItemsTheSame(
                    oldItem: Post,
                    newItem: Post
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                    oldItem: Post,
                    newItem: Post
            ) = oldItem.id == newItem.id

        }
    }
}