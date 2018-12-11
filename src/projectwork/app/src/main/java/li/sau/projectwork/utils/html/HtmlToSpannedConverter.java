package li.sau.projectwork.utils.html;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

import li.sau.projectwork.utils.html.ImageGetter;
import li.sau.projectwork.utils.html.TagHandler;

public class HtmlToSpannedConverter implements ContentHandler {
    private String mSource;
    private XMLReader mReader;
    private ImageGetter mImageGetter;
    private TagHandler mTagHandler;
    private SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder();

    public HtmlToSpannedConverter(
            String source,
            XMLReader mReader,
            ImageGetter imageGetter,
            TagHandler tagHandler
    ) {
        this.mSource = source;
        this.mReader = mReader;
        this.mImageGetter = imageGetter;
        this.mTagHandler = tagHandler;
    }

    public Spanned convert() {
        // This converter implements ContentHandler interface
        mReader.setContentHandler(this);

        try {
            mReader.parse(new InputSource(new StringReader(mSource)));
        } catch (IOException | SAXException e) {
            // We are reading from a string. There isn't IO problems.
            // Never happen! Read the source...
            throw new RuntimeException(e);
        }

        // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/Html.java#757
        // Multiple traverse is fool when using SAX parser

        return mSpannableStringBuilder;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        mTagHandler.handleStartTag(mSpannableStringBuilder, mImageGetter, localName, atts);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        StringBuilder sb = new StringBuilder();

        // Start reading buffer array
        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            sb.append(c);

            // If you want ignore extra whitespaces
            // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/Html.java#1222
        }

        mSpannableStringBuilder.append(sb);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mTagHandler.handleEndTag(mSpannableStringBuilder, localName);
    }

    @Override
    public void setDocumentLocator(Locator locator) { }

    @Override
    public void startDocument() throws SAXException { }

    @Override
    public void endDocument() throws SAXException { }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException { }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException { }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { }

    @Override
    public void processingInstruction(String target, String data) throws SAXException { }

    @Override
    public void skippedEntity(String name) throws SAXException { }

}
