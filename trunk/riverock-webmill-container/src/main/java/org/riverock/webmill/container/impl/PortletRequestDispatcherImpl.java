/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.impl;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.riverock.interfaces.generic.InternalRequest;

public final class PortletRequestDispatcherImpl implements PortletRequestDispatcher {

    private RequestDispatcher requestDispatcher = null;
    private String queryString=null;

    public PortletRequestDispatcherImpl( RequestDispatcher requestDispatcher ) {
        this.requestDispatcher = requestDispatcher;
    }

    public PortletRequestDispatcherImpl( RequestDispatcher requestDispatcher, String queryString) {
        this.requestDispatcher = requestDispatcher;
        this.queryString = queryString;
    }

    public void include( RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        InternalRequest internalRequest = getInternalRequest(request);

        boolean isIncluded = (internalRequest.isIncluded());
        try {
            internalRequest.setIncluded(true);
            internalRequest.setIncludedQueryString(queryString);

            requestDispatcher.include( (HttpServletRequest) internalRequest, (HttpServletResponse) response);
        } catch (IOException ex) {
            throw ex;
        } catch (ServletException ex) {
            if (ex.getRootCause() != null) {
                throw new PortletException(ex.getRootCause());
            } else {
                throw new PortletException(ex);
            }
        } finally {
            internalRequest.setIncluded(isIncluded);
        }
    }

    private static InternalRequest getInternalRequest(RenderRequest request) {
        while (!(request instanceof InternalRequest)) {
            request = (RenderRequest)((ServletRequestWrapper) request).getRequest();
            if (request == null) {
                throw new IllegalStateException(
                        "The internal portlet request cannot be found.");
            }
        }
        return (InternalRequest) request;
    }

}
