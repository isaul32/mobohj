package li.sau.projectwork.model.wp.blog

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Featuredmedia(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val date: String,
        @Embedded(prefix = "details_")
        val media_details: MediaDetails,
        @ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["embedded_id"])
        var embedded_id: Long?
)