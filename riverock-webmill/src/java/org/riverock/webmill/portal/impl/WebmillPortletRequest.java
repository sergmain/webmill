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

import java.util.*;
import java.security.Principal;

import javax.portlet.WindowState;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.PortalContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestWrapper;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthException;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.generic.tools.servlet.RequestDispatcherImpl;
import org.riverock.common.html.Header;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: SergeMaslyukov
 * Date: 24.12.2004
 * Time: 1:47:18
 * $Id$
 */
public class WebmillPortletRequest extends ServletRequestWrapper implements HttpServletRequest, PortletRequest {
    private final static Log log = LogFactory.getLog( WebmillPortletRequest.class );

    protected HttpServletRequest httpRequest = null;
    protected HttpServletResponse httpResponse = null;
    // parameters for current portlet
    protected Map parameters = null;
    protected PortletSession session = null;
    protected AuthSession auth = null;
    // Locale of this request
    protected Locale locale = null;
    protected Locale[] preferredLocale = null;
    protected Cookie[] cookies = null;
    private Map renderParameters = null;
    private Map portletAttributes = null;
    private ServletContext servletContext = null;

    private PortletPreferences portletPreferences = null;

    // context path of current portlet
    private String contextPath = null;

    // context path of portal servlet
    private String portalContextPath = null;
    private PortalContext portalContext = null;

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

    public WebmillPortletRequest(ServletContext servletContext, HttpServletRequest httpServletRequest, PortletPreferences portletPreferences, Map<String, List<String>> portletProperties) {
        super( httpServletRequest );
        this.servletContext = servletContext;
        this.portletPreferences = portletPreferences;
        this.portletProperties = portletProperties;
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
            this.session = new PortletSessionImpl( httpRequest.getSession(true) );
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
        return null;
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

        try {
            boolean status = auth.checkAccess( httpRequest.getServerName() );
            if ( !status )
                return false;

            return auth.isUserInRole( role );
        }
        catch( AuthException authException ) {
            log.error( "Exception in isUserInRole(), role - " + role, authException );
        }
        return false;
    }

    public Object getAttribute( String key ) {
        if (key==null)
            return null;
        return portletAttributes.get( key );
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration( portletAttributes.keySet() );
    }

    public void setAttribute( String key, Object value ) {
        if (key==null || value==null )
            return;

        portletAttributes.put( key, value );
    }

    public void removeAttribute( String key ) {
        if ( key==null )
            return;

        portletAttributes.remove( key );
    }

    public String getParameter( String key ) {
        if (key==null)
            return null;

        if (log.isDebugEnabled()) {
            log.debug("key: "+key);
        }

        if ( parameters==null && renderParameters==null ) {
            if (log.isDebugEnabled()) {
                log.debug("parameters: "+parameters+", renderParameters: " +renderParameters);
            }
            return super.getParameter( key );
        }

        String value = null;
        value = getParameterInternal( renderParameters, key );
        if ( value!=null ) {
            if (log.isDebugEnabled()) {
                log.debug("value #1: "+value);
            }
            return value;
        }

        value = getParameterInternal( parameters, key );
        if ( value!=null ) {
            if (log.isDebugEnabled()) {
                log.debug("value #2: "+value);
            }
            return value;
        }

        if (log.isDebugEnabled()) {
            log.debug("value #3: "+super.getParameter( key ));
        }
        return super.getParameter( key );
    }

    private String getParameterInternal( Map map, String key ) {

        if (map==null || key==null )
            return null;

        Object obj = map.get( key );
        if ( obj==null )
            return null;

        if ( obj instanceof List )
            return ( (List)obj ).get( 0 ).toString();

        if ( obj instanceof String[] ) {
            String[] strings = (String[])obj;
            if (strings.length>0)
                return strings[0].toString();
            else
                return null;
        }
        return obj.toString();
    }

    public Enumeration getParameterNames() {
        if ( parameters == null && renderParameters==null )
            return new Hashtable().elements();

        List set = new LinkedList();
        if (parameters!=null)
            set.addAll( parameters.keySet() );

        if (renderParameters!=null)
            set.addAll( renderParameters.keySet() );

        return Collections.enumeration( set );
    }

