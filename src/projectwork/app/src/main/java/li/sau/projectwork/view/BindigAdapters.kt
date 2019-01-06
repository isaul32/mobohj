package li.sau.projectwork.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import li.sau.projectwork.BuildConfig
import li.sau.projectwork.R
import li.sau.projectwork.utils.BASE_URI
import li.sau.projectwork.view.models.PostViewModel

@BindingAdapter("fadeVisible")
fun setFadeVisible(view: View, visible: Boolean) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()

        if (visible) {
            view.visibility = View.VISIBLE
            view.alpha = 0f
            view.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.alpha = 1f
                }
            })
        } else {
            view.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.alpha = 1f
                    view.visibility = View.GONE
                }
            })
        }
    }
}

val goforecrewPatterns = arrayOf(
        //"https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-01.jpg",
        //"https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-02.jpg",
        "https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-03.jpg"
        //"https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-04.jpg",
        //"https://gofore.com/wp-content/themes/gofore/elements/img/goforecrew_pattern-05.jpg"
)

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, model: PostViewModel) {
    val picasso = Picasso.get()
    if (BuildConfig.DEBUG) {
        picasso.isLoggingEnabled = true
    }

    model.imageUrl?.let {
        picasso
                .load(BASE_URI + it)
                .into(view)
    } ?: run {

        view.setImageResource(R.color.goforeImagePlaceholder)
        picasso
                .load(goforecrewPatterns[(model.id % goforecrewPatterns.size).toInt()])
                .into(view)
    }
}