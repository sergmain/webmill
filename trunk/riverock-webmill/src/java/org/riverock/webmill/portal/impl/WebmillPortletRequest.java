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
package org.riverock.webmill.portal.impl;

import java.security.Principal;
import java.util.*;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.portlet.PortletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.tools.servlet.RequestDispatcherImpl;
import org.riverock.generic.tools.servlet.ServletRequestWrapperInclude;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portal.PortalRequestInstance;

/**
 * User: SergeMaslyukov
 * Date: 24.12.2004
 * Time: 1:47:18
 * $Id$
 */
public class WebmillPortletRequest extends ServletRequestWrapper implements HttpServletRequest, PortletRequest {
    private final static Logger log = Logger.getLogger( WebmillPortletRequest.class );

    protected HttpServletRequest httpRequest = null;
    protected HttpServletResponse httpResponse = null;
    // parameters for current portlet
    protected Map<String, List<String>> parameters = null;
    protected PortletSession session = null;
    protected AuthSession auth = null;
    // Locale of this request
    protected Locale locale = null;
    protected Locale[] preferredLocale = null;
    protected Cookie[] cookies = null;
    private Map<String, List<String>> renderParameters = null;
    private Map<String, Object> portletAttributes = null;
    private ServletContext servletContext = null;

    private PortletPreferences portletPreferences = null;
    private ServletRequest wrapper = null;

    // context path of current portlet
    private String contextPath = null;

    private PortalContext portalContext = null;
    private PortletContext portletContext = null;

    private Map<String, List<String>> portletProperties = null;

    public void destroy() {
        httpRequest = null;
        httpResponse = null;
        parameters = null;
        session = null;
        auth = null;
        locale = null;
        preferredLocale = null;
        cookies = null;
        renderParameters = null;
        portalContext = null;
    }

    public WebmillPortletRequest(ServletContext servletContext, HttpServletRequest httpServletRequest, PortletPreferences portletPreferences, Map<String, List<String>> portletProperties,final Map<String, Object> portletAttributes, Map<String, List<String>> renderParameters, PortletContext portletContext) {
        super( new ServletRequestWrapperInclude(httpServletRequest, null, portletAttributes) );
        wrapper = super.getRequest();
        this.portletAttributes = portletAttributes;
        this.servletContext = servletContext;
        this.portletContext = portletContext;
        this.portletPreferences = portletPreferences;
        this.portletProperties = portletProperties;
        this.renderParameters = renderParameters;
    }

    public ServletRequest getRequest() {
        return wrapper;
    }

    public void setRequest(ServletRequest servletRequest) {
        wrapper = servletRequest;
    }

    public boolean isWindowStateAllowed( WindowState windowState ) {
        return false;
    }

    public boolean isPortletModeAllowed( PortletMode portletMode ) {
        return false;
    }

    public PortletMode getPortletMode() {
        return PortletMode.VIEW;
    }

    public WindowState getWindowState() {
        return WindowState.NORMAL;
    }

    public PortletPreferences getPreferences() {
        return portletPreferences;
    }

    public PortletSession getPortletSession() {
        return session;
    }

    public PortletSession getPortletSession( boolean b ) {
        if ( !httpRequest.isRequestedSessionIdValid() ) {
            this.session = null;
            this.session = new PortletSessionImpl( httpRequest.getSession(true), portletContext );
        }
        return session;
    }

