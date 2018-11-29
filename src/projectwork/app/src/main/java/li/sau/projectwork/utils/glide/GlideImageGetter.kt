package li.sau.projectwork.utils.glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


@SuppressLint("RestrictedApi")
class GlideImageGetter(
        private val mContext: Context,
        private val mTextView: TextView
) : Html.ImageGetter {
    private val TAG by lazy { GlideImageGetter::class.java.simpleName }

    override fun getDrawable(src: String): Drawable? {
        Log.d(TAG, "Download image from: $src")

        val wrapper = DrawableWrapper()
        //val target = ImageGetterViewTarget(mTextView, wrapper)

        /*Glide.with(mContext)
                .load(src)
                .into(target)*/


        Picasso.get()
                .load(src)
                .placeholder(ColorDrawable(Color.RED))
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                        Log.d(TAG, "onBitmapLoaded called")
                        val drawable = BitmapDrawable(mContext.resources, bitmap)
                        val bounds = calculateBounds(drawable)
                        drawable.bounds = bounds
                        wrapper.bounds = bounds
                        wrapper.setDrawable(drawable)
                        //wrapper.invalidateSelf()
                        //mTextView.invalidate()
                        mTextView.invalidate()
                    }

                    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                        Log.d(TAG, "onBitmapFailed called")
                    }

                    fun onBitmapFailed(errorDrawable: Drawable) {
                        Log.d(TAG, "onBitmapFailed called")
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                        Log.d(TAG, "onPrepareLoad called")
                        //placeHolderDrawable.bounds = calculateBounds(placeHolderDrawable)
                        //wrapper.bounds = calculateBounds(placeHolderDrawable)
                        //wrapper.setDrawable(placeHolderDrawable)
                        //wrapper.invalidateSelf()
                        //mTextView.invalidate()
                    }
                })

        return wrapper
    }

    private fun calculateBounds(drawable: Drawable): Rect {
        if (drawable.intrinsicWidth > 100) {
            val width: Float
            val height: Float
            Log.d(TAG, "Image width is " + drawable.intrinsicWidth)
            Log.d(TAG, "View width is " + mTextView.width)
            if (drawable.intrinsicWidth >= mTextView.width) {
                val downScale = drawable.intrinsicWidth.toFloat() / mTextView.width.toFloat()
                width = drawable.intrinsicWidth.toFloat() / downScale
                height = drawable.intrinsicHeight.toFloat() / downScale
            } else {
                val multiplier = mTextView.width.toFloat() / drawable.intrinsicWidth.toFloat()
                width = drawable.intrinsicWidth.toFloat() * multiplier
                height = drawable.intrinsicHeight.toFloat() * multiplier
            }
            Log.d(TAG, "New Image width is $width")


            return Rect(0, 0, Math.round(width), Math.round(height))
        } else {
            return Rect(0, 0, drawable.intrinsicWidth * 2, drawable.intrinsicHeight * 2)
        }
    }


    /*class ImageGetterViewTarget(
            mView: TextView,
            private val mWrapper: DrawableWrapper
    ) : CustomViewTarget<TextView, Drawable>(mView) {
        private val TAG by lazy { ImageGetterViewTarget::class.java.simpleName }
        private val mTargets = mutableSetOf<ImageGetterViewTarget>()

        init {
            mTargets.add(this)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            Log.d(TAG, "onLoadFailed called")
        }

        override fun onResourceCleared(placeholder: Drawable?) {
            Log.d(TAG, "onResourceCleared called")
        }

        override fun onResourceReady(drawable: Drawable, transition: Transition<in Drawable>?) {
            Log.d(TAG, "onResourceReady called")

            setDrawable(drawable)
        }

        private fun setDrawable(drawable: Drawable) {
            //val drawable = resource ?: ColorDrawable(Color.RED)
            val rect = calculateBounds(drawable)
            drawable.bounds = rect
            mWrapper.bounds = rect
            mWrapper.setDrawable(drawable)

            getView().invalidate()
        }

        // Scale image to same width as view
        private fun calculateBounds(drawable: Drawable): Rect {
            if (drawable.intrinsicWidth > 100) {
                val width: Float
                val height: Float
                Log.d(TAG, "Image width is " + drawable.intrinsicWidth)
                Log.d(TAG, "View width is " + view.width)
                if (drawable.intrinsicWidth >= getView().width) {
                    val downScale = drawable.intrinsicWidth.toFloat() / getView().width.toFloat()
                    width = drawable.intrinsicWidth.toFloat() / downScale
                    height = drawable.intrinsicHeight.toFloat() / downScale
                } else {
                    val multiplier = getView().width.toFloat() / drawable.intrinsicWidth.toFloat()
                    width = drawable.intrinsicWidth.toFloat() * multiplier
                    height = drawable.intrinsicHeight.toFloat() * multiplier
                }
                Log.d(TAG, "New Image width is $width")


                return Rect(0, 0, Math.round(width), Math.round(height))
            } else {
                return Rect(0, 0, drawable.intrinsicWidth * 2, drawable.intrinsicHeight * 2)
            }
        }
    }*/
}
