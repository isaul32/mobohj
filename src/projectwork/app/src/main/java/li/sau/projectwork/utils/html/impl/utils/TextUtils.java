package li.sau.projectwork.utils.html.impl.utils;

import android.graphics.Typeface;
import android.text.Layout;

public class TextUtils {

    public static class Bold {

    }

    public static class Italic {

    }

    public static class Underline {

    }

    public static class Strikethrough {

    }

    public static class Big {

    }

    public static class Small {

    }

    public static class Monospace {

    }

    public static class Blockquote {

    }

    public static class Super {

    }

    public static class Sub {

    }

    public static class Bullet {

    }

    public static class Font {
        public String mFamily;
        public Typeface mTypeface;

        public Font(String family, Typeface typeface) {
            mFamily = family;
            mTypeface = typeface;
        }
    }

    public static class Href {
        public String mHref;

        public Href(String href) {
            mHref = href;
        }
    }

    public static class Foreground {
        public int mForegroundColor;

        public Foreground(int foregroundColor) {
            mForegroundColor = foregroundColor;
        }
    }

    public static class Background {
        public int mBackgroundColor;

        public Background(int backgroundColor) {
            mBackgroundColor = backgroundColor;
        }
    }

    public static class Heading {
        public int mLevel;

        public Heading(int level) {
            mLevel = level;
        }
    }

    public static class Newline {
        public int mNumNewlines;

        public Newline(int numNewlines) {
            mNumNewlines = numNewlines;
        }
    }

    public static class Alignment {
        public Layout.Alignment mAlignment;

        public Alignment(Layout.Alignment alignment) {
            mAlignment = alignment;
        }
    }

}
