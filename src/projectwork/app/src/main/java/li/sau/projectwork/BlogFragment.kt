package li.sau.projectwork

import android.os.Bundle
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.fragment_blog.*
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.DefaultTagHandler
import li.sau.projectwork.utils.html.Html
import li.sau.projectwork.workers.blog.PostWorker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BlogFragment : Fragment() {

    private val TAG = BlogFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val work = OneTimeWorkRequest.Builder(PostWorker::class.java).build()
        WorkManager.getInstance().enqueue(work)

        WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
                .observe(viewLifecycleOwner, Observer { status ->
                    if (status != null && status.state.isFinished) {
                        renderBlog()
                    }
                })
    }

    private fun renderBlog() {

        val context = activity?.applicationContext

        context?.let {
            val database = AppDatabase.getInstance(it)

            database.blogPostDao().getAll().observe(viewLifecycleOwner, Observer { posts ->
                val post = posts[6]
                val title = post.title.rendered

                val htmlToSpanned = DefaultTagHandler(context, html_view,
                        BASE_URI,
                        ResourcesCompat.getFont(context, R.font.lato),
                        ResourcesCompat.getFont(context, R.font.aleo))
                val sb = StringBuilder()

                // Add title
                sb.append("<h1>$title</h1>")
                sb.append("\n")

                // Try add date
                try {
                    val dateFormatGmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                            Locale.ENGLISH)
                    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
                    val date = dateFormatGmt.parse(post.date_gmt)

                    // Use system locale
                    val formattedDate = DateFormat.getDateFormat(context).format(date)
                    sb.append("<h6>$formattedDate</h6>")
                    sb.append("\n")

                } catch (e: ParseException) {
                    Log.w(TAG, "Could not parse date: " + post.date)
                }

                // Add excerpt
                /*
                val excerpt = post.excerpt.rendered
                sb.append(excerpt)
                */

                // add content
                val content = post.content.rendered
                sb.append(content)

                html_view.text = Html.fromHtml(sb.toString(), htmlToSpanned)
                html_view.movementMethod = LinkMovementMethod()
            })

        }
    }
}
