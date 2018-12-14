package li.sau.projectwork.utils.html.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import org.xml.sax.Attributes;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import li.sau.projectwork.R;
import li.sau.projectwork.utils.html.ImageGetter;
import li.sau.projectwork.utils.html.TagHandler;
import li.sau.projectwork.utils.html.impl.utils.TextUtils;

public class DefaultTagHandler implements TagHandler {
    private static final String TAG = DefaultTagHandler.class.getSimpleName();
    private static final float[] HEADING_SIZES = {
            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f
            // HTML defaults 2.0f, 1.5f, 1.17f, 1f, 0.83f, 0.67f
            // Android defaults 1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f
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
                               ImageGetter imageGetter,
                               String tag,
                               Attributes attributes) {
        Log.d(TAG, "handleStartTag called for " + tag);

        // https://garygregory.wordpress.com/2015/11/03/java-lowercase-conversion-turkey/
        String tagLowered = tag.toLowerCase(Locale.ROOT);
        switch (tagLowered) {
            case "p":
                startBlockElement(spannableStringBuilder, attributes, 1);
                startCssStyle(spannableStringBuilder, attributes);
                startFont(spannableStringBuilder, "serif", mBodyTextTypeface);
                break;
            case "ul":
                startBlockElement(spannableStringBuilder, attributes, 1);
                break;
            case "li":
                startLi(spannableStringBuilder, attributes);
                break;
            case "a":
                startA(spannableStringBuilder, attributes);
                break;
            case "strong":
            case "b":
                start(spannableStringBuilder, new TextUtils.Bold());
                break;
            case "em":
            case "cite":
            case "dfn":
            case "i":
                start(spannableStringBuilder, new TextUtils.Italic());
                break;
            case "big":
                start(spannableStringBuilder, new TextUtils.Big());
                break;
            case "small":
                start(spannableStringBuilder, new TextUtils.Small());
                break;
            case "u":
                start(spannableStringBuilder, new TextUtils.Underline());
                break;
            case "del":
            case "s":
            case "strike":
                start(spannableStringBuilder, new TextUtils.Strikethrough());
                break;
            case "sup":
                start(spannableStringBuilder, new TextUtils.Super());
                break;
            case "sub":
                start(spannableStringBuilder, new TextUtils.Sub());
                break;
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                int level = Character.getNumericValue(tag.charAt(1));
                startHeading(spannableStringBuilder, attributes, level);
                startFont(spannableStringBuilder, "sans-serif", mHeaderTypeface);
                break;
            case "img":
                startImg(spannableStringBuilder, attributes, imageGetter);
                break;
            case "html":
            case "body":
            case "br":
            case "div":
            case "span":
                // Skipped
                break;
            default:
                Log.w(TAG, "Skip " + tagLowered
                        + " start tag, because implementation is missing.");
                break;
        }
    }

    @Override
    public void handleEndTag(SpannableStringBuilder spannableStringBuilder, String tag) {
        Log.d(TAG, "handleEndTag called for " + tag);

        String tagLowered = tag.toLowerCase(Locale.ROOT);
        switch (tag.toLowerCase(Locale.ROOT)) {
            case "br":
                spannableStringBuilder.append("\n");
                break;
            case "p":
                endFont(spannableStringBuilder);
                endCssStyle(spannableStringBuilder);
                endBlockElement(spannableStringBuilder);
                break;
            case "ul":
                endBlockElement(spannableStringBuilder);
                break;
            case "li":
                endLi(spannableStringBuilder);
                break;
            case "a":
                endA(spannableStringBuilder);
                break;
            case "strong":
            case "b":
                end(spannableStringBuilder, TextUtils.Bold.class, new StyleSpan(Typeface.BOLD));
                break;
            case "em":
            case "cite":
            case "dfn":
            case "i":
                end(spannableStringBuilder, TextUtils.Italic.class, new StyleSpan(Typeface.ITALIC));
                break;
            case "big":
                end(spannableStringBuilder, TextUtils.Big.class, new RelativeSizeSpan(1.25f));
                break;
            case "small":
                end(spannableStringBuilder, TextUtils.Small.class, new RelativeSizeSpan(0.8f));
                break;
            case "u":
                end(spannableStringBuilder, TextUtils.Underline.class, new UnderlineSpan());
                break;
            case "del":
            case "s":
            case "strike":
                end(spannableStringBuilder, TextUtils.Strikethrough.class, new StrikethroughSpan());
                break;
            case "sup":
                end(spannableStringBuilder, TextUtils.Super.class, new SuperscriptSpan());
                break;
            case "sub":
                end(spannableStringBuilder, TextUtils.Sub.class, new SubscriptSpan());
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
            case "html":
            case "body":
            case "div":
            case "span":
                // Skipped
                break;
            default:
                Log.w(TAG, "Skip " + tagLowered
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

    private void startLi(Editable text, Attributes attributes) {
        startBlockElement(text, attributes, 1);
        start(text, new TextUtils.Bullet());
        startCssStyle(text, attributes);
    }

    private void startA(Editable text, Attributes attributes) {
        String href = attributes.getValue("", "href");
        start(text, new TextUtils.Href(href));
    }

    private void endCssStyle(Editable text) {
        TextUtils.Strikethrough s = getLast(text, TextUtils.Strikethrough.class);
        if (s != null) {
            setSpanFromMark(text, s, new StrikethroughSpan());
        }
    }

    private void endLi(Editable text) {
        endCssStyle(text);
        endBlockElement(text);
        int bulletColor = mContext.getColor(R.color.goforePrimary1);
        float density = mContext.getResources().getDisplayMetrics().density;
        int gapWidth = Math.round((float) 80 / density);
        int bulletRadius = Math.round((float) 30 / density);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            end(text, TextUtils.Bullet.class, new BulletSpan(gapWidth, bulletColor, bulletRadius));
        } else {
            end(text, TextUtils.Bullet.class, new BulletSpan(gapWidth, bulletColor));
        }
    }

    private void endA(Editable text) {
        TextUtils.Href h = getLast(text, TextUtils.Href.class);
        if (h != null) {
            if (h.mHref != null) {
                setSpanFromMark(text, h, new URLSpan((h.mHref)));
            }
        }
    }

    private void end(Editable text, Class kind, Object repl) {
        Object obj = getLast(text, kind);
        if (obj != null) {
            setSpanFromMark(text, obj, repl);
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
    private void startFont(Editable text, String family, Typeface typeface) {
        if (typeface != null) {
            start(text, new TextUtils.Font(family, typeface));
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
                } else {
                    setSpanFromMark(text, font, new TypefaceSpan(font.mFamily));
                }
            }
        }
    }

    private void startImg(
            Editable text,
            Attributes attributes,
            ImageGetter imageGetter
    ) {
        int viewWidth = mTextView.getWidth();

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
            Log.w(TAG, "Image source is missing");
            return;
        }

        if (width <= 0 || height <= 0 || viewWidth == 0) {
            // Sizes are not ok
            Log.w(TAG, "Image sizes are wrong");
            return;
        }

        float ratio = (float) width / (float) height;
        int resizedHeight = Math.round((float) viewWidth / ratio);

        if (imageGetter != null) {
            Drawable drawable = imageGetter.getDrawable(selectedSrc, viewWidth, resizedHeight);

            int len = text.length();
            text.append("\uFFFC");
            text.setSpan(new ImageSpan(drawable, selectedSrc),
                    len,
                    text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private String getAbsUrl(String url) {
        if (mBaseUri != null) {
            return mBaseUri + url;
        }

        return url;
    }

}
