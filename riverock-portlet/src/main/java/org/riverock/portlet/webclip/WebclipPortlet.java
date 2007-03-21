/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.webclip;

import org.riverock.portlet.dao.PortletDaoFactory;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import javax.portlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 17:16:06
 * <p/>
 * $Id$
 */
public class WebclipPortlet implements Portlet {
    private static Logger log = Logger.getLogger(WebclipPortlet.class);

    private PortletConfig portletConfig=null;

    public void init(PortletConfig config) throws PortletException {
        this.portletConfig=config;
    }

    public void destroy() {
        portletConfig=null;
    }

    public void processAction (ActionRequest request, ActionResponse response) throws PortletException, IOException {
        log.debug("Start processAction()");

        try {
            Long webclipId = getWebclipId(request);
            Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            if (webclipId==null) {
                webclipId=createWebclip(request, siteId);
            }
            if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.SAVE_ACTION))) {
                log.debug("    process 'save' action");
                setNewHrefPrefix(request, request.getParameter(WebclipConstants.NEW_HREF_PREFIX_PARAM));
                setHrefStartPart(request, request.getParameter(WebclipConstants.HREF_START_PAGE_PARAM));
                setUrl(request, request.getParameter(WebclipConstants.SOURCE_URL_PARAM));
            }
            else if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.REFRESH_ACTION))) {
                log.debug("    process 'refresh' action");
                refreshWebclipData(request, webclipId, siteId);
            }
            else {
                throw new RuntimeException("Unknown action type");
            }
        }
        catch (Error e) {
            response.setRenderParameter(WebclipConstants.WEBCLIP_ERROR_MESSAGE_PARAM, e.toString());
            throw e;
        }
        catch (Exception e) {
            response.setRenderParameter(WebclipConstants.WEBCLIP_ERROR_MESSAGE_PARAM, e.toString());
            throw new PortletException("processAction error", e);
        }
    }

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        Long webclipId = getWebclipId(request);
        Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        WebclipBean bean = getWebclip(webclipId, siteId);
        request.setAttribute(WebclipConstants.WEBCLIP_BEAN, bean);
        request.setAttribute(WebclipConstants.SOURCE_URL_PARAM, getUrl(request));
        request.setAttribute(WebclipConstants.HREF_START_PAGE_PARAM, getHrefStartPart(request));
        request.setAttribute(WebclipConstants.NEW_HREF_PREFIX_PARAM, getNewHrefPrefix(request));

        // set error of process action
        request.setAttribute(WebclipConstants.WEBCLIP_ERROR_MESSAGE, PortletService.getString(request, WebclipConstants.WEBCLIP_ERROR_MESSAGE_PARAM, "") );

        portletConfig.getPortletContext().getRequestDispatcher(WebclipConstants.RIVEROCK_WEBLICP_INDEX_JSP).include(request, response);
    }

    private String getUrl(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.URL_SOURCE_PREF, null);
    }
                    
    private String getNewHrefPrefix(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.NEW_HREF_PREFIX_PREF, null);
    }

    private String getHrefStartPart(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.HREF_START_PAGE_PREF, null);
    }

    private String getProxyPort(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.PROXY_PORT_PREF, null);
    }

    private String getProxyHost(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.PROXY_HOST_PREF, null);
    }

    private String getProxyLogin(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.PROXY_LOGIN_PREF, null);
    }

    private String getProxyPassword(PortletRequest request) {
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(WebclipConstants.PROXY_PASSWORD_PREF, null);
    }

    private void refreshWebclipData(PortletRequest request, Long webclipId, Long siteId) {
        log.info("Start refreshWebclipData()");

        String url = getUrl(request);
        if (StringUtils.isNotBlank(url)) {
            WebclipBean webclip=getWebclip(webclipId, siteId);
            if (webclip==null) {
                Long id = createWebclip(request, siteId);
                if (id==null) {
                    throw new RuntimeException("id of new webclip is null");
                }
                webclip=getWebclip(webclipId, siteId);
                if (webclip==null) {
                    throw new RuntimeException("webclip is null");
                }
            }
            try {
                URL urlObject = new URL(url);
                URLConnection urlConnection;
//                Proxy proxy = prepareProxy(request);
//                if (proxy!=null) {
//                    urlConnection = urlObject.openConnection(proxy);
//                }
//                else {
                    urlConnection = urlObject.openConnection();
//                }
                if (log.isDebugEnabled()) {
                    log.debug("Current read timeout: " + urlConnection.getReadTimeout());
                }
                urlConnection.setReadTimeout(WebclipConstants.DEFAULT_READ_TIMEOUT);
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int count;
                while ((count=is.read(bytes)) != -1) {
                    os.write(bytes, 0, count);
                }
                is.close();

                bytes = os.toByteArray();
                if (log.isDebugEnabled()) {
                    log.debug("Content from "+url+":\n" + new String(bytes));
                }
                WebclipUrlProducer producer = new WebclipUrlProducerImpl(getNewHrefPrefix(request), getHrefStartPart(request));
                WebclipDataProcessor processor = new WebclipDataProcessorImpl(
                    producer, bytes, WebclipConstants.DIV_NODE_TYPE, "content" );
                
                os = new ByteArrayOutputStream();
                processor.modify(os);
                webclip.setWebclipData(os.toString(CharEncoding.UTF_8));
                PortletDaoFactory.getWebclipDao().updateWebclip(webclip);
            } catch (Throwable th) {
                String es = "Error get content from URL: " + url;
                log.error(es, th);
                throw new RuntimeException(es, th);
            }
        }
    }

    private Proxy prepareProxy(PortletRequest request) {
        String port = getProxyPort(request);
        String host = getProxyHost(request);
        String login = getProxyLogin(request);
        String password = getProxyPassword(request);

        if (port==null && host==null) {
            return null;
        }
        if (port==null || host==null ){
            log.warn("Proxy definition not complete. proxy host: " +host+", port: " + port);
        }

        SocketAddress addr = new InetSocketAddress(host, new Integer(port));
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        return proxy;
    }

    private WebclipBean getWebclip(Long webclipId, Long siteId) {
        if (siteId==null) {
            throw new RuntimeException("siteId is null");
        }
        if (webclipId==null) {
            return null;
        }

        return PortletDaoFactory.getWebclipDao().getWebclip(siteId, webclipId);
    }

    private void setUrl(PortletRequest request, String url) {
        if (log.isDebugEnabled()) {
            log.debug("Start setUrl(), url: " + url);
        }

        if (StringUtils.isNotBlank(url)) {
            PortletPreferences preferences = request.getPreferences();
            try {
                preferences.setValue(WebclipConstants.URL_SOURCE_PREF, url);
            } catch (ReadOnlyException e) {
                String es = "preferences must be not read-only";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
            try {
                preferences.store();
            } catch (Exception e) {
                String es = "Error store preference";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
        }
    }

    private void setHrefStartPart(PortletRequest request, String hrefStartPart) {
        if (log.isDebugEnabled()) {
            log.debug("Start setHrefStartPart(), hrefStartPart: " + hrefStartPart);
        }

        if (StringUtils.isNotBlank(hrefStartPart)) {
            PortletPreferences preferences = request.getPreferences();
            try {
                preferences.setValue(WebclipConstants.HREF_START_PAGE_PREF, hrefStartPart);
            } catch (ReadOnlyException e) {
                String es = "preferences must be not read-only";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
            try {
                preferences.store();
            } catch (Exception e) {
                String es = "Error store preference";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
        }
    }

    private void setNewHrefPrefix(PortletRequest request, String newHrefPrefix) {
        if (log.isDebugEnabled()) {
            log.debug("Start setNewHrefPrefix(), newHrefPrefix: " + newHrefPrefix);
        }

        if (StringUtils.isNotBlank(newHrefPrefix)) {
            PortletPreferences preferences = request.getPreferences();
            try {
                preferences.setValue(WebclipConstants.NEW_HREF_PREFIX_PREF, newHrefPrefix);
            } catch (ReadOnlyException e) {
                String es = "preferences must be not read-only";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
            try {
                preferences.store();
            } catch (Exception e) {
                String es = "Error store preference";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
        }
    }

    private Long getWebclipId(PortletRequest request) {
        log.debug("Strat getWebclipId()");
        PortletPreferences preferences = request.getPreferences();
        String id = preferences.getValue(WebclipConstants.WEBCLIP_ID_PREF, null);
        Long webclipId;
        if(log.isDebugEnabled()) {
            log.debug("    webclipId from preference: "+id);
        }
        if (StringUtils.isBlank(id)) {
            return null;
        }
        else {
            try {
                webclipId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                log.warn("Error parse webclipId: " + id+", will be created new WebclipBean");
                return null;
            }
        }

        return webclipId;
    }

    private Long createWebclip(PortletRequest request, Long siteId) {
        log.debug("Strat getWebclipId()");
        PortletPreferences preferences = request.getPreferences();
        String id = preferences.getValue(WebclipConstants.WEBCLIP_ID_PREF, null);
        Long webclipId;
        if(log.isDebugEnabled()) {
            log.debug("    webclipId from preference: "+id);
        }
        boolean isNew=false;
        if (StringUtils.isBlank(id)) {
            webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId, null);
            isNew=true;
        }
        else {
            try {
                webclipId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                log.warn("Error parse webclipId: " + id+", will be created new WebclipBean");
                webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId, null);
                isNew=true;
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("    isNew #1: "+isNew);
        }

        if (!isNew) {
            WebclipBean bean = PortletDaoFactory.getWebclipDao().getWebclip(siteId, webclipId);
            if(log.isDebugEnabled()) {
                log.debug("    WebclipBean: "+bean);
            }
            if (bean==null) {
                // create new webclip bean
                webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId, null);
                isNew=true;
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("    isNew #2: "+isNew);
        }

        if (isNew) {
            try {
                preferences.setValue(WebclipConstants.WEBCLIP_ID_PREF, webclipId.toString());
            } catch (ReadOnlyException e) {
                String es = "preferences must be not read-only";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
            try {
                preferences.store();
            } catch (Exception e) {
                String es = "Error store preference";
                log.error(es);
                throw new IllegalStateException(es, e);
            }
        }

        if (webclipId==null) {
            throw new IllegalStateException("webclipId is null");
        }
        return webclipId;
    }

}
