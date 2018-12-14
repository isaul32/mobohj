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

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        val picasso = Picasso.get()
        if (BuildConfig.DEBUG) {
            picasso.isLoggingEnabled = true
        }

        picasso
                .load(BASE_URI + it)
                .into(view)
    } ?: run {
        view.setImageResource(R.color.goforeImagePlaceholder)
    }
}