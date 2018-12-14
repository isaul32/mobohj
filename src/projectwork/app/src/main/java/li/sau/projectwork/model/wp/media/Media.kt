package li.sau.projectwork.model.wp.media

data class Media(
        val id: Long,
        val date: String,
        val media_details: MediaDetails,
        val source_url: String
)