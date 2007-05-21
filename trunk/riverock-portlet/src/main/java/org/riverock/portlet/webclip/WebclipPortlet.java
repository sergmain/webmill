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
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.SiteLanguage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.portlet.*;
import java.io.IOException;
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

            PortalDaoProvider portalDaoProvider = (PortalDaoProvider)request.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
            PortalInfo portalInfo = (PortalInfo)request.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
            Long siteLanguageId = portalInfo.getSiteLanguageId(request.getLocale());
            if (siteLanguageId==null) {
                throw new RuntimeException("siteLanguageId is null for locale "+ request.getLocale().toString());
            }
            SiteLanguage siteLanguage = portalDaoProvider.getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId);
            if (siteLanguage==null) {
                throw new RuntimeException("Not found siteLanguage for siteLanguageId "+siteLanguageId);
            }
            if (!siteId.equals(siteLanguage.getSiteId())) {
                throw new RuntimeException("Wrong siteId. Expected: " + siteId+", real; " + siteLanguage.getSiteId());
            }

            if (webclipId==null) {
                webclipId=createWebclip(request, siteId);
            }
            if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.SAVE_PARAM_ACTION))) {
                log.debug("    execute 'save-param' action");
                saveParamAction(request);
            }
            else if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.PROCESS_CONTENT_ACTION))) {
                log.debug("    execute 'process-content' action");
                processWebclipData(request, webclipId, siteId, portalDaoProvider, siteLanguageId);
            }
            else if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.SAVE_GET_PROCESS_ACTION))) {
                log.debug("    execute 'save-get-process' action");
                saveParamAction(request);
                getOriginContent(request, webclipId, siteId);
                processWebclipData(request, webclipId, siteId, portalDaoProvider, siteLanguageId);
            }
            else if (StringUtils.isNotBlank(request.getParameter(WebclipConstants.GET_ORIGIN_CONTENT_ACTION))) {
                log.debug("    execute 'get-origin-content' action");
                getOriginContent(request, webclipId, siteId);
            }
            else {
                throw new RuntimeException("Unknown action type");
            }
        }
        catch (Throwable e) {
            log.error("Error", e);
            response.setRenderParameter(WebclipConstants.WEBCLIP_ERROR_MESSAGE_PARAM, e.toString());
        }
    }

    private void saveParamAction(ActionRequest request) {
        setNewHrefPrefix(request, request.getParameter(WebclipConstants.NEW_HREF_PREFIX_PARAM));
        setHrefStartPart(request, request.getParameter(WebclipConstants.HREF_START_PAGE_PARAM));
        setUrl(request, request.getParameter(WebclipConstants.SOURCE_URL_PARAM));
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

    private void getOriginContent(PortletRequest request, Long webclipId, Long siteId) throws IOException {
        log.info("Start processWebclipData()");

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

            WebclipUtils.loadContentFromSource(webclip, url);
        }
    }

    private void processWebclipData(PortletRequest request, Long webclipId, Long siteId, PortalDaoProvider portalDaoProvider, Long siteLanguageId) throws IOException {
        log.info("Start processWebclipData()");

        String url = getUrl(request);
        String hrefPrefix = getNewHrefPrefix(request);
        String hrefStartPart = getHrefStartPart(request);

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
                return;
            }

            WebclipUtils.processStoredContent(webclip, hrefPrefix, hrefStartPart, portalDaoProvider, siteLanguageId);
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
            webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId);
            isNew=true;
        }
        else {
            try {
                webclipId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                log.warn("Error parse webclipId: " + id+", will be created new WebclipBean");
                webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId);
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
                webclipId = PortletDaoFactory.getWebclipDao().createWebclip(siteId);
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
