package li.sau.projectwork.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_about.*
import li.sau.projectwork.R
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.utils.html.Html
import li.sau.projectwork.utils.html.impl.DefaultTagHandler

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { context ->
            val htmlToSpanned = DefaultTagHandler(activity, htmlView,
                    BASE_URI,
                    ResourcesCompat.getFont(context, R.font.lato),
                    ResourcesCompat.getFont(context, R.font.aleo))

            htmlView.text = Html.fromHtml(resources.getString(R.string.about_text), htmlToSpanned)
        }
        
    }
}
