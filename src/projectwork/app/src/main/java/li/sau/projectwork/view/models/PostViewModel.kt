package li.sau.projectwork.view.models

import android.text.Spanned
import androidx.lifecycle.ViewModel
import li.sau.projectwork.utils.html.Html

data class PostViewModel(
        val id: Long,
        val title: String = "Title placeholder",
        val author: String? = "Author placeholder",
        val imageUrl: String? = "https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-05.jpg"
) : ViewModel() {

    fun getTitle(): Spanned {
        return Html.fromHtml(title)
    }

    fun getAuthor(): Spanned {
        return Html.fromHtml(author)
    }

}