    public String[] getParameterValues( String key ) {
        if ( parameters==null && renderParameters==null  )
            return null;

        List<String> list = new ArrayList<String>();

        List<String> temp = null;
        temp = getParameterArray( parameters, key );
        if (temp!=null) {
            list.addAll( temp );
            temp.clear();
            temp = null;
        }

        temp = getParameterArray( renderParameters, key );
        if (temp!=null) {
            list.addAll( temp );
            temp.clear();
            temp = null;
        }

        String[] values = new String[list.size()];
        int i=0;
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String es = it.next();
            values[i++] = es;
        }
        list.clear();
        list = null;

        return values;
    }

    private static List<String> getParameterArray( Map map, String key ) {

        if (map==null || key==null )
            return null;

        Object obj = map.get( key );
        if ( obj==null )
            return null;

        List<String> list = new ArrayList<String>();
        if ( obj instanceof List ) {
            list.addAll( (List)obj );
        } else if (obj instanceof String[] ) {
            String values[] = (String[])obj;
            for (final String newVar : values) list.add(newVar.toString());
        }
        else
            list.add( obj.toString() );

        return list;
    }


    public Map getParameterMap() {
        if ( parameters==null && renderParameters==null )
            return null;

        Map map = new HashMap();
        if ( parameters!=null )
            map.putAll( parameters );

        if ( renderParameters!=null )
            map.putAll( renderParameters );

        return map;
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
        String attr = (String)super.getAttribute( "javax.servlet.include.query_string" );
        return ( attr != null ) ?attr
            :httpRequest.getQueryString();
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

    public RequestDispatcher getRequestDispatcher(String path) {
/*
        RequestDispatcher rd = super.getRequestDispatcher( path );
        if (log.isDebugEnabled()) {
            log.debug( "path: " + path );
            log.debug( "RequestDispatcher: " + rd );
        }
        return rd;
*/
        RequestDispatcher rd = servletContext.getRequestDispatcher( path );
        if ( log.isDebugEnabled() ) {
            log.debug( "ServletContext: " + servletContext );
            log.debug( "RequestDispatcher: " + rd );
        }
        return new RequestDispatcherImpl( rd );
    }

    protected void prepareRequest(
        final Map parameters, final PortalRequestInstance portalRequestInstance,
        final Map renderParameters, final Map portletAttributes,
        final String contextPath, final String portalContextPath,
        final PortalContext portalContext) {

        this.portalContext = portalContext;
        this.contextPath = contextPath;
        this.portalContextPath = portalContextPath;
        this.parameters = Collections.unmodifiableMap( parameters );
        this.renderParameters = renderParameters;
        this.portletAttributes = portletAttributes;
        this.session = new PortletSessionImpl(portalRequestInstance.getHttpRequest().getSession(true));
        this.httpRequest = portalRequestInstance.getHttpRequest();
        this.auth = portalRequestInstance.getAuth();
        this.locale = portalRequestInstance.getLocale();
        this.preferredLocale = portalRequestInstance.getPreferredLocale();

        Cookie[] c = httpRequest.getCookies();
        if (c!=null) {
            this.cookies = new Cookie[c.length];
            System.arraycopy(c, 0, this.cookies, 0, c.length);
        }
        else {
            this.cookies = new Cookie[0];
        }

        this.setAttribute( ContainerConstants.PORTAL_DEFAULT_PORTLET_TYPE_ATTRIBUTE, portalRequestInstance.getDefaultPortletDefinition() );
        this.setAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE, portalRequestInstance.getNameTemplate() );
        this.setAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE, portalRequestInstance.getPortalInfo() );
        this.setAttribute( ContainerConstants.PORTAL_COOKIES_ATTRIBUTE, cookies );
        this.setAttribute( ContainerConstants.PORTAL_QUERY_STRING_ATTRIBUTE, httpRequest.getQueryString() );
        this.setAttribute( ContainerConstants.PORTAL_QUERY_METHOD_ATTRIBUTE, httpRequest.getMethod() );
        this.setAttribute( ContainerConstants.PORTAL_URL_RESOURCE_ATTRIBUTE, portalRequestInstance.getUrlResource() );
        this.setAttribute( ContainerConstants.PORTAL_COOKIE_MANAGER_ATTRIBUTE, portalRequestInstance.getCookieManager() );
        this.setAttribute( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH, this.portalContextPath );

        this.setAttribute( ContainerConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE, httpRequest.getRemoteAddr() );
        this.setAttribute( ContainerConstants.PORTAL_USER_AGENT_ATTRIBUTE, Header.getUserAgent(httpRequest) );
    }
}
