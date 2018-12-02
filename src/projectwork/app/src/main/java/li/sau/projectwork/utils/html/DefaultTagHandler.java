package li.sau.projectwork.utils.html;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.xml.sax.Attributes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import li.sau.projectwork.BuildConfig;

public class DefaultTagHandler implements TagHandler {
    private static final String TAG = DefaultTagHandler.class.getSimpleName();
    private static final float[] HEADING_SIZES = {
            2.0f, 1.5f, 1.17f, 1f, 0.83f, 0.67f
            // Defaults 1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f
    };

    private static final Pattern sTextAlignPattern = Pattern.compile(
            "(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b");
    private static final Pattern sForegroundColorPattern = Pattern.compile(
            "(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b");
    private static final Pattern sBackgroundColorPattern = Pattern.compile(
            "(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b");
    private static final Pattern sTextDecorationPattern = Pattern.compile(
            "(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b");

    private Context mContext;
    private Set<Target> mTargets = new HashSet<>();
    private TextView mTextView;
    private String mBaseUri;
    private Typeface mHeaderTypeface;
    private Typeface mBodyTextTypeface;


    public DefaultTagHandler(Context context,
                             TextView textView,
                             String baseUri,
                             Typeface headerTypeface,
                             Typeface bodyTextTypeface) {
        mContext = context;
        mTextView = textView;
        mBaseUri = baseUri;
        mHeaderTypeface = headerTypeface;
        mBodyTextTypeface = bodyTextTypeface;
    }

    @Override
    public void handleStartTag(SpannableStringBuilder spannableStringBuilder,
                               String tag,
                               Attributes attributes) {
        Log.d(TAG, "handleStartTag called for " + tag);

        // https://garygregory.wordpress.com/2015/11/03/java-lowercase-conversion-turkey/
        String tagLowered = tag.toLowerCase(Locale.ROOT);
        switch (tagLowered) {
            case "p":
                startBlockElement(spannableStringBuilder, attributes, 1);
                startCssStyle(spannableStringBuilder, attributes);
                startFont(spannableStringBuilder, mBodyTextTypeface);
                break;
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                int level = Character.getNumericValue(tag.charAt(1));
                startHeading(spannableStringBuilder, attributes, level);
                startFont(spannableStringBuilder, mHeaderTypeface);
                break;
            case "img":
                startImg(spannableStringBuilder, attributes);
                break;
            default:
                Log.d(TAG, "Skip " + tagLowered
                        + " start tag, because implementation is missing.");
                break;
        }
    }

    @Override
    public void handleEndTag(SpannableStringBuilder spannableStringBuilder, String tag) {
        Log.d(TAG, "handleEndTag called for " + tag);

        String tagLowered = tag.toLowerCase(Locale.ROOT);
        switch (tag.toLowerCase(Locale.ROOT)) {
            case "p":
                endFont(spannableStringBuilder);
                endCssStyle(spannableStringBuilder);
                endBlockElement(spannableStringBuilder);
                break;
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                endFont(spannableStringBuilder);
                endHeading(spannableStringBuilder);
                break;
            case "img":
                break;
            default:
                Log.d(TAG, "Skip " + tagLowered
                        + " end tag, because implementation is missing.");
                break;
        }
    }

    private void startBlockElement(Editable text, Attributes attributes, int margin) {
        if (margin > 0) {
            appendNewlines(text, margin);
            start(text, new TextUtils.Newline(margin));
        }
        String style = attributes.getValue("", "style");
        if (style != null) {
            Matcher m = sTextAlignPattern.matcher(style);
            if (m.find()) {
                String alignment = m.group(1);
                if (alignment.equalsIgnoreCase("start")) {
                    start(text, new TextUtils.Alignment(Layout.Alignment.ALIGN_NORMAL));
                } else if (alignment.equalsIgnoreCase("center")) {
                    start(text, new TextUtils.Alignment(Layout.Alignment.ALIGN_CENTER));
                } else if (alignment.equalsIgnoreCase("end")) {
                    start(text, new TextUtils.Alignment(Layout.Alignment.ALIGN_OPPOSITE));
                }
            }
        }
    }

    private void appendNewlines(Editable text, int minNewline) {
        final int len = text.length();
        if (len == 0) {
            return;
        }
        int existingNewlines = 0;
        for (int i = len - 1; i >= 0 && text.charAt(i) == '\n'; i--) {
            existingNewlines++;
        }
        for (int j = existingNewlines; j < minNewline; j++) {
            text.append("\n");
        }
    }

