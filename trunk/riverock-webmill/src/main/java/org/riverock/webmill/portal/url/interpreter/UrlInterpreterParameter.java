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
package org.riverock.webmill.portal.url.interpreter;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;
import org.riverock.interfaces.portal.PortalInfo;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:46:07
 */
public final class UrlInterpreterParameter {
    private PortletDefinitionProvider portletDefinitionProvider=null;
    private Locale predictedLocale = null;
    private boolean isMultiPartRequest = false;
    private File requestBodyFile = null;
    private Long siteId;
    private Map<String, List<String>> httpRequestParameter;
    private String pathInfo;
    private PortalInfo portalInfo;

    public UrlInterpreterParameter(
        String pathInfo, PortletDefinitionProvider portletDefinitionProvider,
        boolean isMultiPartRequest, File requestBodyFile,
        Long siteId, Locale predictedLocale, Map<String, List<String>> httpRequestParameter,
        PortalInfo portalInfo
    ) {
        this.siteId = siteId;
        this.pathInfo = pathInfo;
        this.portletDefinitionProvider = portletDefinitionProvider;
        this.isMultiPartRequest = isMultiPartRequest;
        this.requestBodyFile = requestBodyFile;
        this.predictedLocale = predictedLocale;
        this.httpRequestParameter = httpRequestParameter;
        this.portalInfo=portalInfo;

        if ((isMultiPartRequest && httpRequestParameter!=null) || (!isMultiPartRequest && requestBodyFile!=null)) {
            throw new PortalException(
                "Wrong state. isMultiPartRequest: " + isMultiPartRequest+", httpRequestParameter!=null: " +(httpRequestParameter!=null)+
                    ", requestBodyFile!=null: " + (requestBodyFile!=null)
            );
        }
    }

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public PortletDefinitionProvider getPortletDefinitionProvider() {
        return portletDefinitionProvider;
    }

    public Map<String, List<String>> getHttpRequestParameter() {
        return httpRequestParameter;
    }

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public boolean isMultiPartRequest() {
        return isMultiPartRequest;
    }

    public Locale getPredictedLocale() {
        return predictedLocale;
    }

    public Long getSiteId() {
        return siteId;
    }
}