    public String getProperty( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values!=null) {
            return values.get(0);
        }
        else {
            return null;
        }
    }

    public Enumeration getProperties( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values==null) {
            values = new ArrayList<String>();
        }
        return Collections.enumeration( values );
    }

    public Enumeration getPropertyNames() {
        return Collections.enumeration( portletProperties.keySet() );
    }

    public PortalContext getPortalContext() {
        return portalContext;
    }

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return httpRequest.getCookies();
    }

    public long getDateHeader( String name ) {
        return httpRequest.getDateHeader( name );
    }

    public String getHeader( String name ) {
        return httpRequest.getHeader( name );
    }

    public Enumeration getHeaders( String name ) {
        return httpRequest.getHeaders( name );
    }

    public Enumeration getHeaderNames() {
        return httpRequest.getHeaderNames();
    }

    public int getIntHeader( String name ) {
        return httpRequest.getIntHeader( name );
    }

    public String getMethod() {
        return httpRequest.getMethod();
    }

    public String getContextPath() {
        return contextPath;
//        final String realPath = servletContext.getRealPath("/");
//        File dir = new File(realPath);
//        return dir.getName();
//        return super.getContextPath();
    }

    public String getRemoteUser() {
        if ( auth == null )
            return null;

        return auth.getUserLogin();
    }

    public Principal getUserPrincipal() {
        if ( auth == null )
            return null;

        return auth;
    }

    public boolean isUserInRole( String role ) {
        if (role==null) {
            return false;
        }

        if (role.equals(PortalConstants.WEBMILL_GUEST_ROLE)) {
            return true;
        }

        if ( httpRequest.getServerName() == null || auth == null ) {
            return false;
        }

        boolean status = auth.checkAccess( httpRequest.getServerName() );
        if ( !status )
            return false;

        return auth.isUserInRole( role );
    }

    public Object getAttribute( String key ) {
        if (log.isDebugEnabled()) {
            log.debug("getAttribute(), key: " + key + ", this: " + this);
        }
        if (key==null) {
            throw new IllegalArgumentException("Call getAttribute() with key==null");
        }

        Object o = portletAttributes.get( key );
        if (o!=null) {
            return o;
        }

        return getRequest().getAttribute(key);
    }

    public Enumeration getAttributeNames() {
/*
        List keys = Collections.list(getRequest().getAttributeNames());
        keys.addAll( portletAttributes.keySet() );
        return Collections.enumeration( keys );
*/
        return Collections.enumeration( portletAttributes.keySet() );
    }

    public void setAttribute( String key, Object value ) {
        if (log.isDebugEnabled()) {
            log.debug("set attribute. key: " + key + ", value: " + value+", this: "+this);
        }
        
        if (key==null) {
            throw new IllegalArgumentException("Call setAttribute() with key==null");
        }

        if ( value==null ) {
            removeAttribute( key );
        }
//        getRequest().setAttribute(key, value);
        portletAttributes.put( key, value );
    }

    public void removeAttribute( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call removeAttribute() with null");
        }

