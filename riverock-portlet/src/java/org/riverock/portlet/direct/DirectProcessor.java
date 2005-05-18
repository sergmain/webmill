/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.direct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Enumeration;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portlet.CookieManager;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 25.01.2005
 * Time: 20:22:10
 * $Id$
 */
public final class DirectProcessor {
    private final static Logger log = Logger.getLogger( DirectProcessor.class );
    private static final int TIMEOUT = 10000;

    public static void process( final RenderRequest renderRequest, final RenderResponse renderResponse ) throws PortletException, IOException {

        String path = (String)renderRequest.getAttribute( PortalConstants.PORTAL_URL_RESOURCE_ATTRIBUTE );
        String methodName = (String)renderRequest.getAttribute( PortalConstants.PORTAL_QUERY_METHOD_ATTRIBUTE );

        String uri = null;
        boolean currentContext = PortletTools.getMetadataBoolean( renderRequest, "use-current-context", false );
        String sessionIdName = PortletTools.getMetadata( renderRequest, "session-id-name", "JSESSIONID" );
        if (currentContext) {
            uri = renderRequest.getContextPath() + path;
        }
        else {
            uri = path;
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "path: " + path);
            log.debug( "methodName: " + methodName);
            log.debug( "currentContext: " + currentContext);
            log.debug( "uri: " + uri);
        }

        if ( path == null )
            return;

