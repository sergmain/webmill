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
package org.riverock.webmill.portal.context;

import java.util.Locale;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.portlet.PortletContainer;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 12:46:07
 */
public final class RequestContextParameter {
    private HttpServletRequest request = null;
    private PortalInfo portalInfo = null;
    private PortletContainer portletContainer = null;
    private Locale predictedLocale = null;
    private boolean isMultiPartRequest = false;
    private File requestBodyFile = null;

    public RequestContextParameter(HttpServletRequest request, PortalInfo portalInfo, PortletContainer portletContainer, boolean isMultiPartRequest, File requestBodyFile) {
        this.request = request;
        this.portalInfo = portalInfo;
        this.portletContainer = portletContainer;
        this.predictedLocale = RequestContextUtils.prepareLocale(this);
        this.isMultiPartRequest = isMultiPartRequest;
        this.requestBodyFile = requestBodyFile;
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

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    public PortletContainer getPortletContainer() {
        return portletContainer;
    }

}
