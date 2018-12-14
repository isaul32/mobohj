package li.sau.projectwork.view.models

import android.text.Spanned
import androidx.lifecycle.ViewModel
import li.sau.projectwork.utils.html.Html

data class PostViewModel(
        val id: Long,
        val title: String = "Title placeholder",
        val author: String? = "Author placeholder",
        val imageUrl: String? = "https://cdn-images-1.medium.com/max/1000/1*vdsHPzUebwn3Y_M6hvcEOg.jpeg"
) : ViewModel() {

    fun getTitle(): Spanned {
        return Html.fromHtml(title, null, null)
    }

    fun getAuthor(): Spanned {
        return Html.fromHtml(author, null, null)
    }
    
}