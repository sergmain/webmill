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

/**
 * $Id$
 */
package org.riverock.webmill.portlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.utils.ServletUtils;
import org.riverock.generic.db.DatabaseAdapter;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

public final class ContextNavigator extends HttpServlet {
    private static Logger log = Logger.getLogger(ContextNavigator.class);

    static final String copyright =
        "<!--\n" +
        "  Engine: WebMill\n" +
        " Release: @@WEBMILL_RELEASE@@\n" +
        "   Build: @@WEBMILL_BUILD@@\n" +
        "Homepage: http://webmill.riverock.org\n" +
        "-->\n";

    private static final int NUM_LINES = 100;

    static ServletConfig portalServletConfig = null;
    public void init( ServletConfig servletConfig ) throws ServletException{
        portalServletConfig = servletConfig;
        super.init(servletConfig);
    }

    public ContextNavigator() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    private static Object syncCounter = new Object();
    private static int counterNDC = 0;

    public void doGet(HttpServletRequest request_, HttpServletResponse response_)
        throws IOException, ServletException {

        PortalRequestInstance portalRequestInstance = new PortalRequestInstance();

        // Prepare Nested D... Context
        synchronized (syncCounter) {
            if (log.isDebugEnabled())
                log.debug("counterNDC #1 " + counterNDC);

            portalRequestInstance.counter = counterNDC;
            ++counterNDC;

            if (log.isDebugEnabled()) {
                log.debug("counterNDC #2 " + counterNDC);
                log.debug("counter #3 " + portalRequestInstance.counter);
            }

            NDC.push("" + portalRequestInstance.counter);

            if (log.isDebugEnabled()) {
                log.debug("counterNDC #4 " + counterNDC);
                log.debug("counter #5 " + portalRequestInstance.counter);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("counter #6 " + portalRequestInstance.counter);
            log.debug("this " + this);
            log.debug("request_ " + request_);
            log.debug("response_ " + response_);
        }

        try {
            boolean isSessionValid = request_.isRequestedSessionIdValid();

            if (log.isDebugEnabled())
                log.debug("session ID is valid - " + isSessionValid);

            if (!isSessionValid) {
                if (log.isInfoEnabled())
                    log.info("invalidate current session ");

                try {
                    HttpSession tempSession = request_.getSession(false);
                    if (tempSession != null)
                        tempSession.invalidate();

                    request_.getSession(true);
                } catch (java.lang.IllegalStateException e) {
                    log.warn("Error invalidate session", e);
                }

                if (log.isInfoEnabled())
                    log.info("Status of new session ID is - " + request_.isRequestedSessionIdValid());

            }

            setContentType(response_);

            try {
                portalRequestInstance.db = DatabaseAdapter.getInstance(false);
            } catch (Exception e) {
                log.error("Error create DBconnect", e);

                String errorString = "Error create DBconnect<br>" +
                    ExceptionTools.getStackTrace(e, NUM_LINES);
                portalRequestInstance.byteArrayOutputStream.write(errorString.getBytes());
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("Request URL - " + request_.getRequestURL());
                log.debug("Request query string - " + request_.getQueryString());

                for (Enumeration e = request_.getParameterNames(); e.hasMoreElements();) {
                    String s = (String) e.nextElement();
                    log.debug("Request attr - " + s + ", value - " + ServletUtils.getString(request_, s));
                }
            }

            long jspPageMills = 0;
            try {
                if (log.isInfoEnabled()) {
                    jspPageMills = System.currentTimeMillis();
                    log.info("start init PortalRequestInstance ");
                }
                portalRequestInstance.init(request_, response_, portalRequestInstance.db);

            } catch (Exception e) {
                final String es = "Error init PortalRequestInstance ";
                log.error(es, e);
                portalRequestInstance.byteArrayOutputStream.write(
                    (es+"<br>"+ExceptionTools.getStackTrace(e, NUM_LINES, "<BR>")).getBytes()
                );
                return;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("init PortalRequestInstance for " + (System.currentTimeMillis() - jspPageMills) + " milliseconds");
                }
            }

            if ( !portalRequestInstance.initTemplate() ) return;
            if ( !portalRequestInstance.initXslt() ) return;


            // if default portlet type is null, process as 'mill.index'
//            if (ctxInstance.getDefaultPortletType() == null) {
//                PortalRequestProcessor.processRequest(ctxInstance, portalRequestInstance);
//                return;
//            }

//            PortletType portlet = ctxInstance.getDefaultPortletDescription().getPortletConfig();
//
//            if (log.isDebugEnabled()) {
//                log.debug("Portlet type: " + ctxInstance.getDefaultPortletType());
//                log.debug("namePortlet ID: " + PortletTools.getStringParam(portlet, PortletTools.name_portlet_id));
//            }

            PortalRequestProcessor.processRequest( portalRequestInstance );
/*

            switch (ctxInstance.getDefaultPortletDescription().getPortletType().getType()) {
                case PortletDescriptionTypeTypePortletType.CONTROLLER_TYPE:

                    RequestControllerProcessor.processControllerRequest(ctxInstance, portlet, portalRequestInstance);
                    break;

                case PortletDescriptionTypeTypePortletType.MODEL_TYPE:

                    String errorString = "Portlet with type 'model' can't bind to Menu";
                    log.error(errorString);
                    portalRequestInstance.byteArrayOutputStream.write(errorString.getBytes());
                    break;

                case PortletDescriptionTypeTypePortletType.VIEW_TYPE:

                    PortalRequestProcessor.processRequest(ctxInstance, portalRequestInstance);
                    break;

                default:
                    errorString =
                        "Unknown type of portlet - " +
                        PortletTools.getStringParam(portlet, PortletTools.type_portlet);
                    log.error(errorString);
                    portalRequestInstance.byteArrayOutputStream.write(errorString.getBytes());
                    break;
            }
*/
        }
        catch (Throwable e) {
            log.error("Error processing page with ContextNavigator", e);
            log.error("CN debug. type " + portalRequestInstance.getDefaultPortletType());

            if (log.isDebugEnabled()) {
                log.error("CN debug. Request URL - " + portalRequestInstance.getHttpRequest().getRequestURL());
                log.error("CN debug. Request query string - " + portalRequestInstance.getHttpRequest().getQueryString());

                for (Enumeration en = request_.getParameterNames(); en.hasMoreElements();) {
                    String s = (String) en.nextElement();
                    try {
                        log.error("CN debug. Request attr - " + s + ", value - " + ServletUtils.getString(request_, s));
                    } catch (ConfigException exc) {
                    }
                }
            }

            portalRequestInstance.byteArrayOutputStream.write(
                ("Error processing page with ContextNavigator<br>" +
                ExceptionTools.getStackTrace(e, NUM_LINES, "<br>")
                ).getBytes()
            );
        }
        finally {
            try {
                String timeString = "\n<!-- NDC #" + portalRequestInstance.counter + ", page with portlets processed for " + (System.currentTimeMillis() - portalRequestInstance.startMills) + " milliseconds -->";

                if (log.isInfoEnabled())
                    log.info(timeString);

                if (portalRequestInstance.byteArrayOutputStream == null) {
                    String es = "byteArrayOutputStream is null, try to recover data and start another loop for this request";
                    log.error(es);
                    throw new PortalException(es);
                }

                portalRequestInstance.byteArrayOutputStream.write(timeString.getBytes());

                Writer out = response_.getWriter();
                if (out == null) {
                    String fatalString = "fatal error - response.getWriter() is null";
                    log.fatal(fatalString);
                    throw new ServletException(fatalString);
                }

                portalRequestInstance.byteArrayOutputStream.flush();
                portalRequestInstance.byteArrayOutputStream.close();
                response_.setContentLength(portalRequestInstance.byteArrayOutputStream.size());
                out.write(new String(portalRequestInstance.byteArrayOutputStream.toByteArray(), WebmillConfig.getHtmlCharset()));

                out.flush();
                out.close();
                out = null;
                portalRequestInstance.byteArrayOutputStream = null;
                DatabaseAdapter.close(portalRequestInstance.db);
                portalRequestInstance.db = null;

                Long maxMemory = null;
                try {
                    maxMemory = MainTools.getMaxMemory();
                }
                catch (Exception e) {
                }

                log.warn("free memory " + Runtime.getRuntime().freeMemory() +
                    " total memory " + Runtime.getRuntime().totalMemory() +
                    (maxMemory != null ? " max memory " + maxMemory : ""));
            }
            catch (Throwable th) {
                final String es = "Throwable ";
                log.error(es, th);
                throw new ServletException( es, th );
            }
        }
        NDC.pop();//remove();
    }

    public static void setContentType(HttpServletResponse response) throws Exception {
        setContentType(response, WebmillConfig.getHtmlCharset());
    }

    public static void setContentType(HttpServletResponse response, String charset) throws PortalException {

        if (log.isDebugEnabled()) log.debug("set new charset - " + charset);
        try {
            response.setContentType("text/html; charset=" + charset);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }
}