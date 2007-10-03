/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.common.contenttype;

import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * @author SergeMaslyukov
 *         Date: 15.01.2006
 *         Time: 16:16:12
 *         $Id$
 */
public class ContentTypeManager {
    private static Logger log = Logger.getLogger(ContentTypeManager.class);

    private ContentType contentType = null;

    // todo
    // If the deployment descriptor contains a locale-encoding-mapping-list element,
    // and that element provides a mapping for the given locale, that mapping is used.
    // Otherwise, the mapping from locale to character encoding is container dependent.
    final static Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    private static CharsetMapper charsetMapper = new CharsetMapper();

    public static ContentTypeManager getInstance(Locale locale) {
        return new ContentTypeManager(locale, true);
    }

    public static ContentTypeManager getInstance(Locale locale, boolean isUseMapper) {
        return new ContentTypeManager(locale, isUseMapper);
    }

    private ContentTypeManager(Locale locale, boolean isUseMapper) {
        if (log.isDebugEnabled()) {
            log.debug("start initContentType");
            log.debug("locale: " + locale);
        }
        if (locale == null) {
            contentType = new ContentType(DEFAULT_CONTENT_TYPE, DEFAULT_CHARSET);
            return;
        }

        String charset = null;
        if (isUseMapper) {
            charset = charsetMapper.getCharset(locale);
        }

        if (charset == null) {
            contentType = new ContentType("text/html", DEFAULT_CHARSET);
        }
        else {
            contentType = new ContentType("text/html", Charset.forName(charset));
        }

        if (log.isDebugEnabled()) {
            log.debug("contentType: " + contentType);
        }
    }

    public void setContentType(String contentTypeString ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "set new contentType: " + contentTypeString );
        }

        this.contentType = prepareContentType( contentTypeString );
    }

    public void setCharacterEncoding( String charset ) {
        contentType.setCharset( charset );
    }

    public String getContentType() {

        StringBuilder content = contentType.getContentTypeStringBuilder();
        if (content==null) {
            return null;
        }

        String charset = getCharacterEncoding();
        if (charset==null) {
            return content.toString();
        }
        else {
            return  content.append( "; charset=" ).append( charset ).toString();
        }
    }

    public String getCharacterEncoding() {

        if ( log.isDebugEnabled()) {
            log.debug("start getCharacterEncoding");
            log.debug("contentType: " + contentType );
            if (contentType!=null ) {
                log.debug("contentType: " + contentType.getCharset().toString() );
            }
            else {
                log.debug( "contentType is null" );
            }
        }

        if ( contentType.getCharset()!=null )
            return contentType.getCharset().toString();
        else
            return null;
    }

    static ContentType prepareContentType(String contentTypeString) {
        ContentType type = new ContentType(contentTypeString);
        if (log.isDebugEnabled()) {
            log.debug("parsed contentType: " + type);
            log.debug("contentType.getContentType(): " + type.getContentType());
            log.debug("contentType.getCharset(): " + type.getCharset());
        }

        StringBuilder contentTypeTemp;
        if (type.getContentType() != null) {
            contentTypeTemp = type.getContentTypeStringBuilder();
        }
        else {
            contentTypeTemp = new StringBuilder(DEFAULT_CONTENT_TYPE);
        }

        Charset charsetTemp;
        if (type.getCharset() != null) {
            charsetTemp = type.getCharset();
        }
        else {
            charsetTemp = DEFAULT_CHARSET;
        }

        ContentType contentType = new ContentType(contentTypeTemp, charsetTemp);

        if (log.isDebugEnabled()) {
            log.debug("result contentType.charset " + contentType.getCharset());
            log.debug("result contentType.contentType " + contentType.getContentType());
        }

        return contentType;
    }
}
