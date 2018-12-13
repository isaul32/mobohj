package li.sau.projectwork.model.wp.blog

import com.squareup.moshi.Json

data class Embedded(
        val author: List<Author> = emptyList(),
        @Json(name = "wp:featuredmedia")
        val featuredmedia: List<Featuredmedia> = emptyList()
)