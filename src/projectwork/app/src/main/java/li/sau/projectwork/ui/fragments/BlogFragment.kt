package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogBinding
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.impl.DefaultTagHandler
import li.sau.projectwork.workers.blog.PostWorker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.view.animation.AnimationUtils
import com.squareup.picasso.Picasso
import li.sau.projectwork.BuildConfig
import li.sau.projectwork.R
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.TextViewModel
import li.sau.projectwork.utils.html.impl.PicassoImageGetter
import li.sau.projectwork.utils.html.Html


class BlogFragment : Fragment() {

    private val TAG = BlogFragment::class.java.simpleName
    private lateinit var mBinding: FragmentBlogBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentBlogBinding.inflate(inflater, container, false)

        // Add model to binding
        val model = TextViewModel()
        model.loading.set(true)
        mBinding.model = model

        return mBinding.root
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

            /*val fadeIn = AnimationUtils.loadAnimation(it,
                    android.R.anim.fade_in)
            val fadeOut = AnimationUtils.loadAnimation(it,
                    android.R.anim.fade_out)

            val database = AppDatabase.getInstance(it)

            database.blogPostDao().getAll().observe(viewLifecycleOwner, Observer { posts ->
                val post = posts[6]
                val htmlToSpanned = DefaultTagHandler(context, mBinding.htmlView,
                        BASE_URI,
                        ResourcesCompat.getFont(context, R.font.lato),
                        ResourcesCompat.getFont(context, R.font.aleo))

                val picasso = Picasso.get()
                if (BuildConfig.DEBUG) {
                    picasso.isLoggingEnabled = true
                }

                val imageGetter = PicassoImageGetter(context, picasso, mBinding.htmlView)

                val html = Html.fromHtml(buildHtmlTextFromPost(post), imageGetter, htmlToSpanned)
                mBinding.htmlView.text = html
                mBinding.model?.loading?.set(false)
            })*/

        }
    }

    private fun buildHtmlTextFromPost(post: Post): String {
        val sb = StringBuilder()
        val title = post.title.rendered

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

        return sb.toString()
    }

}
