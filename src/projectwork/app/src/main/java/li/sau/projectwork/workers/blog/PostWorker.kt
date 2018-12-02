package li.sau.projectwork.workers.blog

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.data.wp.WordPressAPICalls
import li.sau.projectwork.model.blog.Post
import java.io.IOException

class PostWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { PostWorker::class.java.simpleName }

    override fun doWork(): ListenableWorker.Result {
        try {
            val res = WordPressAPICalls.getBlogPosts().execute()
            if (res.isSuccessful) {
                val blogPosts = res.body()

                blogPosts?.let { posts ->
                    val database = AppDatabase.getInstance(applicationContext)

                    database.blogPostDao().insertAll(posts)

                    val data = mapOf("posts" to posts.map(Post::id).toLongArray())

                    // Have to use putAll because put is restrict to group
                    outputData = Data.Builder()
                            .putAll(data)
                            .build()
                    return Result.SUCCESS
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.localizedMessage, e)
        }

        return Result.FAILURE
    }
}
