/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import org.riverock.webmill.exception.PortalException;

/**
 * $Id: PortletUtils.java 1052 2006-11-14 16:46:45Z serg_main $
 */
@SuppressWarnings({"UnusedAssignment"})
public final class PortletUtils {
    private final static Logger log = Logger.getLogger(PortletUtils.class);

    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_WML = "text/wml";

    public static boolean isMultiPart( HttpServletRequest request ) {
        return FileUpload.isMultipartContent( new ServletRequestContext( request ) );
    }

    public static String getString( final HttpServletRequest request, final String f, final String def) {
        return org.riverock.common.tools.ServletTools.getString( request, f, def, CharEncoding.ISO_8859_1, StandardCharsets.UTF_8.toString());
    }

    public static File storeBodyRequest( final HttpServletRequest request, int maxLength ) {
        if (log.isDebugEnabled()) {
            log.info("start storeBodyRequest()" );
        }

        if (!isMultiPart(request)) {
            return null;
        }
        int length = request.getContentLength();
        if (log.isDebugEnabled()) {
            log.debug("Content length: " + length);
        }
        if (length>maxLength) {
            throw new IllegalStateException("Can not process body request because content length exceed max length. " +
                "max length: "+maxLength + ", request content length: " + length);
        }
        File file;
        try {
            file = File.createTempFile("request", ".dat") ;
            if (log.isDebugEnabled()) {
                log.debug("Temporary file with multipart request: " + file);
            }
            OutputStream outputStream = new FileOutputStream( file );
            InputStream inputStream = request.getInputStream();
            copyData(inputStream, outputStream);
            outputStream.flush();
            outputStream.close();
            outputStream = null;
            inputStream.close();
            inputStream = null;
        }
        catch (IOException e) {
            String es = "Error store body of request";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }

        return file;
    }

    public static final int BUFFER_SIZE = 512;
    public static void copyData(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count;
        byte buffer[] = new byte[BUFFER_SIZE];
        while ((count = inputStream.read(buffer))>=0) {
            outputStream.write(buffer, 0, count);
            outputStream.flush();
        }
    }

    public static void setContentType(HttpServletResponse response) throws PortalException {
        setContentType(response, StandardCharsets.UTF_8.toString());
    }

    public static void setContentType(HttpServletResponse response, String charset) throws PortalException {

        final String type = "text/html; charset=" + charset;

        if (log.isDebugEnabled()) {
            log.debug("set new charset: " + type);
            log.debug("response: " + response);
        }

        try {
            response.setContentType(type);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }
}