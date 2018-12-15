package li.sau.projectwork.utils.html.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import li.sau.projectwork.utils.html.ImageGetter;
import li.sau.projectwork.utils.html.impl.utils.HtmlDrawableWrapper;

public class SynchronousPicassoImageGetter implements ImageGetter {

    private static final String TAG = SynchronousPicassoImageGetter.class.getSimpleName();

    private Context mContext;
    private Picasso mPicasso;

    public SynchronousPicassoImageGetter(
            Context context,
            Picasso picasso
    ) {
        this.mContext = context;
        this.mPicasso = picasso;
    }

    @Override
    public Drawable getDrawable(String source, int width, int height) {
        // Make wrapper for the image
        final DrawableWrapper wrapper = new HtmlDrawableWrapper(new ColorDrawable(Color.parseColor("#EAEAEA")));

        // Set bounds to right
        wrapper.setBounds(0, 0, width, height);

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