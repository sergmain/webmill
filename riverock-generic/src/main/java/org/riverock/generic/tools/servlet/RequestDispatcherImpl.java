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
