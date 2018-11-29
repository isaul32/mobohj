package li.sau.projectwork.utils.glide

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

class DrawableWrapper : Drawable(), Drawable.Callback {

    private var mDrawable: Drawable? = null

    override fun draw(canvas: Canvas) {
        mDrawable?.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        mDrawable?.alpha = alpha
    }

    override fun getOpacity(): Int {
        return mDrawable?.opacity ?: 0
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mDrawable?.colorFilter = colorFilter
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        callback?.unscheduleDrawable(who, what)
    }

    override fun invalidateDrawable(who: Drawable) {
        callback?.invalidateDrawable(who)
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        callback?.scheduleDrawable(who, what, `when`)
    }

    fun setDrawable(drawable: Drawable) {
        mDrawable?.callback = null
        mDrawable = drawable
        drawable.callback= this
    }
}