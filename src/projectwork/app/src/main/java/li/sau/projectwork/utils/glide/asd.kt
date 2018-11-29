package li.sau.projectwork.utils.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class asd {
    init {
        Picasso.get()
                .load("asd")
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        // not being called the first time
                    }

                    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {

                    }

                    fun onBitmapFailed(errorDrawable: Drawable) {

                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                    }
                })
    }
}
