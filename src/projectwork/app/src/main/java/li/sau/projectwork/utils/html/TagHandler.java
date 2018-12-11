package li.sau.projectwork.utils.html;

import android.text.SpannableStringBuilder;

import org.xml.sax.Attributes;

public interface TagHandler {

    void handleStartTag(
            SpannableStringBuilder spannableStringBuilder,
            ImageGetter imageGetter,
            String tag,
            Attributes attributes
    );

    void handleEndTag(SpannableStringBuilder spannableStringBuilder, String tag);

}