        HttpMethodBase method = null;
        try {
            HttpClient httpClient = new HttpClient();

            initCookie( httpClient, renderRequest, sessionIdName );
            setConnectionTimeout( httpClient );


            httpClient.getHostConfiguration().setHost( renderRequest.getServerName(), renderRequest.getServerPort(), "http" );

            if ( log.isDebugEnabled() ) {
                log.debug( "request type: " + methodName );
            }
            method = null;
            if ( methodName == null || "GET".equalsIgnoreCase( methodName ) ) {
                method = getMethodGet( renderRequest, uri );
            }
            else if ("POST".equalsIgnoreCase( methodName )) {
                method = getMethodPostSimple( renderRequest, uri );
            }
            else {
                throw new PortletException(
                    "DirectHtmlPortlet handle only GET and POST method. " +
                    "Current type of method: " + methodName );
            }

            int responseCode = httpClient.executeMethod( method );

            if (responseCode>=300 && responseCode<400) {
                Header locationHeader = method.getResponseHeader("location");
                if (locationHeader!=null) {
                    switch (responseCode) {
                        case HttpStatus.SC_MOVED_TEMPORARILY:
                            method.releaseConnection();
                            method = processRedirect( locationHeader.getValue() );
                            httpClient.executeMethod( method );
                            break;
                        default:
                            throw new IOException("Redirection for status "+responseCode+" not implemented" );
                    }
                }
                else {
                    throw new IOException("New location to redirect not specified");
                }
            }

            OutputStream outputStream = renderResponse.getPortletOutputStream();

            InputStream inputStream = method.getResponseBodyAsStream();
            int ch;
            while( ( ch = inputStream.read() ) != -1 )
                outputStream.write( (byte)ch );

            outputStream.flush();

            setCookie( httpClient, renderRequest, sessionIdName );
        }
        finally {
            if (method!=null)
                method.releaseConnection();
        }
    }

    private static HttpMethodBase processRedirect( final String uri ) {
        HttpMethodBase method = new GetMethod( uri );
        method.setFollowRedirects( true );
        return method;
    }


    private static void setConnectionTimeout( HttpClient httpClient ) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout( TIMEOUT );
    }

    private static void setCookie( HttpClient httpClient, RenderRequest renderRequest, String sessionIdName ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "Start set cookie");

            Enumeration e = renderRequest.getAttributeNames();
            if ( e.hasMoreElements() ) {
                for( ; e.hasMoreElements(); ) {
                    String s = (String)e.nextElement();
                    log.debug( "renderRequest attr - " + s + ", value - " + renderRequest.getAttribute( s ) );
                }
            } else {
                log.debug( "renderRequest attr map is empty" );
            }

            e = renderRequest.getParameterNames();
            if ( e.hasMoreElements() ) {
                for( ; e.hasMoreElements(); ) {
                    String s = (String)e.nextElement();
                    log.debug( "renderRequest param - " + s + ", value - " + renderRequest.getParameter( s ) );
                }
            } else {
                log.debug( "renderRequest param map is empty" );
            }

        }

        CookieManager cookieManager = (CookieManager)renderRequest.getAttribute( PortalConstants.PORTAL_COOKIE_MANAGER_ATTRIBUTE );
        if (cookieManager==null) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Cookie manager is not found.");
                Enumeration e = renderRequest.getAttributeNames();
                if ( e.hasMoreElements() ) {
                    for( ; e.hasMoreElements(); ) {
                        String s = (String)e.nextElement();
                        log.debug( "renderRequest attr - " + s + ", value - " + renderRequest.getAttribute( s ) );
                    }
                } else {
                    log.debug( "renderRequest attr map is empty" );
                }
            }
            return;
        }
        else {
            if ( log.isDebugEnabled() ) {
                log.debug( "Cookie manager is present.");
            }
        }
        //httpClient.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);
        // Netscape Cookie Draft spec is provided for completeness
        // You would hardly want to use this spec in real life situations
        // httppclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // Compatibility policy is provided in order to mimic cookie
        // management of popular web browsers that is in some areas
        // not 100% standards compliant

        Cookie[] cookies = httpClient.getState().getCookies();
        if ( log.isDebugEnabled() ) {
            if (cookies!=null)
                log.debug( "Count of cookie in response: " + cookies.length);
            else
                log.debug( "Cookie in response not found");
        }

        for (int i=0; i<cookies.length; i++) {
            if ( log.isDebugEnabled() ) {
                log.debug( "Cookie: " + cookies[i].toExternalForm() );
            }

            if (sessionIdName.equals( cookies[i].getName() )) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Skip cookie: " + cookies[i].toExternalForm() );
                }
                continue;
            }

            javax.servlet.http.Cookie mycookie = new javax.servlet.http.Cookie(
                cookies[i].getName(), cookies[i].getValue()
            );
            mycookie.setDomain(
                cookies[i].getDomain()!=null?cookies[i].getDomain():renderRequest.getServerName()
            );
            mycookie.setPath(
//                ( GenericConfig.getContextName() != null ?GenericConfig.getContextName() :"/" )
                "/"
            );
            mycookie.setMaxAge(
                cookies[i].getExpiryDate()!=null?(int)((cookies[i].getExpiryDate().getTime()-System.currentTimeMillis())/ 1000):-1
            );
            mycookie.setSecure( cookies[i].getSecure() );

            cookieManager.addCookie( mycookie );
        }
    }

    private static void initCookie( HttpClient httpClient, RenderRequest renderRequest, String sessionIdName ) {

        javax.servlet.http.Cookie[] cookies = (javax.servlet.http.Cookie[])renderRequest.getAttribute(PortalConstants.PORTAL_COOKIES_ATTRIBUTE) ;

        // Get initial state object
        HttpState initialState = new HttpState();
        // Initial set of cookies can be retrieved from persistent storage and
        // re-created, using a persistence mechanism of choice,
        boolean isSessionCookie = false;
        for (int i=0; i<cookies.length; i++) {
            if (log.isDebugEnabled()) {
                if (cookies[i]!=null)
                    log.debug( "cookie, name: " + cookies[i].getName() + ", value: " + cookies[i].getValue() );
                else
                    log.debug( "cookie is null" );
            }
            Cookie mycookie = new Cookie(
                renderRequest.getServerName(),
                cookies[i].getName(),
                cookies[i].getValue(),
                (cookies[i].getPath()!=null?cookies[i].getPath():
                ( "/" )
//                ( GenericConfig.getContextName() != null ?GenericConfig.getContextName() :"/" )
                ),
                cookies[i].getMaxAge(),
                cookies[i].getSecure()
            );
            if (sessionIdName.equals( cookies[i].getName() ) ) {
                isSessionCookie = true;
            }

            initialState.addCookie(mycookie);
        }
        if (!isSessionCookie)
            initialState.addCookie(
                new Cookie( renderRequest.getServerName(), "JSESSIONID",
                    renderRequest.getPortletSession().getId(),
                    "/",
//                    renderRequest.getContextPath(),
                    -1, false
                )
            );

        httpClient.setState(initialState);

        // RFC 2101 cookie management spec is used per default
        // to parse, validate, format & match cookies
//        httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        // A different cookie management spec can be selected
        // when desired
        httpClient.getParams().setCookiePolicy( CookiePolicy.BROWSER_COMPATIBILITY );

    }

    private final static class PartSourceWrapper extends ByteArrayPartSource {
        private org.riverock.common.multipart.FilePart part = null;

        PartSourceWrapper( final org.riverock.common.multipart.FilePart part ){
            super(null, null);
            this.part = part;
        }

        public long getLength(){
            return part.getLength();
        }

        public String getFileName() {
            return "not-implemented-method";
        }

        public InputStream createInputStream() {
            return  part;
        }
    }

    private static HttpMethodBase getMethodGet( final RenderRequest renderRequest, String uri ) throws PortletException {
        List parameters = new LinkedList();
        Map portletParameters = renderRequest.getParameterMap();
        Iterator iterator = portletParameters.keySet().iterator();
        while( iterator.hasNext() ) {
            String name = (String)iterator.next();
            Object obj = portletParameters.get( name );
            if ( obj == null )
                continue;

            if ( log.isDebugEnabled() ) {
                log.debug( "Paramete name: " + name + ", value: " + obj );
            }

            if ( obj instanceof String ) {
                parameters.add( new NameValuePair( name, (String)obj ) );
            } else if ( obj instanceof List ) {
                Iterator it = ( (List)obj ).iterator();
                while( it.hasNext() ) {
                    parameters.add( new NameValuePair( name, it.next().toString() ) );
                }
            } else if ( obj instanceof String[] ) {
                for( int i = 0; i<( (String[])obj ).length; i++ ) {
                    parameters.add( new NameValuePair( name, ( (String[])obj )[i].toString() ) );
                }
            } else {
                throw new PortletException( "Portlet parameter type for method 'GET' must be String, List or String[]. " +
                    "Found type: " + obj.getClass().getName() );
            }
        }

        HttpMethodBase method = new GetMethod( uri );
        method.setFollowRedirects(true);

        Object[] objects = parameters.toArray();
        if ( log.isDebugEnabled() ) {
            log.debug( "Class of array: " + objects );
        }

        if ( objects != null ) {
            if ( objects.length == 0 ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Parameters not found" );
                }
            } else {
                NameValuePair[] pairs = new NameValuePair[objects.length];
                for( int i = 0; i<objects.length; i++ ) {
                    pairs[i] = (NameValuePair)objects[i];
                    if ( log.isDebugEnabled() ) {
                        log.debug( "param: " + pairs[i].toString() );
                    }
                }
                method.setQueryString( pairs );
            }
        }
        return method;
    }

    private static HttpMethodBase getMethodPost( final RenderRequest renderRequest, String uri ) throws PortletException {
        if ( log.isDebugEnabled() ) {
            log.debug( "Start prepare POST request");
        }

        PostMethod method = new PostMethod( uri );
        List parameters = new LinkedList();
        Map portletParameters = renderRequest.getParameterMap();
        Iterator iterator = portletParameters.keySet().iterator();
        while( iterator.hasNext() ) {
            String name = (String)iterator.next();
            Object obj = portletParameters.get( name );
            if ( obj == null )
                continue;

            if ( log.isDebugEnabled() ) {
                log.debug( "Paramete name: " + name + ", value: " + obj );
            }

            if ( obj instanceof String ) {
                parameters.add( new StringPart( name, (String)obj ) );
            } else if ( obj instanceof List ) {
                Iterator it = ( (List)obj ).iterator();
                while( it.hasNext() ) {
                    parameters.add( new StringPart( name, it.next().toString() ) );
                }
            } else if ( obj instanceof String[] ) {
                for( int i = 0; i<( (String[])obj ).length; i++ ) {
                    parameters.add( new StringPart( name, ( (String[])obj )[i].toString() ) );
                }
            } else if ( obj instanceof org.riverock.common.multipart.FilePart ) {
                org.riverock.common.multipart.FilePart part =
                    (org.riverock.common.multipart.FilePart)obj;
                PartSource partSource = new PartSourceWrapper( part );
                parameters.add( new FilePart( name, partSource ) );
            } else {
                throw new PortletException( "Portlet parameter type for method 'GET' must be String, List or String[]. " +
                    "Found type: " + obj.getClass().getName() );
            }
        }

        Object[] objects = parameters.toArray();
        if ( log.isDebugEnabled() ) {
            log.debug( "Class of array: " + objects );
        }

        PartBase[] parts = new PartBase[0];
        if ( objects != null ) {
            if ( objects.length == 0 ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Parameters not found" );
                }
            } else {
                parts = new PartBase[objects.length];
                for( int i = 0; i<objects.length; i++ ) {
                    parts[i] = (PartBase)objects[i];
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "Count elements in MultiPart request: " + parts.length );
        }
//        method.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true );
        method.setRequestEntity( new MultipartRequestEntity( parts, method.getParams() ) );

        return method;
    }

    private static HttpMethodBase getMethodPostSimple( final RenderRequest renderRequest, String uri ) throws PortletException {
        if ( log.isDebugEnabled() ) {
            log.debug( "Start prepare POST request");
        }

        PostMethod method = new PostMethod( uri );
        List parameters = new LinkedList();
        Map portletParameters = renderRequest.getParameterMap();
        Iterator iterator = portletParameters.keySet().iterator();
        while( iterator.hasNext() ) {
            String name = (String)iterator.next();
            Object obj = portletParameters.get( name );
            if ( obj == null )
                continue;

            if ( log.isDebugEnabled() ) {
                log.debug( "Paramete name: " + name + ", value: " + obj );
            }

            if ( obj instanceof String ) {
                parameters.add( new NameValuePair( name, (String)obj ) );
            } else if ( obj instanceof List ) {
                Iterator it = ( (List)obj ).iterator();
                while( it.hasNext() ) {
                    parameters.add( new NameValuePair( name, it.next().toString() ) );
                }
            } else if ( obj instanceof String[] ) {
                for( int i = 0; i<( (String[])obj ).length; i++ ) {
                    parameters.add( new NameValuePair( name, ( (String[])obj )[i].toString() ) );
                }
//            } else if ( obj instanceof org.riverock.common.multipart.FilePart ) {
//                org.riverock.common.multipart.FilePart part =
//                    (org.riverock.common.multipart.FilePart)obj;
//                PartSource partSource = new PartSourceWrapper( part );
//                parameters.add( new FilePart( name, partSource ) );
            } else {
                throw new PortletException( "Portlet parameter type for method 'GET' must be String, List or String[]. " +
                    "Found type: " + obj.getClass().getName() );
            }
        }

        Object[] objects = parameters.toArray();
        if ( log.isDebugEnabled() ) {
            log.debug( "Class of array: " + objects );
        }

        NameValuePair[] parts = new NameValuePair[0];
        if ( objects != null ) {
            if ( objects.length == 0 ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "Parameters not found" );
                }
            } else {
                parts = new NameValuePair[objects.length];
                for( int i = 0; i<objects.length; i++ ) {
                    parts[i] = (NameValuePair)objects[i];
                }
            }
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "Count elements in MultiPart request: " + parts.length );
        }
        method.setRequestBody( parts );

        return method;
    }
}
