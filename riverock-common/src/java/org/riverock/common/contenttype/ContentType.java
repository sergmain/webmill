package org.riverock.common.contenttype;

import java.nio.charset.Charset;

/**
 * @author SergeMaslyukov
 *         Date: 15.01.2006
 *         Time: 16:15:39
 *         $Id$
 */
public class ContentType {

    private StringBuffer contentType = null;
    private Charset charset = null;

    public ContentType(final String contentType) {
        this(contentType, (String) null);
    }

    public void setCharset(String charset_) {
        this.charset = Charset.forName(charset_);
    }

    public ContentType(final StringBuffer contentType, final Charset charset) {
        this.contentType = contentType;
        this.charset = charset;
    }

    public ContentType(final String contentType, final Charset charset) {
        this.contentType = new StringBuffer(contentType);
        this.charset = charset;
    }

    public ContentType(final String contentType, final String defaultContentType) {
        parse(contentType);
        if (contentType == null)
            this.contentType = new StringBuffer(defaultContentType);
    }

    // format: "text/html; charset=utf-8"
    private final static String CHARSET = "charset";

    private void parse(final String contentTypeString) {
        if (contentTypeString == null)
            return;

        int idx = contentTypeString.indexOf(';');
        if (idx == -1) {
            this.contentType = new StringBuffer(contentTypeString);
            return;
        }

        this.charset = extractCharset(contentTypeString.substring(idx + 1));
        this.contentType = new StringBuffer(contentTypeString.substring(0, idx).trim());
    }

    private Charset extractCharset(final String contentType) {
        if (contentType == null)
            return null;

        String s = contentType.trim();
        if (!s.startsWith(CHARSET))
            return null;

        int idx = s.indexOf('=');
        if (idx == -1)
            return null;

        return Charset.forName(s.substring(idx + 1).trim());
    }

    public String getContentType() {
        return contentType.toString();
    }

    public StringBuffer getContentTypeStringBuffer() {
        return contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public String toString() {
        return "[contentType:"+contentType+",charset:"+charset+"]";
    }
}
