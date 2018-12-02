package li.sau.projectwork.model.wp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag (
        @PrimaryKey(autoGenerate = false)
        val id: Long,
        val count: Long,
        val description: String,
        val link: String,
        val name: String,
        val slug: String,
        val taxonomy: String
        // val meta
)