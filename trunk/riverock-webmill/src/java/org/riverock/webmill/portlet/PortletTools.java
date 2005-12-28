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
package org.riverock.webmill.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.riverock.common.multipart.AbstractPart;
import org.riverock.common.multipart.FilePart;
import org.riverock.common.multipart.MultipartHandler;
import org.riverock.common.multipart.MultipartRequestException;
import org.riverock.common.multipart.MultipartRequestWrapper;
import org.riverock.common.multipart.UploadException;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.schema.site.SiteTemplateParameterType;

/**
 * $Id$
 */
public final class PortletTools {

    public static final String MIME_TYPE_TEXT_XML = "text/xml";
    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_WML = "text/wml";

    public static String getString(final PortletRequest request, final String f, final String def) {
        return PortletService.getString( request, f, def, WebmillConfig.getServerCharset(), WebmillConfig.getHtmlCharset());
    }

    public static String getString( final List v, final String nameParam) throws IllegalArgumentException{
        return getString(v, nameParam, null);
    }

    public synchronized static String getString( final List v, final String nameParam, final String defValue ) {
        if (v == null || nameParam == null || nameParam.trim().length() == 0)
            return defValue;

        for (int i = 0; i < v.size(); i++) {
            SiteTemplateParameterType item = (SiteTemplateParameterType) v.get(i);
            if (item.getName().equals(nameParam))
                return item.getValue();
        }
        return defValue;
    }

    private static final boolean saveUploadedFilesToDisk = false;
    public static Map<String, Object> getParameters( final HttpServletRequest request )
        throws IOException, UploadException, PortletException, MultipartRequestException {

        Map<String, Object> p = new HashMap<String, Object>();
        String contentType = request.getContentType();

        if (contentType != null &&
            contentType.toLowerCase().indexOf(MultipartRequestWrapper.MFDHEADER) > -1) {
            // it's a multipart request

            MultipartRequestWrapper reqWrapper =
                new MultipartRequestWrapper(
                    request,
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
        }
        else {
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
        }

        return p;
    }
}