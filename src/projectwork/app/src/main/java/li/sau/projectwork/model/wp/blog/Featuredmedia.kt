package li.sau.projectwork.model.wp.blog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Featuredmedia(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val date: String,
        //val media_details: MediaDetails,

        @ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["embedded_id"])
        val embedded_id: Long?
)