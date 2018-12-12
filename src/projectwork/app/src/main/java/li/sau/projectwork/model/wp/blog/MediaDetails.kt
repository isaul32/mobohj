package li.sau.projectwork.model.wp.blog

import androidx.room.Embedded

data class MediaDetails(
        val file: String,
        val width: Long,
        val height: Long,
        @Embedded
        val sizes: MediaDetailsSizes
)