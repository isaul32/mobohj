package li.sau.projectwork.data.blog

class PostRepository private constructor(private val postDao: PostDao) {

    fun getPosts() = postDao.getAll()

    companion object {

        @Volatile private var instance: PostRepository? = null

        fun getInstance(postDao: PostDao) =
                instance ?: synchronized(this) {
                    instance
                            ?: PostRepository(postDao).also { instance = it }
                }

    }

}