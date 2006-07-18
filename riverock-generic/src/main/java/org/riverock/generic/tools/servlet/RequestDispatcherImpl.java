/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.generic.tools.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import org.riverock.interfaces.generic.InternalRequest;

/**
 * User: SergeMaslyukov
 * Date: 24.01.2005
 * Time: 23:59:02
 * $Id$
 */
public class RequestDispatcherImpl implements RequestDispatcher {
    private final static Logger log = Logger.getLogger( RequestDispatcherImpl.class );

    private RequestDispatcher requestDispatcher = null;
    private String queryString=null;

    public RequestDispatcherImpl( RequestDispatcher requestDispatcher ) {
        this.requestDispatcher = requestDispatcher;
    }

    public RequestDispatcherImpl( RequestDispatcher requestDispatcher, String queryString ) {
        this.requestDispatcher = requestDispatcher;
        this.queryString = queryString;
    }

    public void forward( ServletRequest servletRequest, ServletResponse servletResponse ) throws ServletException, IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "forward new context" );
        }

        try {
            requestDispatcher.include( servletRequest, servletResponse );
            servletResponse.flushBuffer();
        }
        catch( java.io.IOException e ) {
            String es = "IOException include new request";
            log.error( es, e );
            throw e;
        }
        catch( ServletException e ) {
            String es = "Error include new request";
            log.error( es, e );
            throw new ServletException( es, e );
        }
    }

    public void include( ServletRequest request, ServletResponse response ) throws IOException, ServletException {


        InternalRequest internalRequest = getInternalRequest(request);

        boolean isIncluded = (internalRequest.isIncluded());
        try {
            internalRequest.setIncluded(true);
            internalRequest.setIncludedQueryString(queryString);

            requestDispatcher.include( (ServletRequest) internalRequest, response);
        }
        catch( java.io.IOException e ) {
            String es = "IOException include new request";
            log.error( es, e );
            throw e;
        }
        catch( ServletException e ) {
            String es = "Error include new request";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            internalRequest.setIncluded(isIncluded);
        }
    }

    private static InternalRequest getInternalRequest(ServletRequest request) {
        while (!(request instanceof InternalRequest)) {
            request = ((ServletRequestWrapper) request).getRequest();
            if (request == null) {
                throw new IllegalStateException(
                        "The internal request cannot be found.");
            }
        }
        return (InternalRequest) request;
    }

}
