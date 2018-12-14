package li.sau.projectwork.model.wp

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
        val id: Long,
        val name: String,
        val url: String,
        val description: String,
        val link: String,
        val slug: String
)
