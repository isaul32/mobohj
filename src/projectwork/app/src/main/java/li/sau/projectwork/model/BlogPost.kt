package li.sau.projectwork.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlogPost(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val link: String
)
