/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import org.riverock.common.contenttype.ContentTypeManager;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.namespace.Namespace;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:01 AM
 * <p/>
 * $Id$
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
    }

    public ActionRequestImpl(
        final Map<String, List<String>> parameters,
        final PortalRequestInstance portalRequestInstance,
        final ServletContext servletContext,
        final String contextPath,
        final PortletPreferences portletPreferences,
        final Map<String, List<String>> portletProperties,
        final PortalContext portalContext,
        final PortletContext portletContext,
        final PortletDefinition portletDefinition,
        final Namespace namespace) {

        super(
            servletContext, portalRequestInstance.getHttpRequest(), portletPreferences,
            portletProperties, new HashMap<String, List<String>>(),
            portletContext, portletDefinition, namespace
        );

        prepareRequest(parameters, portalRequestInstance, contextPath, portalContext);
        requestBodyFile = portalRequestInstance.getRequestBodyFile();
        isMultiPartRequest = portalRequestInstance.isMultiPartRequest();

        contentTypeManager = ContentTypeManager.getInstance(portalRequestInstance.getLocale(), false);
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

