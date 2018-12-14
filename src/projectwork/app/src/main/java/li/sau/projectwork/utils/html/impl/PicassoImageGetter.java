package li.sau.projectwork.utils.html.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import li.sau.projectwork.utils.html.ImageGetter;
import li.sau.projectwork.utils.html.impl.utils.HtmlDrawableWrapper;

public class PicassoImageGetter implements ImageGetter {

    private static final String TAG = PicassoImageGetter.class.getSimpleName();

    private Context mContext;
    private TextView mTextView;
    private Picasso mPicasso;
    //private Set<Target> mTargets = new HashSet<>(); // Prevent garbage collector to clean targets

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

        /*Target target = new Target() {
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
                .into(target);*/

        try {
            Bitmap bitmap = mPicasso
                    .load(source)
                    .get();
            BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
            wrapper.setDrawable(drawable);
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return wrapper;
    }

}