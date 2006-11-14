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
package org.riverock.webmill.portal.impl;

import java.security.Principal;
import java.util.*;

import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.StringTools;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.tools.servlet.RequestDispatcherImpl;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.SecurityRoleRef;
import org.riverock.webmill.container.portlet.bean.Supports;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.action.PortalActionExecutorImpl;
import org.riverock.webmill.portal.mail.PortalMailServiceProviderImpl;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.namespace.NamespaceMapper;
import org.riverock.webmill.portal.namespace.NamespaceMapperImpl;
import org.riverock.webmill.portal.user.PortalUserManagerImpl;

/**
 *
 * Part of code used from Apache Pluto project, License Apache2
 *
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
    private ServletContext servletContext = null;

    private PortletPreferences portletPreferences = null;

    // context path of current portlet
    private String contextPath = null;

    private PortalContext portalContext = null;
    private PortletContext portletContext = null;

    private Map<String, List<String>> portletProperties = null;
    private PortletDefinition portletDefinition=null;
    private Namespace namespace=null;
    private NamespaceMapper mapper = new NamespaceMapperImpl();

    protected boolean included = false;
    protected String includedQueryString = null;
    protected Map<String, List<String>> includedParameters = null;

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
        includedParameters=null;
    }

    public WebmillPortletRequest(
        final ServletContext servletContext, final HttpServletRequest httpServletRequest,
        final PortletPreferences portletPreferences, final Map<String, List<String>> portletProperties,
        final Map<String, List<String>> renderParameters,
        final PortletContext portletContext,
        final PortletDefinition portletDefinition,
        final Namespace namespace
    ) {

        super( httpServletRequest );

        this.namespace = namespace;
        this.portletDefinition = portletDefinition;
        this.servletContext = servletContext;
        this.portletContext = portletContext;
        this.portletPreferences = portletPreferences;
        this.portletProperties = portletProperties;
        this.renderParameters = renderParameters;
    }

    public boolean isWindowStateAllowed( WindowState windowState ) {
        for (Enumeration en = portalContext.getSupportedWindowStates();
                en.hasMoreElements(); ) {
            if (en.nextElement().toString().equals(windowState.toString())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPortletModeAllowed( PortletMode portletMode ) {
        return (isPortletModeAllowedByPortlet(portletMode) && isPortletModeAllowedByPortal(portletMode));
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
        return getPortletSession(true);
    }

    public PortletSession getPortletSession( boolean create ) {

        if (log.isDebugEnabled()) {
            log.debug("Retreiving portlet session (create=" + create + ")");
        }

        //
        // It is critical that we don't retrieve the portlet session until the
        //   cross context dispatch has been completed.  If we do then we risk
        //   having a cached version which is invalid for the context within
        //   which it exists.
        //
        if (portletContext == null) {
            throw new IllegalStateException("PortletContext is null");
        }

        if ( !httpRequest.isRequestedSessionIdValid() ) {
            this.session = null;
            this.session = new PortletSessionImpl( httpRequest.getSession(true), portletContext, namespace );
            return session;
        }

        //
        // We must make sure that if the session has been invalidated (perhaps
        //   through setMaxIntervalTimeout()) and the underlying request
        //   returns null that we no longer use the cached version.
        // We have to check (ourselves) if the session has exceeded its max
        //   inactive interval. If so, we should invalidate the underlying
        //   HttpSession and recreate a new one (if the create flag is set to
        //   true) -- We just cannot depend on the implementation of
        //   javax.servlet.http.HttpSession!
        //
        HttpSession httpSession = httpRequest.getSession(create);
        if (httpSession != null) {
            int maxInactiveInterval = httpSession.getMaxInactiveInterval();
            if (maxInactiveInterval >= 0) {    // < 0 => Never expires.
            	long maxInactiveTime = httpSession.getMaxInactiveInterval() * 1000L;
            	long currentInactiveTime = System.currentTimeMillis() - httpSession.getLastAccessedTime();
            	if (currentInactiveTime > maxInactiveTime) {
            		if (log.isDebugEnabled()) {
            			log.debug("The underlying HttpSession is expired and should be invalidated.");
            		}
            		httpSession.invalidate();
            		httpSession = httpRequest.getSession(create);
            	}
            }
        }
        if (httpSession == null) {
            if (log.isDebugEnabled()) {
                log.debug("The underlying HttpSession is not available: no session will be returned.");
            }
            return null;
        }
        //
        // If we reach here, we are sure that the underlying HttpSession is
        //   available. If we haven't created and cached a portlet session
        //   instance, we will create and cache one now.
        //
        if (this.session == null) {
        	if (log.isDebugEnabled()) {
        		log.debug("Creating new portlet session...");
        	}
            this.session = new PortletSessionImpl( httpSession, portletContext, namespace );
        }
        return session;
    }

    /**
     * Returns the value of the specified request property
     * as a <code>String</code>. If the request did not include a property
     * of the specified name, this method returns <code>null</code>.
     * <p>
     * A portlet can access portal/portlet-container specific properties
     * through this method and, if available, the
     * headers of the HTTP client request.
     * <p>
     * This method should only be used if the
     * property has only one value. If the property might have
     * more than one value, use {@link #getProperties}.
     * <p>
     * If this method is used with a multivalued
     * parameter, the value returned is equal to the first value
     * in the Enumeration returned by <code>getProperties</code>.
     *
     * @param key		a <code>String</code> specifying the
     *				property name
     *
     * @return			a <code>String</code> containing the
     *				value of the requested
     *				property, or <code>null</code>
     *				if the request does not
     *				have a property of that name.
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if name is <code>null</code>.
     */
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

    /**
     * Returns all the values of the specified request property
     * as a <code>Enumeration</code> of <code>String</code> objects.
     * <p>
     * If the request did not include any propertys
     * of the specified name, this method returns an empty
     * <code>Enumeration</code>.
     * The property name is case insensitive. You can use
     * this method with any request property.
     *
     * @param key		a <code>String</code> specifying the
     *				property name
     *
     * @return		a <code>Enumeration</code> containing
     *                  	the values of the requested property. If
     *                  	the request does not have any properties of
     *                  	that name return an empty <code>Enumeration</code>.
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if name is <code>null</code>.
     */
    public Enumeration getProperties( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values==null) {
            values = Collections.emptyList();
        }
        return Collections.enumeration( values );
    }

    /**
     *
     * Returns a <code>Enumeration</code> of all the property names
     * this request contains. If the request has no
     * properties, this method returns an empty <code>Enumeration</code>.
     *
     *
     * @return			an <code>Enumeration</code> of all the
     *				property names sent with this
     *				request; if the request has
     *				no properties, an empty <code>Enumeration</code>.
     */
    public Enumeration getPropertyNames() {
        Set<String> names = portletProperties.keySet();
        if (names==null) {
            names = Collections.emptySet();
        }
        return Collections.enumeration(names);
    }

    public PortalContext getPortalContext() {
        return portalContext;
    }

    /**
     * Returns the name of the authentication scheme used for the
     * connection between client and portal,
     * for example, <code>BASIC_AUTH</code>, <code>CLIENT_CERT_AUTH</code>,
     * a custom one or <code>null</code> if there was no authentication.
     *
     * @return		one of the static members <code>BASIC_AUTH</code>,
     *			<code>FORM_AUTH</code>, <code>CLIENT_CERT_AUTH</code>,
     *                    <code>DIGEST_AUTH</code> (suitable for == comparison)
     *			indicating the authentication scheme,
     *                    a custom one, or
     *			<code>null</code> if the request was
     *			not authenticated.
     */
    public String getAuthType() {
        if ( auth == null )
            return null;

        return HttpServletRequest.FORM_AUTH;
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

    private Map<String, Boolean> userRoles = new HashMap<String, Boolean>();

    public boolean isUserInRole( String role ) {
        if (log.isDebugEnabled()) {
            log.debug("isUserInRole()");
            log.debug("    role: " + role);
        }
        if (role==null) {
            return false;
        }

        Boolean access = userRoles.get(role);
        if (access!=null) {
            return access;
        }

        if (role.equals(PortalConstants.WEBMILL_GUEST_ROLE)) {
            return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("    serverName: " + httpRequest.getServerName());
            log.debug("    auth: " + auth);
        }

        if (httpRequest.getServerName()==null || auth==null) {
            return role.equals(PortalConstants.WEBMILL_ANONYMOUS_ROLE);
        }

        // here auth always not null. return false for webmill.anonymous role
        if (role.equals(PortalConstants.WEBMILL_ANONYMOUS_ROLE)) {
            return false;
        }

        boolean status = auth.checkAccess( httpRequest.getServerName() );
        if ( !status ) {
            userRoles.put(role, false);
            return false;
        }

        if (role.equals(PortalConstants.WEBMILL_AUTHENTIC_ROLE)) {
            userRoles.put(PortalConstants.WEBMILL_AUTHENTIC_ROLE, true);
            return true;
        }

        PortletDefinition def = portletDefinition;

        SecurityRoleRef ref = null;
        for (SecurityRoleRef r : def.getSecurityRoleRef()){
            if (r.getRoleName().equals(role)) {
                ref = r;
                break;
            }
        }

        String link;
        if (ref != null && ref.getRoleLink() != null) {
            link = ref.getRoleLink();
        } else {
            link = role;
        }

        boolean roleRefAccess = auth.isUserInRole( link );
        userRoles.put(role, roleRefAccess);

        return roleRefAccess;
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    public Object getAttribute( String key ) {
/*
        if (log.isDebugEnabled()) {
            log.debug("getAttribute(), key: " + key + ", this: " + this);
        }
*/
        if (key==null) {
            throw new IllegalArgumentException("Call getAttribute() with key==null");
        }

        String encodedName = isNameReserved(key) ?
                key :
                mapper.encode(namespace, key);

        Object attribute = getHttpServletRequest().getAttribute(encodedName);

        if (attribute == null) {
            attribute = getHttpServletRequest().getAttribute(key);
        }
        return attribute;
    }

    public Enumeration<String> getAttributeNames() {
        Enumeration attributes = this.getHttpServletRequest().getAttributeNames();
        List<String> portletAttributes = new ArrayList<String>();
        while (attributes.hasMoreElements()) {
            String attribute = (String) attributes.nextElement();

            String portletAttribute = mapper.decode(namespace, attribute);

/*
            if (log.isDebugEnabled()) {
                log.debug("getAttributeNames(), attr names: "+attribute);
            }
*/
            if (portletAttribute != null) { // it is in the portlet's namespace
                portletAttributes.add(portletAttribute);
            }
        }

        return Collections.enumeration( portletAttributes );
    }

    public void setAttribute( String key, Object value ) {
        if (key==null) {
            throw new IllegalArgumentException("Call setAttribute() with key==null");
        }

        String encodedName = isNameReserved(key) ?
                key : mapper.encode(namespace, key);

/*
        if (log.isDebugEnabled()) {
            log.debug("setAttribute(), key: " + key+", value: " +value+", isReserved: "+ isNameReserved(key)+", encodedName: " +encodedName );
        }
*/

        if (value == null) {
            removeAttribute(key);
        } else {
            getHttpServletRequest().setAttribute(encodedName, value);
        }
/*
        if (log.isDebugEnabled()) {
            boolean isFound = false;
            log.debug("Portlet request: "+ this);
            log.debug("Portlet request attributes: ");
            for (Enumeration e = getHttpServletRequest().getAttributeNames(); e.hasMoreElements();) {
                String key1 = (String) e.nextElement();
                log.debug("    key: " + key1 + ", value: " + getHttpServletRequest().getAttribute(key1));
                isFound = true;
            }
            if (!isFound) {
                log.debug("Attribute in portlet request is present: " + isFound);
            }
        }
*/
    }

    public void removeAttribute( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call removeAttribute() with null");
        }
        String encodedName = isNameReserved(key) ? key : mapper.encode(namespace, key);
        getHttpServletRequest().removeAttribute(encodedName);
    }

    public String getParameter( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call getParameter() with null");
        }

        if ( parameters==null && renderParameters==null && includedParameters==null ) {
            return null;
        }

        String value = getParameterInternal( renderParameters, key );
        if ( value!=null ) {
            return value;
        }

        value = getParameterInternal( includedParameters, key );
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
        if ( parameters == null && renderParameters==null && includedParameters==null) {
            return new Hashtable(0).elements();
        }

        List<String> list = new ArrayList<String>();
        if (parameters!=null) {
            list.addAll( parameters.keySet() );
        }

        if (renderParameters!=null) {
            list.addAll( renderParameters.keySet() );
        }

        if (includedParameters!=null) {
            list.addAll( includedParameters.keySet() );
        }

//        dumpMap(renderParameters, "getParameterNames(), renderParameters");
        return Collections.enumeration( Collections.unmodifiableList(list) );
    }

    public String[] getParameterValues( String key ) {
        if (key==null) {
            throw new IllegalArgumentException("Call getParameterValues() with null");
        }

        if ( parameters==null && renderParameters==null && includedParameters==null ) {
            return null;
        }

        List<String> list = new ArrayList<String>();

        List<String> temp = getParameterArray( parameters, key );
        if (temp!=null) {
            list.addAll( temp );
        }

        temp = getParameterArray( includedParameters, key );
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

//        dumpMap(renderParameters, "getParameterValues(), renderParameters");
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
//        dumpMap(renderParameters, "getParameterMap(), renderParameters");
//        dumpMap(parameters, "getParameterMap(), parameters");

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if ( parameters!=null )
            map.putAll( parameters );

        if ( renderParameters!=null ) {
            for ( Map.Entry<String, List<String>> entry : renderParameters.entrySet()) {
                List<String> list = map.get(entry.getKey());
                if (list!=null) {
                    List<String> tempList = new ArrayList<String>(list);
                    tempList.addAll(entry.getValue());
                    map.put(entry.getKey(), tempList);
                }
                else {
                    map.put( entry.getKey(), entry.getValue() );
                }
            }
        }
        if ( includedParameters!=null ) {
            for ( Map.Entry<String, List<String>> entry : includedParameters.entrySet()) {
                List<String> list = map.get(entry.getKey());
                if (list!=null) {
                    List<String> tempList = new ArrayList<String>(list);
                    tempList.addAll(entry.getValue());
                    map.put(entry.getKey(), tempList);
                }
                else {
                    map.put( entry.getKey(), entry.getValue() );
                }
            }
        }
        Map<String, String[]> result = new HashMap<String, String[]>();
        for ( Map.Entry<String, List<String>> entry : map.entrySet()) {
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
        String queryString = null;
        int index = path.indexOf("?");
        if (index > 0 && index < path.length() - 1) {
            queryString = path.substring(index + 1);
            log.debug("queryString: " + queryString);
        }

        return new RequestDispatcherImpl( rd, queryString );
    }

    protected void prepareRequest(
        final Map<String, List<String>> parameters, final PortalRequestInstance portalRequestInstance,
        final String contextPath,
        final PortalContext portalContext ) {

        this.portalContext = portalContext;
        this.contextPath = contextPath;
        this.parameters = Collections.unmodifiableMap( parameters );
        this.session = new PortletSessionImpl(portalRequestInstance.getHttpRequest().getSession(true), portletContext, namespace);
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

        PortalMailServiceProviderImpl mailServiceProvider = new PortalMailServiceProviderImpl(
            portalContext.getProperty( ContainerConstants.PORTAL_PROP_SMTP_HOST ),
            portalRequestInstance.getPortalInfo().getSite().getAdminEmail()
        );
        this.setAttribute(
            ContainerConstants.PORTAL_PORTAL_MAIL_SERVICE_PROVIDER,
            mailServiceProvider
        );

/*
        if (log.isDebugEnabled()) {
            log.debug("metadata map: "+portletMetadata );
            if (portletMetadata == null) {
                log.debug("portlet metadata is null");
            } else {
                log.debug("portlet metadata:");
                for (Map.Entry<String, String> entry : portletMetadata.entrySet()) {
                    log.debug("    key: " + entry.getKey() + ", value: " + entry.getValue());
                }
            }
        }
*/

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        this.setAttribute(
            ContainerConstants.PORTAL_PORTAL_USER_MANAGER,
            new PortalUserManagerImpl(
                portalRequestInstance.getPortalInfo().getSite().getSiteId(),
                new Long(portalContext.getProperty( ContainerConstants.PORTAL_PROP_COMPANY_ID )),
                mailServiceProvider,
                portletPreferences,
                classLoader
            )
        );
//        public PortalActionExecutorImpl(ClassLoader portalClassLoader, Long siteId,
// String applicationPath, String virtualHostUrl, String portalContext) {
        this.setAttribute(
            ContainerConstants.PORTAL_PORTAL_ACTION_EXECUTOR,
            new PortalActionExecutorImpl(
                classLoader,
                portalRequestInstance.getPortalInfo().getSite().getSiteId(),
                PropertiesProvider.getApplicationPath(),
                buildVirtualHostUrl(httpRequest),
                portalContext.getProperty( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH )
            )
        );
    }

    private String buildVirtualHostUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getScheme()).append("://").append(req.getServerName());
        if (req.getServerPort()!=80) {
            sb.append(req.getServerPort());
        }
        return sb.toString();
    }

    // Private methods

    private boolean isNameReserved(String name) {
        return false;
//        return name.startsWith("java.") || name.startsWith("javax.");
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

    private boolean isPortletModeAllowedByPortlet(PortletMode mode) {
        if (isPortletModeMandatory(mode)) {
            return true;
        }

        for (Supports modes : portletDefinition.getSupports()) {
            for (String m : modes.getPortletMode()) {
                if (m.equals(mode.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPortletModeAllowedByPortal(PortletMode mode) {
        Enumeration supportedModes = portalContext.getSupportedPortletModes();
        while (supportedModes.hasMoreElements()) {
            if (supportedModes.nextElement().toString().equals(
                    (mode.toString()))) {
                return true;
            }
        }
        return false;
    }

    private boolean isPortletModeMandatory(PortletMode mode) {
        return PortletMode.VIEW.equals(mode) || PortletMode.EDIT.equals(mode) || PortletMode.HELP.equals(mode);
    }

}
