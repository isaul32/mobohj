package li.sau.projectwork.model.wp.blog

import androidx.room.Embedded

data class MediaDetailsSizes (
    @Embedded(prefix = "full_")
    val full: ImageFile?,
    @Embedded(prefix = "large_")
    val large: ImageFile?,
    @Embedded(prefix = "medium_")
    val medium: ImageFile?,
    @Embedded(prefix = "thumbnail_")
    val thumbnail: ImageFile?
)