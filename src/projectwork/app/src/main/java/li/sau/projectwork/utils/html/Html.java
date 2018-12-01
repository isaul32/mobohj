package li.sau.projectwork.utils.html;

import android.text.Spanned;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class Html {

    /*public static Spanned fromHtml(String source) {
        return fromHtml(source, new DefaultTagHandler());
    }*/

    public static Spanned fromHtml(String source, TagHandler tagHandler) {
        Parser parser = new Parser();
        try {
            parser.setProperty(Parser.schemaProperty, new HTMLSchema());
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            // Never happen! Read the source...
            throw new RuntimeException(e);
        }

        // Create converter instance
        HtmlToSpannedConverter converter = new HtmlToSpannedConverter(source, parser, tagHandler);

        return converter.convert();
    }
}
