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
package org.riverock.webmill.portal.instance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.interfaces.portal.CookieManager;
import org.riverock.interfaces.portal.search.PortalIndexer;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerFactory;
import org.riverock.webmill.container.tools.PortletContainerUtils;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.search.PortalIndexerImpl;
import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.webmill.portal.namespace.NamespaceMapper;
import org.riverock.webmill.portal.namespace.NamespaceFactory;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.page_element.PortalPageController;
import org.riverock.webmill.portal.instance.PortalVersion;
import org.riverock.webmill.portal.PortalResponse;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.template.PortalTemplateManager;
import org.riverock.webmill.template.PortalTemplateManagerFactory;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.utils.PortletUtils;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 18:00:18
 *         $Id$
 */
//@SuppressWarnings({"UnusedAssignment"})
public class PortalInstanceImpl implements PortalInstance  {
    private final static Logger log = Logger.getLogger(PortalInstanceImpl.class);

    private static final String UNKNOWN_PORTAL_VERSON = "0.0.1";
    private static final String WEBMILL_PROPERTIES = "/org/riverock/webmill/portal/webmill.properties";

    private static PortalVersion portalVersion = new PortalVersion( getPortalVersion() );
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
    private PortalIndexer portalIndexer = null;
    private Collection<String> supportedList = null;
    private static final Collection<String> destroyedPortletName = new ConcurrentLinkedQueue<String>();

    private Long siteId;

    private PortalTemplateManager portalTemplateManager=null;
    private ClassLoader portalClassLoader=null;

    public void destroy() {
        portletContainer.unregisterPortalInstance();
        portalServletConfig = null;
        portletContainer = null;
        portalIndexer=null;
        if (supportedList!=null) {
            supportedList.clear();
            supportedList = null;
        }
        siteId=null;
        portalClassLoader = null;
    }

    public ClassLoader getPortalClassLoader() {
        return portalClassLoader;
    }

    public PortalTemplateManager getPortalTemplateManager() {
        return portalTemplateManager;
    }

    public PortalIndexer getPortalIndexer() {
        return portalIndexer;
    }

    public ServletConfig getPortalServletConfig() {
        return portalServletConfig;
    }

    public PortletContainer getPortletContainer() {
        return portletContainer;
    }

    public String getPortalName() {
        return PORTAL_INFO;
    }

    private synchronized static String getPortalVersion() {
        if (PORTAL_VERSION!=null) {
            return PORTAL_VERSION;
        }
        Properties pr = new Properties();
        try {
            InputStream inputStream=null;
            try {
                inputStream = PortalInstanceImpl.class.getResourceAsStream(WEBMILL_PROPERTIES);
                pr.load(inputStream);
            }
            finally {
                if (inputStream!=null) {
                    inputStream.close();
                    //noinspection UnusedAssignment
                    inputStream=null;
                }
            }
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
        }
        catch (IOException e) {
            String es = "Error load webmill.properties files.";
            log.error(es, e);
            PORTAL_VERSION = UNKNOWN_PORTAL_VERSON;
        }
        return PORTAL_VERSION;
    }

    public static PortalInstanceImpl getInstance( Long siteId, ServletConfig servletConfig, ClassLoader portalClassLoader ) {
        return new PortalInstanceImpl( siteId, servletConfig, portalClassLoader );
    }

    public int getPortalMajorVersion() {
        return portalVersion.major;
    }

    public int getPortalMinorVersion() {
        return portalVersion.minor;
    }


    private PortalInstanceImpl( Long siteId, ServletConfig servletConfig, ClassLoader portalClassLoader  ) {
        this.siteId = siteId;
        this.portalServletConfig = servletConfig;
        this.portalClassLoader = portalClassLoader;

        this.portletContainer = PortletContainerFactory.getInstance( this, PortletContainerUtils.getDeployedInPath(servletConfig) );
        this.supportedList = InternalDaoFactory.getInternalDao().getSupportedLocales();
        this.portalIndexer = new PortalIndexerImpl(this.siteId, this.portletContainer, portalClassLoader);
        this.portalTemplateManager = PortalTemplateManagerFactory.getInstance(this.siteId);
    }

    public Long getSiteId() {
        return siteId;
    }

    private final static Object syncCounter = new Object();
    private static int counterNDC = 0;

