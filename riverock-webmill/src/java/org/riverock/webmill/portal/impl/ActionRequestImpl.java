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
package org.riverock.webmill.portal.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletPreferences;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import org.riverock.common.contenttype.ContentTypeManager;
import org.riverock.webmill.portal.PortalRequestInstance;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:01 AM
 *
 * $Id$
 */
public final class ActionRequestImpl extends WebmillPortletRequest implements ActionRequest {
    private final static Logger log = Logger.getLogger( ActionRequestImpl.class );

    private File requestBodyFile = null;
    private boolean isMultiPartRequest;

    private BufferedReader realBufferedReader = null;
    private InputStream realInputStream = null;
    private ContentTypeManager contentTypeManager = null;

    public void destroy() {
        super.destroy();
    }

    public ActionRequestImpl(final Map<String, Object> parameters, final PortalRequestInstance portalRequestInstance, final ServletContext servletContext, final Map<String, Object> portletAttributes, final String contextPath, final String portalContextPath, final PortletPreferences portletPreferences, final Map<String, List<String>> portletProperties, final PortalContext portalContext ) {
        super( servletContext, portalRequestInstance.getHttpRequest(), portletPreferences, portletProperties);
        prepareRequest( parameters, portalRequestInstance, null, portletAttributes, contextPath, portalContextPath, portalContext );
        requestBodyFile = portalRequestInstance.getRequestBodyFile();
        isMultiPartRequest = portalRequestInstance.isMultiPartRequest();

        contentTypeManager = ContentTypeManager.getInstance(portalRequestInstance.getLocale(), false);
    }

    public InputStream getPortletInputStream() throws java.io.IOException {
        if (!isMultiPartRequest) {
            throw new IllegalStateException("Cant get portlet input stream for content type: " +httpRequest.getContentType() );
        }
        if (realBufferedReader!=null) {
            throw new IllegalStateException( "getReader() already invoked" );
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "getPortletInputStream(), realInputStream: " +
                (realInputStream==null?"is null":realInputStream.getClass().getName())
            );
        }

        realInputStream = new FileInputStream( requestBodyFile );

        return realInputStream;
    }

    public BufferedReader getReader() throws java.io.IOException {
        if (!isMultiPartRequest) {
            throw new IllegalStateException("Cant get portlet input stream for content type: " +httpRequest.getContentType() );
        }
        if (realInputStream!=null) {
            throw new IllegalStateException( "getPortletInputStream() already invoked" );
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "getReader(), realInputStream: " +
                (realInputStream==null?"is null":realInputStream.getClass().getName())
            );
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "contentType: " + contentTypeManager );
            if (contentTypeManager!=null)
                log.debug( "charset: " + contentTypeManager.getCharacterEncoding() );
        }

        realBufferedReader = new BufferedReader(
            new InputStreamReader( new FileInputStream(requestBodyFile), contentTypeManager.getCharacterEncoding() )
        );

        return realBufferedReader;
    }

    public void setCharacterEncoding( String encoding ) {
        contentTypeManager.setCharacterEncoding( encoding );
    }

    public String getCharacterEncoding() {
        return contentTypeManager.getCharacterEncoding();
    }

    public String getContentType() {
        if (isMultiPartRequest) {
            return httpRequest.getContentType();
        }
        else {
            return contentTypeManager.getContentType();
        }
    }

    public int getContentLength() {
        if (isMultiPartRequest) {
            return httpRequest.getContentLength();
        }
        else {
            return -1;
        }
    }
}

