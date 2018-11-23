package li.sau.projectwork.data

class BlogPostRepository private constructor(private val blogPostDao: BlogPostDao) {
    companion object {

        @Volatile private var instance: BlogPostRepository? = null

        fun getInstance(blogPostDao: BlogPostDao) =
                instance ?: synchronized(this) {
                    instance ?: BlogPostRepository(blogPostDao).also { instance = it }
                }

    }
}