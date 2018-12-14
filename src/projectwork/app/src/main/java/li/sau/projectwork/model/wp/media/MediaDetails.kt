package li.sau.projectwork.model.wp.media

data class MediaDetails(
        val file: String,
        val width: Long,
        val height: Long,
        val sizes: MediaDetailsSizes
)