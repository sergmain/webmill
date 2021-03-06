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
package org.riverock.webmill.portal.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import org.riverock.common.contenttype.ContentTypeManager;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.namespace.Namespace;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:01 AM
 * <p/>
 * $Id: ActionRequestImpl.java 1374 2007-08-28 19:11:15Z serg_main $
 */
public final class ActionRequestImpl extends WebmillPortletRequest implements ActionRequest {
    private final static Logger log = Logger.getLogger(ActionRequestImpl.class);

    private File requestBodyFile = null;
    private boolean isMultiPartRequest;

    private BufferedReader realBufferedReader = null;
    private InputStream realInputStream = null;
    private ContentTypeManager contentTypeManager = null;

    public void destroy() {
        super.destroy();
        requestBodyFile=null;
        if (realBufferedReader!=null) {
            try {
                realBufferedReader.close();
                realBufferedReader=null;
            }
            catch (IOException e) {
                log.warn("Error close realBufferedReader", e);
            }
        }
        if (realInputStream!=null) {
            try {
                realInputStream.close();
            }
            catch (IOException e) {
                log.warn("Error close realInputStream", e);
            }
        }
        contentTypeManager=null;
    }

    public ActionRequestImpl(
        final Map<String, List<String>> parameters,
        final PortalRequest portalRequest,
        final ServletContext servletContext,
        final String contextPath,
        final PortletPreferences portletPreferences,
        final Map<String, List<String>> portletProperties,
        final PortletContext portletContext,
        final PortletDefinition portletDefinition,
        final Namespace namespace,
        final PortalInfo portalInfo,
        PortalInstance portalInstance
        ) {

        super(
            servletContext, portalRequest.getHttpRequest(), portletPreferences,
            portletProperties, new HashMap<String, List<String>>(),
            portletContext, portletDefinition, namespace,
            portalInstance
        );

        prepareRequest(parameters, portalRequest, contextPath, portalInfo);
        requestBodyFile = portalRequest.getRequestBodyFile();
        isMultiPartRequest = portalRequest.isMultiPartRequest();

        contentTypeManager = ContentTypeManager.getInstance(portalRequest.getLocale(), false);
    }

    public InputStream getPortletInputStream() throws java.io.IOException {
        if (!isMultiPartRequest) {
            throw new IllegalStateException("Request is not multipart. Cant get portlet inputStream");
        }
        if (realBufferedReader != null) {
            throw new IllegalStateException("getReader() already invoked");
        }
        if (log.isDebugEnabled()) {
            log.debug("getPortletInputStream(), realInputStream: " +
                (realInputStream == null ? "is null" : realInputStream.getClass().getName()));
        }

        realInputStream = new FileInputStream(requestBodyFile);

        return realInputStream;
    }

    public BufferedReader getReader() throws java.io.IOException {
        if (!isMultiPartRequest) {
            throw new IllegalStateException("Request is not multipart. Cant get portlet reader");
        }
        if (realInputStream != null) {
            throw new IllegalStateException("getPortletInputStream() already invoked");
        }

        if (log.isDebugEnabled()) {
            log.debug("getReader(), realInputStream: " +
                (realInputStream == null ? "is null" : realInputStream.getClass().getName()));
        }

        if (log.isDebugEnabled()) {
            log.debug("contentType: " + contentTypeManager);
            if (contentTypeManager != null)
                log.debug("charset: " + contentTypeManager.getCharacterEncoding());
        }

        realBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(requestBodyFile), contentTypeManager.getCharacterEncoding()));

        return realBufferedReader;
    }

    public void setCharacterEncoding(String encoding) {
        if (realInputStream != null && realBufferedReader != null) {
            throw new IllegalStateException("try to setCharacterEncoding() after invoke getReader() or getPortletInputStream()");
        }
        contentTypeManager.setCharacterEncoding(encoding);
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
        if (isMultiPartRequest && requestBodyFile != null) {
            return (int) requestBodyFile.length();
        }
        else {
            return -1;
        }
    }
}

