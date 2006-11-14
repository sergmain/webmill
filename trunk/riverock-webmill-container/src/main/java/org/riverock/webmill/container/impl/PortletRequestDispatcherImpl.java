/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
