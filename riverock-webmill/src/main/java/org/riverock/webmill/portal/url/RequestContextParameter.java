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
package org.riverock.webmill.portal.url;

import java.util.Locale;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.riverock.webmill.container.portlet.PortletContainer;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:46:07
 */
public final class RequestContextParameter {
    private HttpServletRequest request = null;
    private PortletContainer portletContainer = null;
    private Locale predictedLocale = null;
    private boolean isMultiPartRequest = false;
    private File requestBodyFile = null;
    private Long siteId;

    public RequestContextParameter(HttpServletRequest request, PortletContainer portletContainer, boolean isMultiPartRequest, File requestBodyFile, Long siteId) {
        this.siteId = siteId;
        this.request = request;
        this.portletContainer = portletContainer;
        this.isMultiPartRequest = isMultiPartRequest;
        this.requestBodyFile = requestBodyFile;

        this.predictedLocale = RequestContextUtils.prepareLocale(this);
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

    public HttpServletRequest getRequest() {
        return request;
    }

    public PortletContainer getPortletContainer() {
        return portletContainer;
    }

    public Long getSiteId() {
        return siteId;
    }
}
