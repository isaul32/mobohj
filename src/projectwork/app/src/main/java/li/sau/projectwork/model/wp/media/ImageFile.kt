package li.sau.projectwork.model.wp.media

data class ImageFile (
    val file: String,
    val height: Long,
    val width: Long,
    val mime_type: String,
    val source_url: String
)