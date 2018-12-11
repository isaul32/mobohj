package li.sau.projectwork.utils.html.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashSet;
import java.util.Set;

import li.sau.projectwork.utils.html.ImageGetter;
import li.sau.projectwork.utils.html.impl.utils.HtmlDrawableWrapper;

public class PicassoImageGetter implements ImageGetter {

    private Context mContext;
    private TextView mTextView;
    private Picasso mPicasso;
    private Set<Target> mTargets = new HashSet<>(); // Prevent garbage collector to clean targets

    public PicassoImageGetter(
            Context context,
            Picasso picasso,
            TextView textView
    ) {
        this.mContext = context;
        this.mPicasso = picasso;
        this.mTextView = textView;
    }

    @Override
    public Drawable getDrawable(String source, int width, int height) {
        // Make wrapper for the image
        final DrawableWrapper wrapper = new HtmlDrawableWrapper(new ColorDrawable(Color.parseColor("#EAEAEA")));

        // Set bounds to right
        wrapper.setBounds(0, 0, width, height);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(),
                        bitmap);

                // Set drawable and render again
                wrapper.setDrawable(drawable);

                // Refresh view
                mTextView.setText(mTextView.getText());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        mTargets.add(target);

        // Try load the image
        mPicasso
                .load(source)
                .into(target);

        return wrapper;

        /*Log.d(PicassoImageGetter.class.getName(), "Start loading url " + source);

        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();

        mPicasso
                .load(source)
                //.error(R.drawable.connection_error)
                .into(drawable);

        return drawable;*/
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                checkBounds();
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            if (drawable != null) {
                this.drawable = drawable;
                checkBounds();
            }
        }

        private void checkBounds() {
            float defaultProportion = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
            int width = Math.min(mTextView.getWidth(), drawable.getIntrinsicWidth());
            int height = (int) ((float) width / defaultProportion);

            if (getBounds().right != mTextView.getWidth() || getBounds().bottom != height) {

                setBounds(0, 0, mTextView.getWidth(), height); //set to full width

                int halfOfPlaceHolderWidth = (int) ((float) getBounds().right / 2f);
                int halfOfImageWidth = (int) ((float) width / 2f);

                drawable.setBounds(
                        halfOfPlaceHolderWidth - halfOfImageWidth, //centering an image
                        0,
                        halfOfPlaceHolderWidth + halfOfImageWidth,
                        height);

                mTextView.setText(mTextView.getText()); //refresh text
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            setDrawable(placeHolderDrawable);
        }

    }
}