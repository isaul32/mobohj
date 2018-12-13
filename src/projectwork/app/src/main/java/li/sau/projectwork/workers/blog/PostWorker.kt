package li.sau.projectwork.workers.blog

import android.content.Context
import android.util.Log
import androidx.work.*
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.data.wp.WordPressAPICalls
import li.sau.projectwork.model.wp.blog.Post
import java.io.IOException

class PostWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { PostWorker::class.java.simpleName }

    override fun doWork(): Result {
        try {
            val res = WordPressAPICalls.getPosts().execute()
            if (res.isSuccessful) {
                val blogPosts = res.body()

                blogPosts?.let { posts ->
                    val database = AppDatabase.getInstance(applicationContext)

                    database.postFeaturedmediaJoinDao().insertAllWithMedia(posts)

                    val data = mapOf("posts" to posts.map(Post::id).toLongArray())

                    // Have to use putAll because put is restrict to group
                    return Result.success(Data.Builder()
                            .putAll(data)
                            .build())
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.localizedMessage, e)
        }

        return Result.failure()
    }
}
