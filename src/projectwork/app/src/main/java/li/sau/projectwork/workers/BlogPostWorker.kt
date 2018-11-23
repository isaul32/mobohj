package li.sau.projectwork.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.data.WordPressAPICalls
import li.sau.projectwork.model.BlogPost
import java.io.IOException

class BlogPostWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG by lazy { BlogPostWorker::class.java.simpleName }

    override fun doWork(): ListenableWorker.Result {
        try {
            val res = WordPressAPICalls.getBlogPosts().execute()
            if (res.isSuccessful) {
                val blogPosts = res.body()

                blogPosts?.let {
                    val database = AppDatabase.getInstance(applicationContext)
                    database.blogPostDao().insertAll(it)

                    val data = mapOf("posts" to it.map(BlogPost::id).toLongArray())

                    // Have to use putAll because put is restrict to group
                    outputData = Data.Builder()
                            .putAll(data)
                            .build()
                }
            }
        } catch (ex: IOException) {
            Log.e(TAG, ex.localizedMessage, ex)
            return Result.FAILURE
        }

        return Result.SUCCESS
    }


}
