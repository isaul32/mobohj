package li.sau.projectwork.model.wp.blog

import com.squareup.moshi.Json

data class Embedded(
        //@field:Json(name = "wp:featuredmedia")
        @Json(name = "wp:featuredmedia")
        val featuredmedia: List<Featuredmedia> = emptyList()
        /*val id: Long,
        @Relation(parentColumn = "post_id",
                entityColumn = "embedded_id",
                entity = Featuredmedia::class)
        @Json(name = "wp:featuredmedia")
        val featuredmedia: List<Featuredmedia>,

        val post_id: Long*/
)