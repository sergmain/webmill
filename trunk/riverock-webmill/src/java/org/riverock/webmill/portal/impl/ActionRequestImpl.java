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
import java.io.InputStream;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.servlet.ServletContext;

import org.riverock.webmill.portal.PortalRequestInstance;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:01 AM
 *
 * $Id$
 */
public final class ActionRequestImpl extends WebmillPortletRequestV2 implements ActionRequest {

    public void destroy() {
        super.destroy();
    }

    public ActionRequestImpl(final Map parameters, final PortalRequestInstance portalRequestInstance, ServletContext servletContext, Map portletAttributes, String contextPath, String portalContextPath) {
        super( servletContext, portalRequestInstance.getHttpRequest() );
        prepareRequest( parameters, portalRequestInstance, null, portletAttributes, contextPath, portalContextPath);
    }

    public InputStream getPortletInputStream() {
        throw new IllegalStateException( "not implemented. use request.getParameter() method" );
    }

    public BufferedReader getReader() {
        return null;
    }

    public void setCharacterEncoding( String s ) {
        throw new IllegalStateException( "not implemented" );
//        httpResponse.setContent
    }

    public String getCharacterEncoding() {
        return httpRequest.getCharacterEncoding();
    }

    public String getContentType() {
        return httpRequest.getContentType();
    }

    public int getContentLength() {
        return httpRequest.getContentLength();
    }
}

