/*
 * org.riverock.common - Supporting classes and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.common.contenttype;

import java.nio.charset.Charset;

/**
 * @author SergeMaslyukov
 *         Date: 15.01.2006
 *         Time: 16:15:39
 *         $Id$
 */
public class ContentType {

    private StringBuilder contentType = null;
    private Charset charset = null;

    public ContentType(final String contentType) {
        this(contentType, (String) null);
    }

    public void setCharset(String charset_) {
        this.charset = Charset.forName(charset_);
    }

    public ContentType(final StringBuilder contentType, final Charset charset) {
        this.contentType = contentType;
        this.charset = charset;
    }

    public ContentType(final String contentType, final Charset charset) {
        this.contentType = new StringBuilder(contentType);
        this.charset = charset;
    }

    public ContentType(final String contentType, final String defaultContentType) {
        parse(contentType);
        if (contentType == null)
            this.contentType = new StringBuilder(defaultContentType);
    }

    // format: "text/html; charset=utf-8"
    private final static String CHARSET = "charset";

    private void parse(final String contentTypeString) {
        if (contentTypeString == null)
            return;

        int idx = contentTypeString.indexOf(';');
        if (idx == -1) {
            this.contentType = new StringBuilder(contentTypeString);
            return;
        }

        this.charset = extractCharset(contentTypeString.substring(idx + 1));
        this.contentType = new StringBuilder(contentTypeString.substring(0, idx).trim());
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

    public StringBuilder getContentTypeStringBuilder() {
        return contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public String toString() {
        return "[contentType:"+contentType+",charset:"+charset+"]";
    }
}
