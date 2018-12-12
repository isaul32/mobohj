package li.sau.projectwork.model.wp.blog

import com.squareup.moshi.Json

data class Embedded(
        @Json(name = "wp:featuredmedia")
        val featuredmedia: List<Featuredmedia> = emptyList()
)