    private void start(Editable text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void startCssStyle(Editable text, Attributes attributes) {
        String style = attributes.getValue("", "style");
        if (style != null) {
            Matcher m = sTextDecorationPattern.matcher(style);
            if (m.find()) {
                String textDecoration = m.group(1);
                if (textDecoration.equalsIgnoreCase("line-through")) {
                    start(text, new TextUtils.Strikethrough());
                }
            }
        }
    }

    private void endCssStyle(Editable text) {
        TextUtils.Strikethrough s = getLast(text, TextUtils.Strikethrough.class);
        if (s != null) {
            setSpanFromMark(text, s, new StrikethroughSpan());
        }
    }

    private <T> T getLast(Spanned text, Class<T> kind) {
        // This knows that the last returned object from getSpans() will be the most recently added.
        T[] objects = text.getSpans(0, text.length(), kind);
        if (objects.length == 0) {
            return null;
        } else {
            return objects[objects.length - 1];
        }
    }

    private void setSpanFromMark(Spannable text, Object mark, Object... spans) {
        int where = text.getSpanStart(mark);
        text.removeSpan(mark);
        int len = text.length();
        if (where != len) {
            for (Object span : spans) {
                text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void endBlockElement(Editable text) {
        TextUtils.Newline n = getLast(text, TextUtils.Newline.class);
        if (n != null) {
            appendNewlines(text, n.mNumNewlines);
            text.removeSpan(n);
        }
        TextUtils.Alignment a = getLast(text, TextUtils.Alignment.class);
        if (a != null) {
            setSpanFromMark(text, a, new AlignmentSpan.Standard(a.mAlignment));
        }
    }

    private void startHeading(Editable text, Attributes attributes, int level) {
        startBlockElement(text, attributes, 1);
        start(text, new TextUtils.Heading(level - 1));
    }
    private void startFont(Editable text, Typeface typeface) {
        if (typeface != null) {
            start(text, new TextUtils.Font(typeface));
        }
    }

    private void endHeading(Editable text) {
        // RelativeSizeSpan and StyleSpan are CharacterStyles
        // Their ranges should not include the newlines at the end
        TextUtils.Heading h = getLast(text, TextUtils.Heading.class);
        if (h != null) {
            setSpanFromMark(text, h, new RelativeSizeSpan(HEADING_SIZES[h.mLevel]),
                    new StyleSpan(Typeface.BOLD));
        }
        endBlockElement(text);
    }

    private void endFont(Editable text) {
        TextUtils.Font font = getLast(text, TextUtils.Font.class);
        if (font != null) {
            if (font.mTypeface != null) {
                // Require API level 28
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    setSpanFromMark(text, font, new TypefaceSpan(font.mTypeface));
                }
            }
        }
    }

    private void startImg(Editable text, Attributes attributes) {
        final int viewWidth = mTextView.getWidth();

        // Calculate image size
        int width;
        int height;

        try {
            width = Integer.parseInt(attributes.getValue("", "width"));
            height = Integer.parseInt(attributes.getValue("", "height"));
        } catch (NumberFormatException e) {
            // If size is not set or valid, skip image
            // Todo: Try still load image and set it to original size
            return;
        }

        int distance = Math.abs(viewWidth - width);

        // Try find the best image version for view
        Map<Integer, String> options = new TreeMap<>();

        // Add default src option
        options.put(distance, getAbsUrl(attributes.getValue("", "src")));

        // Try get other src options
        String srcset = attributes.getValue("", "srcset");
        if (srcset != null) {

            String[] srcs = srcset.split(",[ ]*");
            for (String src : srcs) {
                String[] srcInfo = src.split("\\s+");
                if (srcInfo.length == 2) {
                    String imgWidthInfo = srcInfo[1];
                    int pos = imgWidthInfo.length() - 1;
                    if (imgWidthInfo.length() > 1
                            && imgWidthInfo.charAt(imgWidthInfo.length() - 1) == 'w') {
                        try {
                            int imgWidth = Integer.parseInt(imgWidthInfo.substring(0, pos));
                            int imgDistance = Math.abs(viewWidth - imgWidth);
                            String imgSrc = getAbsUrl(srcInfo[0]);

                            options.put(imgDistance, imgSrc);
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Invalid width", e);
                        }
                    }
                }
            }
        }

        String selectedSrc;
        Iterator<Map.Entry<Integer, String>> iterator = options.entrySet().iterator();
        if (iterator.hasNext()) {
            selectedSrc = iterator.next().getValue();
        } else {
            // There should be at least default value
            return;
        }

        final float ratio = (float) width / (float) height;
        final int resizedHeight = Math.round((float) viewWidth / ratio);

        // Make wrapper for the image
        final DrawableWrapper wrapper = new HtmlDrawableWrapper(new ColorDrawable(Color.RED));

        // Set bounds to right
        wrapper.setBounds(0, 0, viewWidth, resizedHeight);

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

        // Get picasso instance
        Picasso picasso = Picasso.get();

        if (BuildConfig.DEBUG) {
            picasso.setLoggingEnabled(true);
        }

        // Try load the image
        picasso
                .load(selectedSrc)
                .into(target);

        int len = text.length();
        text.append("\uFFFC");
        text.setSpan(new ImageSpan(wrapper, selectedSrc), len, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private String getAbsUrl(String url) {
        if (mBaseUri != null) {
            return mBaseUri + url;
        }

        return url;
    }

}
