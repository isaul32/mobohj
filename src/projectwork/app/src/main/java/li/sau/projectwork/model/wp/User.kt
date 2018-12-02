package li.sau.projectwork.model.wp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val name: String,
        val url: String,
        val description: String,
        val link: String,
        val slug: String
        // val avatar_urls
        // val meta
)