//        getRequest().removeAttribute( key );
        portletAttributes.remove( key );
    }

    public String getParameter( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call getParameter() with null");
        }

        if ( parameters==null && renderParameters==null ) {
            return null;
        }

        String value = getParameterInternal( renderParameters, key );
        if ( value!=null ) {
            return value;
        }

        value = getParameterInternal( parameters, key );
        return value;
    }

    private String getParameterInternal( Map<String, List<String>> map, String key ) {

        if (map==null || key==null )
            return null;

        List<String> obj = map.get( key );
        if ( obj==null )
            return null;

        return obj.get(0);

    }

    public Enumeration getParameterNames() {
        if ( parameters == null && renderParameters==null ) {
            // Todo may be exists better solution?
            return new Hashtable().elements();
        }

        List<String> set = new ArrayList<String>();
        if (parameters!=null)
            set.addAll( parameters.keySet() );

        if (renderParameters!=null)
            set.addAll( renderParameters.keySet() );

        dumpMap(renderParameters, "getParameterNames(), renderParameters");
        return Collections.enumeration( set );
    }

    public String[] getParameterValues( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call getParameterValues() with null");
        }

        if ( parameters==null && renderParameters==null  )
            return null;

        List<String> list = new ArrayList<String>();

        List<String> temp = getParameterArray( parameters, key );
        if (temp!=null) {
            list.addAll( temp );
        }

        temp = getParameterArray( renderParameters, key );
        if (temp!=null) {
            list.addAll( temp );
        }

        String[] values = list.toArray(new String[0]);

        if (log.isDebugEnabled()) {
            log.debug("getParameterValues(): " + StringTools.arrayToString(values));
        }

        dumpMap(renderParameters, "getParameterValues(), renderParameters");
        return values;
    }

    private static List<String> getParameterArray( Map<String, List<String>> map, String key ) {

        if (map==null || key==null )
            return null;

        List<String> list = map.get( key );
        if ( list==null )
            return null;

        return list;
    }


    public Map getParameterMap() {
        dumpMap(renderParameters, "getParameterMap(), renderParameters");
        dumpMap(parameters, "getParameterMap(), parameters");

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if ( parameters!=null )
            map.putAll( parameters );

        if ( renderParameters!=null ) {
            for ( Map.Entry<String, List<String>> entry : renderParameters.entrySet()) {
                List<String> list = map.get(entry.getKey());
                if (list!=null) {
                    if (log.isDebugEnabled()) {
                        log.debug("getParameterMap() #1.1, key: "+entry.getKey()+", value: " + StringTools.arrayToString(list.toArray(new String[0])));
                        log.debug("getParameterMap() #1.2, key: "+entry.getKey()+", value: " + StringTools.arrayToString(entry.getValue().toArray(new String[0])));
                    }
                    list.addAll(entry.getValue());
                    if (log.isDebugEnabled()) {
                        log.debug("getParameterMap() #1.3, key: "+entry.getKey()+", value: " + StringTools.arrayToString(list.toArray(new String[0])));
                    }
                    map.put(entry.getKey(), list);
                }
                else {
                    if (log.isDebugEnabled()) {
                        log.debug("getParameterMap() #2, key: "+entry.getKey()+", value: " + StringTools.arrayToString(entry.getValue().toArray(new String[0])));
                    }
                    map.put( entry.getKey(), entry.getValue() );
                }
            }
        }
        Map<String, String[]> result = new HashMap<String, String[]>();
        for ( Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (log.isDebugEnabled()) {
                log.debug("getParameterMap() #3, key: "+entry.getKey()+", value: " + StringTools.arrayToString(entry.getValue().toArray(new String[0])));
            }
            result.put( entry.getKey(), entry.getValue().toArray(new String[0]));
        }

        return result;
    }

    public boolean isSecure() {
        return httpRequest.isSecure();
    }

    public String getRequestedSessionId() {
        return httpRequest.getRequestedSessionId();
    }

    public boolean isRequestedSessionIdValid() {
        return httpRequest.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return httpRequest.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return httpRequest.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return httpRequest.isRequestedSessionIdFromURL();
    }

    public String getResponseContentType() {
        return null;
    }

    public Enumeration getResponseContentTypes() {
        return null;
    }

    public Locale getLocale() {
        return locale;
    }

    public Enumeration getLocales() {
        return Collections.enumeration( Arrays.asList( preferredLocale ) );
    }

    public String getScheme() {
        return httpRequest.getScheme();
    }

    public String getServerName() {
        return httpRequest.getServerName();
    }

    public int getServerPort() {
        return httpRequest.getServerPort();
    }


    public String getPathInfo() {
        String attr = (String)super.getAttribute( "javax.servlet.include.path_info" );
        if (log.isDebugEnabled()) {
            log.debug( "path_info in attr: "+attr );
            log.debug( "super.path_info: "+httpRequest.getPathInfo() );
        }
        return attr;
//        return ( attr != null ) ?attr
//            :super.getPathInfo();
    }

    public String getQueryString() {
        String queryString = (String)super.getAttribute( "javax.servlet.include.query_string" );

        if (log.isDebugEnabled()) {
            log.debug("queryString: " + queryString);
            log.debug("httpRequest: " + httpRequest);
            if (httpRequest!=null) {
                log.debug("httpRequest.getQueryString(): " + httpRequest.getQueryString());
            }
        }

        if ( queryString != null )
            return queryString;

        if (httpRequest!=null)
            return httpRequest.getQueryString();
        else
            return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getRequestURI() {
        String attr = (String)super.getAttribute( "javax.servlet.include.request_uri" );
        if (log.isDebugEnabled()) {
            log.debug( "request_uri in attr: "+attr );
            log.debug( "super.request_uri: "+httpRequest.getRequestURI() );
        }
        return ( attr != null ) ?attr
            :httpRequest.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getServletPath() {
        String attr = (String)super.getAttribute( "javax.servlet.include.servlet_path" );
        if (log.isDebugEnabled()) {
            log.debug( "servlet_path in attr: "+attr );
            log.debug( "super.servlet_path: "+httpRequest.getServletPath() );
        }
        return ( attr != null ) ?attr
            :httpRequest.getServletPath();
    }

    public HttpSession getSession( boolean create ) {
        return httpRequest.getSession( create );
    }

    public HttpSession getSession() {
        return httpRequest.getSession();
    }

    public int getContentLength() {
        return 0;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        RequestDispatcher rd = servletContext.getRequestDispatcher( path );
        if ( log.isDebugEnabled() ) {
            log.debug( "ServletContext: " + servletContext );
            log.debug( "RequestDispatcher: " + rd );
        }
        return new RequestDispatcherImpl( rd );
    }

    protected void prepareRequest(
        final Map<String, List<String>> parameters, final PortalRequestInstance portalRequestInstance,
        final String contextPath,
        final PortalContext portalContext ) {

        this.portalContext = portalContext;
        this.contextPath = contextPath;
        this.parameters = Collections.unmodifiableMap( parameters );
        this.session = new PortletSessionImpl(portalRequestInstance.getHttpRequest().getSession(true), portletContext);
        this.httpRequest = portalRequestInstance.getHttpRequest();
        this.auth = portalRequestInstance.getAuth();
        this.locale = portalRequestInstance.getLocale();
        this.preferredLocale = portalRequestInstance.getPreferredLocales();

        Cookie[] c = httpRequest.getCookies();
        if (c!=null) {
            this.cookies = new Cookie[c.length];
            System.arraycopy(c, 0, this.cookies, 0, c.length);
        }
        else {
            this.cookies = new Cookie[0];
        }

        this.setAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER, portalRequestInstance.getPortalDaoProvider() );

        this.setAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE, portalRequestInstance.getRequestContext().getTemplateName() );
        this.setAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE, portalRequestInstance.getPortalInfo() );
        this.setAttribute( ContainerConstants.PORTAL_COOKIES_ATTRIBUTE, cookies );

        // PORTAL_QUERY_STRING_ATTRIBUTE constants can be deleted 
        // after rewrite invoke method in member module with 'lookup' type
        this.setAttribute( ContainerConstants.PORTAL_QUERY_STRING_ATTRIBUTE, httpRequest.getQueryString() );
        this.setAttribute( ContainerConstants.PORTAL_QUERY_METHOD_ATTRIBUTE, httpRequest.getMethod() );

        this.setAttribute( ContainerConstants.PORTAL_COOKIE_MANAGER_ATTRIBUTE, portalRequestInstance.getCookieManager() );

        this.setAttribute( ContainerConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE, httpRequest.getRemoteAddr() );
        this.setAttribute( ContainerConstants.PORTAL_USER_AGENT_ATTRIBUTE, Header.getUserAgent(httpRequest) );
    }

    private static void dumpMap(Map<String, List<String>> params, String info) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug(info);
        if (params ==null) {
            log.debug("    Map is null");
            return;
        }
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            List<String> list = entry.getValue();
            log.debug("    key: " + entry.getKey());
            if (list==null) {
                log.debug("    list value is null");
                continue;
            }
            if (list.isEmpty()) {
                log.debug("    list value is empty");
                continue;
            }

            for (String anArr : list) {
                log.debug("        value: " + anArr);
            }
        }
    }
}
