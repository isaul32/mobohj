package li.sau.projectwork.workers.blog

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.rest.WordPressAPICalls
import java.io.IOException

class PostWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val TAG by lazy { PostWorker::class.java.simpleName }

    override fun doWork(): Result {
        try {
            val res = WordPressAPICalls.getPosts(10).execute()
            if (res.isSuccessful) {
                val blogPosts = res.body()

                blogPosts?.let { posts ->

                    posts.forEach { post ->
                        // Get media for post
                        try {
                            val mediaRes = WordPressAPICalls
                                    .getMedia(post.featured_media)
                                    .execute()

                            if (mediaRes.isSuccessful) {
                                val media = mediaRes.body()
                                post.thumbnail_url = media?.media_details?.sizes?.medium?.source_url
                            }
                        } catch (e: IOException) {
                            Log.e(TAG, e.localizedMessage, e)
                        }

                        // Get author for post
                        try {
                            val userRes = WordPressAPICalls
                                    .getUser(post.author)
                                    .execute()
                            if (userRes.isSuccessful) {
                                val author = userRes.body()
                                post.author_name = author?.name
                            }
                        } catch (e: IOException) {
                            Log.e(TAG, e.localizedMessage, e)
                        }
                    }


                    val database = AppDatabase.getInstance(applicationContext)

                    database.postDao().insertAll(posts)

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
