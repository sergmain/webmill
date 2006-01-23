/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.utils;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.common.multipart.AbstractPart;
import org.riverock.common.multipart.FilePart;
import org.riverock.common.multipart.MultipartHandler;
import org.riverock.common.multipart.MultipartRequestException;
import org.riverock.common.multipart.MultipartRequestWrapper;
import org.riverock.common.multipart.UploadException;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalException;

/**
 * $Id$
 */
public final class PortletUtils {
    private final static Logger log = Logger.getLogger(PortletUtils.class);

    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_WML = "text/wml";

    public static String getString(final PortletRequest request, final String f, final String def) {
        return PortletService.getString( request, f, def, WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
    }

    public static boolean isMultiPart( final HttpServletRequest request ) {
        String contentType = request.getContentType();
        return contentType!=null && contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER)>-1;
    }

    public static File storeBodyRequest( final HttpServletRequest request, int maxLength ) {
        if (isMultiPart(request)) {
            int length = request.getContentLength();
            if (length>maxLength) {
                throw new IllegalStateException("Can not process body request because content length exceed max length. " +
                    "max length: "+maxLength + ", request content length: " + length);
            }
            File file = null;
            try {
                file = new File( WebmillConfig.getWebmillTempDir() + File.separatorChar + File.createTempFile("request", ".dat") );
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
        return null;
    }

    public static final int BUFFER_SIZE = 512;
    public static void copyData(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count;
        byte buffer[] = new byte[BUFFER_SIZE];

        while ((count = inputStream.read(buffer))==BUFFER_SIZE) {
            outputStream.write(buffer);
        }

        if (count!=-1) {
            outputStream.write(buffer, 0, count);
        }
    }

    private static final boolean saveUploadedFilesToDisk = false;
    public static Map<String, Object> parseMultiPartRequest(
        InputStream inputStream,
        String contentType,
        int contentLength
        )
        throws IOException, UploadException, PortletException, MultipartRequestException {

        Map<String, Object> p = new HashMap<String, Object>();

        if (contentType != null &&
            contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER)== -1) {
            // it's a multipart request

            throw new IllegalStateException("Method parseMultiPartRequest() can process only MultiPart request.");
        }

        MultipartRequestWrapper reqWrapper =
            new MultipartRequestWrapper(
                inputStream,
                contentType,
                contentLength,
                saveUploadedFilesToDisk,
                null,
                false,
                false,
                3*1024*1024
            );

        MultipartHandler handler = reqWrapper.getMultipartHandler();
        if (handler == null)
            throw new PortletException("MultipartHandler is null");

        Iterator e = handler.getPartsHash().keySet().iterator();
        AbstractPart part = null;
        while (e.hasNext()) {
            String key = (String) e.next();
            part = (AbstractPart) handler.getPartsHash().get(key);
            if (part instanceof List) {
                throw new PortletException("Not implemented");
            }
            else {
                if (part.getType()==AbstractPart.PARAMETER_TYPE) {
                    p.put(key, part.getStringValue());
                }
                else if ( part instanceof FilePart ) {
                    p.put(key, ((FilePart)part) );
                }
                else {
                    throw new PortletException("Unknown type of parameter from multipartrequest: " + part.getClass().getName() );
                }
            }
        }
        return p;
    }

    public static Map<String, Object> getParameters( final HttpServletRequest request ) {

        String contentType = request.getContentType();

        if (contentType != null &&
            contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER) > -1) {
            // it's a multipart request

            throw new IllegalStateException("MultiPart request must processed via parseMultiPartRequest() method");
        }
        Map<String, Object> p = new HashMap<String, Object>();

        Enumeration e = request.getParameterNames();
        for (; e.hasMoreElements() ;) {
            String key = (String)e.nextElement();

            String value[] = request.getParameterValues( key );
            if (value!=null) {
                if (value.length==1)
                    p.put(key, value[0]);
                else {
                    List<String> ee = new ArrayList<String>();
                    for (final String newVar : value)
                        ee.add(newVar);

                    p.put(key, ee);
                }
            }
        }
        return p;
    }

    public static void setContentType(HttpServletResponse response) throws PortalException {
        setContentType(response, WebmillConfig.getHtmlCharset());
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