package li.sau.projectwork.model.wp.blog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "post_featuredmedia_join",
        indices = [
            Index(value = ["post_id", "featuredmedia_id"])
        ],
        primaryKeys = ["post_id", "featuredmedia_id"],
        foreignKeys = [
            ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["post_id"]),
            ForeignKey(entity = Featuredmedia::class, parentColumns = ["id"], childColumns = ["featuredmedia_id"])
        ])
data class PostFeaturedmediaJoin(
        val post_id: Long,
        val featuredmedia_id: Long
)