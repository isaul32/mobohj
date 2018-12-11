package li.sau.projectwork.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.databinding.BindingAdapter

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