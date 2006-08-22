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
package org.riverock.webmill.portal;

import java.util.Map;
import java.util.List;
import java.io.File;

import org.riverock.webmill.portal.context.RequestState;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 17:20:09
 */
public final class PortletParameters {
    private String namespace = null;
    private Map<String, List<String>> parameters = null;

    /** File with request data, if request is multipart */
    private File requestBodyFile = null;

    private boolean isMultiPart = false;
    private RequestState requestState = null;

    public PortletParameters( final String namespace, RequestState requestState, final Map<String, List<String>> parameters) {
        this.namespace = namespace;
        this.parameters = parameters;
        this.requestState = requestState;
    }

    public PortletParameters( final String namespace, RequestState requestState, File requestBodyFile ) {
        this.namespace = namespace;
        this.requestBodyFile = requestBodyFile;
        this.isMultiPart = true;
        this.requestState = requestState;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public boolean isMultiPart() {
        return isMultiPart;
    }

    public File getRequestBodyFile() {
        return requestBodyFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }
}