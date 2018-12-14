package li.sau.projectwork.ui.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.text.Spanned
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import li.sau.projectwork.data.AppDatabase
import li.sau.projectwork.databinding.FragmentBlogBinding
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.impl.DefaultTagHandler
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.squareup.picasso.Picasso
import li.sau.projectwork.BuildConfig
import li.sau.projectwork.R
import li.sau.projectwork.model.wp.blog.Post
import li.sau.projectwork.view.models.TextViewModel
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

    override fun onPause() {
        super.onPause()

        // Prevent scrolling while doing fragment transition
        mBinding.scrollView.fling(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderBlog()
    }

    private fun renderBlog() {
        activity?.applicationContext?.let { context ->

            val postId = BlogFragmentArgs.fromBundle(arguments).postId.toLong()

            val database = AppDatabase.getInstance(context)
            database.postDao().get(postId).observe(viewLifecycleOwner, Observer { post ->
                DoAsync {
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

                    html
                }.execute()
            })
        }
    }

    private fun buildHtmlTextFromPost(post: Post): String {
        val sb = StringBuilder()

        // Add subtitle
        val subtitle = post.wps_subtitle
        if (subtitle.isNotBlank()) {
            sb.append("<h6>$subtitle</h6>")
            sb.append("\n")
        }

        // Add title
        val title = post.title.rendered
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

        // Add author
        val author = post.author_name
        author?.let {
            sb.append("<h6>")
            sb.append(it.toUpperCase())
            sb.append("</h6>")
            sb.append("\n")
        }

        // add content
        val content = post.content.rendered
        sb.append(content)

        return sb.toString()
    }

    @SuppressLint("StaticFieldLeak")
    inner class DoAsync(val handler: () -> Spanned) : AsyncTask<Void, Void, Spanned>() {
        override fun doInBackground(vararg params: Void?): Spanned? {
            Thread.sleep(700)
            return handler()
        }

        override fun onPostExecute(result: Spanned?) {
            mBinding.htmlView.text = result
            mBinding.model?.loading?.set(false)
            mBinding.htmlView.movementMethod = LinkMovementMethod()
        }
    }

}