    public void process(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse )
        throws IOException, ServletException {

        int counter;
        HttpServletRequest request_;
        HttpServletResponse response_;
//        request_ = new InternalServletRequestWrapper( httpServletRequest );
//        response_ = new InternalServletResponseWrapper( httpResponse );
        request_ = httpServletRequest;
        response_ = httpResponse;

        // Prepare Nested Diagnostic Contexts
        synchronized (syncCounter) {
            counter = counterNDC;
            ++counterNDC;
        }
        NDC.push("" + counter);

        if (log.isDebugEnabled()) {
            putMainRequestDebug(counter, request_, response_);
        }

        PortalResponse portalResponse=null;
        PortalRequestInstance portalRequestInstance=null;
        try {
/*
            boolean isSessionValid = request_.isRequestedSessionIdValid();

            if (log.isDebugEnabled()) {
                putSessionDebug(isSessionValid, request_);
            }

            if (!isSessionValid) {
                initSession(request_);
            }
*/

            if (request_.isRequestedSessionIdValid()) {
                checkDestroyedPortlet(PortalInstanceImpl.destroyedPortlet(), request_.getSession(false));
            }

            portalRequestInstance = new PortalRequestInstance( request_, response_, this );
            portalResponse = new PortalResponse();
            PortalPageController.processPortalRequest(this, portalRequestInstance, portalResponse);
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
            if (portalResponse==null) {
                portalResponse = new PortalResponse();
            }                                                            

            if (portalRequestInstance==null) {
                portalRequestInstance = new PortalRequestInstance();
            }

            portalResponse.getByteArrayOutputStream().reset();
            portalResponse.getByteArrayOutputStream().write(
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
            if (portalResponse.getRedirectUrl() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("redirect to new url: " + portalResponse.getRedirectUrl());
                }
//                response_.isOk = true;

                setCookie(portalRequestInstance, response_);

                portalResponse.getByteArrayOutputStream().close();
                portalResponse.setByteArrayOutputStream(null);

                response_.sendRedirect(portalResponse.getRedirectUrl());
                return;
            }

            if (portalResponse.getByteArrayOutputStream() == null) {
                String es = "byteArrayOutputStream is null";
                log.error(es);
                throw new PortalException(es);
            }

            portalResponse.getByteArrayOutputStream().close();
            StringBuilder timeString = getTimeString(counter, portalRequestInstance.getStartMills());
            final byte[] bytesCopyright = getCopyright().getBytes();
            final byte[] bytes = portalResponse.getByteArrayOutputStream().toByteArray();
            final byte[] bytesTimeString = timeString.toString().getBytes();

            final String pageContent = new String(bytes, CharEncoding.UTF_8);

            if (log.isDebugEnabled()) {
                log.debug("ContentLength: " + bytes.length);
                log.debug("pageContent:\n" + pageContent);
            }

//            response_.isOk = true;
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
            //noinspection UnusedAssignment
            out = null;

            if (log.isInfoEnabled()) {
                log.info(timeString.toString());
            }

            log.warn(
                "free memory " + Runtime.getRuntime().freeMemory() +
                    " total memory " + Runtime.getRuntime().totalMemory() +
                    " max memory " + Runtime.getRuntime().maxMemory()
            );
            writeCacheStatistics();
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
                    //noinspection UnusedAssignment
                    portalRequestInstance=null;
                }
            }
            catch (Throwable e) {
                log.error("Error destroy portalRequestInstance object", e);
            }
            try {
                if (portalResponse != null) {
                    portalResponse.destroy();
                    //noinspection UnusedAssignment
                    portalResponse=null;
                }
            }
            catch (Throwable e) {
                log.error("Error destroy portalResponse object", e);
            }
            NDC.pop();
        }
    }

    private void writeCacheStatistics() {
        if (log.isDebugEnabled()) {
            try {
                log.debug("Get keys for all cache.");
                CacheManager manager = CacheManager.getInstance();
                String[] names = manager.getCacheNames();
                for (String name : names) {
                    log.debug("    name: " + name);
                    Cache cache = manager.getCache(name);
                    List keys = cache.getKeysNoDuplicateCheck();
                    for (Object key : keys) {
                        log.debug("        key: " + key.toString());
                    }
                }

                Statistics stats = HibernateUtils.getSession().getSessionFactory().getStatistics();

                double queryCacheHitCount  = stats.getQueryCacheHitCount();
                double queryCacheMissCount = stats.getQueryCacheMissCount();
                double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);

                log.debug("Query Hit ratio: " + queryCacheHitRatio);

                names = stats.getEntityNames();
                for (String name : names) {
                    EntityStatistics entityStats = stats.getEntityStatistics( name );
                    long changes =
                        entityStats.getInsertCount()
                            + entityStats.getUpdateCount()
                            + entityStats.getDeleteCount();
                    log.debug("    "+name+ " changed " + changes + " times" );
                }
            }
            catch(Throwable th) {
                log.info("Error: " + th);
            }
        }
    }

/*
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
*/

    private static void putMainRequestDebug(int counter, HttpServletRequest request_, HttpServletResponse response_) {
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

    private static void setCookie( PortalRequestInstance portalRequestInstance, HttpServletResponse response_ ) {
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
        destroyedPortletName.remove(fullPortletName);
    }

    public static List<String> destroyedPortlet() {
        //noinspection RedundantCast,unchecked
        return (List)Arrays.asList( destroyedPortletName.toArray() );
    }

    public void destroyPortlet(String fullPortletName) {
        destroyedPortletName.add(fullPortletName);
    }

    /**
     * remove form session all attributes, which are corresponded to destroyed portlet
     *
     * @param destroyedPortletNames name of destroyed portlet
     * @param session               http session
     */
    private static void checkDestroyedPortlet(List<String> destroyedPortletNames, HttpSession session) {
        if (session == null) {
            return;
        }
        try {
            NamespaceMapper nm = NamespaceFactory.getNamespaceMapper();
            //noinspection unchecked
            List<String> attrs = Collections.list(session.getAttributeNames());
            for (String portletName : destroyedPortletNames) {
                List<Namespace> namespaces = NamespaceFactory.getNamespaces(portletName);
                for (Namespace namespace : namespaces) {
                    for (String attr : attrs) {
                        String realAttrName = nm.decode(namespace, attr);
                        if (realAttrName != null) {
                            session.removeAttribute(attr);
                        }
                    }
                }
            }
        }
        catch (Throwable e) {
            log.error("Error remove attributed", e);
        }
    }

}