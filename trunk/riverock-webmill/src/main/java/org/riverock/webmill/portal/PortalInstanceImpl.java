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
package org.riverock.webmill.portal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.webmill.container.portlet.PortalInstance;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerFactory;
import org.riverock.webmill.container.tools.PortletContainerUtils;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.webmill.utils.PortletUtils;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 18:00:18
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class PortalInstanceImpl implements PortalInstance  {
    private final static Logger log = Logger.getLogger(PortalInstanceImpl.class);

    private static final String WEBMILL_PROPERTIES = "/org/riverock/webmill/portal/webmill.properties";
    private static final PortalVersion portalVersion = new PortalVersion( getPortalVersion() );
    private static final String PORTAL_INFO = "WebMill/"+getPortalVersion();
    private static String PORTAL_VERSION = null;
    private static final String COPYRIGHT =
        "<!--\n" +
        "  Portal: "+PORTAL_INFO +"\n"+
        "Homepage: http://webmill.riverock.org\n" +
        "-->\n";

    private static final int NUM_LINES = 300;

    private ServletConfig portalServletConfig = null;
    private PortletContainer portletContainer = null;
    private Collection<String> supportedList = null;
    private static final String UNKNOWN_PORTAL_VERSON = "0.0.1";

    private static final Collection<String> destroyedPortletName = new ConcurrentLinkedQueue<String>();

    public void destroy() {
        portalServletConfig = null;
        portletContainer = null;
        if (supportedList!=null) {
            supportedList.clear();
            supportedList = null;
        }
    }

    private static class PortalVersion {
        int major;
        int minor;

        PortalVersion( String version ) {
            StringTokenizer st = new StringTokenizer( version, "." );
            major = new Integer( st.nextToken() );
            minor = new Integer( st.nextToken() );
        }
    }

    public String getPortalName() {
        return PORTAL_INFO + getPortalVersion();
    }

    private static String getPortalVersion() {
        if (PORTAL_VERSION!=null) {
            return PORTAL_VERSION;
        }
        synchronized(PortalInstanceImpl.class) {
            if (PORTAL_VERSION!=null) {
                return PORTAL_VERSION;
            }
            Properties pr = new Properties();
            try {
                pr.load(PortalInstanceImpl.class.getResourceAsStream(WEBMILL_PROPERTIES));
                String version = pr.getProperty("portal.version");
                if (StringUtils.isBlank(version)) {
                    String es = "Value for property 'portal.version' not found";
                    log.error(es);
                    PORTAL_VERSION = UNKNOWN_PORTAL_VERSON;
                }
                else {
                    if (version.equals("${pom.version}")) {
                        PORTAL_VERSION = UNKNOWN_PORTAL_VERSON;
                    }
                    else {
                        PORTAL_VERSION = version;
                    }
                }
            } catch (IOException e) {
                String es = "Error load webmill.properties files.";
                log.error(es, e);
                PORTAL_VERSION = UNKNOWN_PORTAL_VERSON;
            }
        }
        return PORTAL_VERSION;
    }

    public static PortalInstanceImpl getInstance( ServletConfig servletConfig ) {
        return new PortalInstanceImpl( servletConfig );
    }

    public int getPortalMajorVersion() {
        return portalVersion.major;
    }

    public int getPortalMinorVersion() {
        return portalVersion.minor;
    }

    private static class InternalServletRequestWrapper extends HttpServletRequestWrapper {

        // all methos in HttpServletRequest must invoked only from ContextNavigator
        // all others invokes are wrong
        boolean isOk = false;

        public InternalServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public Enumeration getAttributeNames() {
            if ( !isOk ) {
                log.warn( "!!! Requested getAttributeNames() from http request" );
                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }
            return super.getAttributeNames();
        }

        public Object getAttribute(String key) {
            if ( !isOk ) {
                log.warn( "!!! Requested getAttributeNames() from http request, key: " + key );
                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }
            return super.getAttribute(key);
        }

        public void setAttribute(String key, Object value) {
            if ( !isOk ) {
                log.warn( "!!! Requested getAttributeNames() from http request, key: " + key + ", value: " + value );
                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }
            super.setAttribute(key, value);
        }

        public void removeAttribute(String key) {
            if ( !isOk ) {
                log.warn( "!!! Requested removeAttribute() from http request" );
                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }
            super.removeAttribute(key);
        }
    }

    private static class InternalServletResponseWrapper extends HttpServletResponseWrapper {

        // all methos in HttpServletResponse must invoked only from ContextNavigator
        // all others invokes are wrong
        boolean isOk = false;

        public InternalServletResponseWrapper( HttpServletResponse httpServletResponse ) {
            super( httpServletResponse );
        }

        public ServletResponse getResponse(){
            if ( !isOk ) {
                log.warn( "!!! Requested getResponse() from http response" );

                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }
            return super.getResponse();
        }

        public void setResponse(ServletResponse response){
            if ( !isOk ) {
                log.warn( "!!! Requested setResponse() from http response" );
                try {
                    throw new Exception("error");
                }
                catch(Exception e) {
                    log.error("error", e);
                }
            }

            super.setResponse( response );
        }

        public PrintWriter getWriter() throws IOException {
            if ( !isOk )
                log.warn( "!!! Requested getWriter() from http response" );

            return super.getWriter();
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if ( !isOk )
                log.warn( "!!! Requested getOutputStream() from http response" );

            return super.getOutputStream();
        }

        public void setHeader( String name, String value ) {
            if ( !isOk )
                log.warn( "!!! Requested setHeader() from http response" );

            super.setHeader( name, value );
        }

        public void setContentLength( int length ) {
            if ( !isOk )
                log.warn( "!!! Requested setContentLength() from http response" );

            super.setContentLength( length );
        }

        public void setContentType( String type ) {
            if ( !isOk )
                log.warn( "!!! Requested setContentType() from http response" );

            super.setContentType( type );
        }

        public void addCookie(Cookie cookie) {
            if ( !isOk )
                log.warn( "!!! Requested addCookie() from http response" );

            super.addCookie( cookie );
        }
    }

    private PortalInstanceImpl( ServletConfig servletConfig ) {
        this.portalServletConfig = servletConfig;
        this.portletContainer = PortletContainerFactory.getInstance( this, PortletContainerUtils.getDeployedInPath(servletConfig) );
        this.supportedList = InternalDaoFactory.getInternalDao().getSupportedLocales();
    }

    private final static Object syncCounter = new Object();
    private static int counterNDC = 0;

    public void process(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse )
        throws IOException, ServletException {

        int counter;
        HttpServletRequest request_;
//        request_ = new InternalServletRequestWrapper( httpServletRequest );
        request_ = httpServletRequest;
        InternalServletResponseWrapper response_ = new InternalServletResponseWrapper( httpResponse );

        // Prepare Nested Diagnostic Contexts
        synchronized (syncCounter) {
            counter = counterNDC;
            ++counterNDC;
        }
        NDC.push("" + counter);

        if (log.isDebugEnabled()) {
            putMainRequestDebug(counter, request_, response_);
        }

        PortalRequestInstance portalRequestInstance = null;
        try {
            boolean isSessionValid = request_.isRequestedSessionIdValid();

            if (log.isDebugEnabled()) {
                putSessionDebug(isSessionValid, request_);
            }

            if (!isSessionValid) {
                initSession(request_);
            }

            portalRequestInstance = new PortalRequestInstance( request_, response_, portalServletConfig, portletContainer, getPortalName() );
            PortalRequestProcessor.processPortalRequest( portalRequestInstance );
        }
        catch (Throwable e) {
            String es = "General error processing request";
            log.error( es, e );

            if (log.isDebugEnabled()) {
                log.error("CN debug. Request URL - " + request_.getRequestURL());
                log.error("CN debug. Request query string - " + request_.getQueryString());

                for (Enumeration en = request_.getParameterNames(); en.hasMoreElements();) {
                    String s = (String) en.nextElement();
                    try {
                        log.error("CN debug. Request attr - " + s + ", value - " + request_.getParameter(s));
                    } catch (Throwable exc) {
                        // debug. nothing doing
                    }
                }
            }
            if (portalRequestInstance==null)
                portalRequestInstance = new PortalRequestInstance();

            portalRequestInstance.byteArrayOutputStream.reset();
            portalRequestInstance.byteArrayOutputStream.write(
                ( es + "<br>" + ExceptionTools.getStackTrace(e, NUM_LINES, "<br>") ).getBytes()
            );
            NDC.pop();
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start finally block");
        }

        try {
            // work around with redirect
            if (portalRequestInstance.getRedirectUrl() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("redirect to new url: " + portalRequestInstance.getRedirectUrl());
                }
                response_.isOk = true;

                setCookie(portalRequestInstance, response_);

                portalRequestInstance.byteArrayOutputStream.close();
                portalRequestInstance.byteArrayOutputStream = null;

                response_.sendRedirect(portalRequestInstance.getRedirectUrl());
                return;
            }

            if (portalRequestInstance.byteArrayOutputStream == null) {
                String es = "byteArrayOutputStream is null";
                log.error(es);
                throw new PortalException(es);
            }

            portalRequestInstance.byteArrayOutputStream.close();
            StringBuilder timeString = getTimeString(counter, portalRequestInstance.startMills);
            final byte[] bytesCopyright = getCopyright().getBytes();
            final byte[] bytes = portalRequestInstance.byteArrayOutputStream.toByteArray();
            final byte[] bytesTimeString = timeString.toString().getBytes();

            final String pageContent = new String(bytes, CharEncoding.UTF_8);

            if (log.isDebugEnabled()) {
                log.debug("ContentLength: " + bytes.length);
                log.debug("pageContent:\n" + pageContent);
            }

            response_.isOk = true;
            if (portalRequestInstance.getLocale() != null) {
                response_.setLocale(portalRequestInstance.getLocale());
            }
            setCookie(portalRequestInstance, response_);
            response_.setHeader("X-Powered-By", PORTAL_INFO);
            response_.setHeader("Server", PORTAL_INFO);

            PortletUtils.setContentType(response_);
            response_.setHeader("Cache-Control", "no-cache");
            response_.setHeader("Pragma", "no-cache");
            response_.setContentLength(bytesCopyright.length + bytes.length + bytesTimeString.length);

            portalRequestInstance.destroy();
            portalRequestInstance = null;

            OutputStream out = response_.getOutputStream();
            // output copyright
            out.write(bytesCopyright);
            out.write(bytes);
            out.write(bytesTimeString);
            out.flush();
            out.close();
            out = null;

            if (log.isInfoEnabled())
                log.info(timeString.toString());

            log.warn(
                "free memory " + Runtime.getRuntime().freeMemory() +
                    " total memory " + Runtime.getRuntime().totalMemory() +
                    " max memory " + Runtime.getRuntime().maxMemory()
            );
        }
        catch (Throwable th) {
            final String es = "Error last step of build page";
            log.error(es, th);
            throw new ServletException(es, th);
        }
        finally {
            if (log.isDebugEnabled()) {
                log.debug("Start finally-finally block");
            }
            try {
                if (portalRequestInstance != null) {
                    portalRequestInstance.destroy();
                }
            }
            catch (Throwable e) {
                log.error("Error destroy portalRequestInstance object", e);
            }
            NDC.pop();
        }
    }

    private static void initSession(HttpServletRequest request_) {
        if (log.isInfoEnabled()) {
            log.info("invalidate current session ");
        }

        HttpSession tempSession = null;
        try {
            tempSession = request_.getSession(false);
            if (tempSession != null) {
                tempSession.invalidate();
            }

            tempSession = request_.getSession(true);
        } catch (IllegalStateException e) {
            log.warn("Error invalidate session", e);
        }

        if (log.isInfoEnabled()) {
            log.info("Status of new session ID: " + request_.isRequestedSessionIdValid() );
            log.info("new session ID (from request): " + request_.getRequestedSessionId() );
            if (tempSession!=null) {
                log.info("new session ID (from session): " + tempSession.getId() );
            }
            else {
                log.info("new session ID (from session) is null" );
            }
        }
    }

    private static void putSessionDebug(boolean sessionValid, HttpServletRequest request_) {
        log.debug("old session ID is valid: " + sessionValid);
        log.debug("old session ID (from request): " + request_.getRequestedSessionId() );
        HttpSession session = request_.getSession( false );
        log.debug("session: " + session );
        if (session!=null) {
            log.debug("old session ID (from session): " + session.getId() );
        }
        else {
            log.debug(" old session is null" );
        }
    }

    private static void putMainRequestDebug(int counter, HttpServletRequest request_, InternalServletResponseWrapper response_) {
        log.debug("counter #6 " + counter);
        log.debug("request_ " + request_);
        log.debug("response_ " + response_);
        log.debug("Request methos type - " + request_.getMethod() );
        log.debug("Request URL - " + request_.getRequestURL());
        log.debug("Request query string - " + request_.getQueryString());

        for (Enumeration e = request_.getParameterNames(); e.hasMoreElements();) {
            String s = (String) e.nextElement();
            log.debug("Request parameter - " + s + ", value - " + request_.getParameter(s) );
        }
        log.debug( "This request made with cookie" );
        Cookie[] cookies = request_.getCookies();
        if (cookies!=null) {
            for (final Cookie newVar : cookies) {
                log.debug(cookieToString(newVar));
            }
        }
    }

    private static StringBuilder getTimeString( int counter, long startMills ) {
        return new StringBuilder( "\n<!-- NDC #" ).append( counter ).append( ", page processed for " ).append( System.currentTimeMillis() - startMills ).append( " milliseconds -->" );
    }

    private static void setCookie( PortalRequestInstance portalRequestInstance, InternalServletResponseWrapper response_ ) {
        // set Cookie
        CookieManager cookieManager = portalRequestInstance.getCookieManager();
        if (log.isDebugEnabled() ) {
            log.debug( "CookieManager: " + cookieManager );
        }
        if ( cookieManager!=null ) {
            for (Cookie cookie : cookieManager.getCookieList()) {
                if (log.isDebugEnabled()) {
                    log.debug(cookieToString(cookie));
                }
                response_.addCookie(cookie);
            }
        }
    }

    private static String cookieToString( Cookie cookie) {
        return
            "Cookie: " +
            "domain: " + cookie.getDomain()+", " +
            "path: "+cookie.getPath()+", " +
            "name: "+cookie.getName()+", " +
            "value: "+cookie.getValue();
    }

    public static String getCopyright() {
        return COPYRIGHT;
    }

    public Collection<String> getSupportedLocales(){
        return supportedList;
    }

    public void registerPortlet(String fullPortletName) {
        PortalUtils.registerPortletName(fullPortletName);
//        synchronized(destroyedPortletName) {
            destroyedPortletName.remove(fullPortletName);
//        }
    }

    public static List<String> destroyedPortlet() {
//        synchronized(destroyedPortletName) {
            return (List)Arrays.asList( destroyedPortletName.toArray() );
//        }
    }

    public void destroyPortlet(String fullPortletName) {
//        synchronized(destroyedPortletName) {
            destroyedPortletName.add(fullPortletName);
//        }
    }

}