package li.sau.projectwork.workers.blog

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.data.WordPressAPICalls
import li.sau.projectwork.model.blog.Post
import li.sau.projectwork.utils.BASE_URI
import org.jsoup.Jsoup
import org.jsoup.safety.Cleaner
import org.jsoup.safety.Whitelist
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

                    posts.forEach {
                        it.content.cleaned = convertHTML(it.content.rendered)
                        it.excerpt.cleaned = convertHTML(it.excerpt.rendered)
                        it.guid.cleaned = convertHTML(it.guid.rendered)
                        it.title.cleaned = convertHTML(it.title.rendered)
                    }

                    database.blogPostDao().insertAll(posts)

                    val data = mapOf("posts" to posts.map(Post::id).toLongArray())

                    // Have to use putAll because put is restrict to group
                    outputData = Data.Builder()
                            .putAll(data)
                            .build()
                    return Result.SUCCESS
                }
            }
        } catch (ex: IOException) {
            Log.e(TAG, ex.localizedMessage, ex)
        }

        return Result.FAILURE
    }

    fun convertHTML(html: String): String {
        val dirty = Jsoup.parseBodyFragment(html, BASE_URI)
        val cleaner = Cleaner(Whitelist.relaxed().preserveRelativeLinks(true))
        val clean = cleaner.clean(dirty)

        for (img in clean.select("img[src]")) {
            val src = img.attr("src")
            val abs = img.absUrl("src")
            img.attr("src", abs)
        }


        return clean.body().html().toString()
    }
}
