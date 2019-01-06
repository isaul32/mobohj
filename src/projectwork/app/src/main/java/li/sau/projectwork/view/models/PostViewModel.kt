package li.sau.projectwork.view.models

import android.text.Spanned
import androidx.lifecycle.ViewModel
import li.sau.projectwork.utils.html.Html

data class PostViewModel(
        val id: Long,
        val title: String,
        val author: String?,
        val imageUrl: String?
) : ViewModel() {

    fun getTitle(): Spanned {
        return Html.fromHtml(title)
    }

    fun getAuthor(): Spanned {
        return Html.fromHtml(author)
    }

}