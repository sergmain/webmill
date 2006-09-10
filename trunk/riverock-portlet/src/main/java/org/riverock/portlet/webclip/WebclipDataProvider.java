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

import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.dao.PortletDaoFactory;
import org.riverock.webmill.container.ContainerConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import java.net.URLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 18:47:10
 * <p/>
 * $Id$
 */
public class WebclipDataProvider {
    private static Logger log = Logger.getLogger(WebclipDataProvider.class);

    public static final String WEBCLIP_ID_PREF = "webclip.webclip_id";
    public static final String URL_PREF = "webclip.url";

    public Long getWebclipId() {
        log.debug("Strat getWebclipId()");
        PortletRequest request = FacesTools.getPortletRequest();
        PortletPreferences preferences = request.getPreferences();
        String id = preferences.getValue(WEBCLIP_ID_PREF, null);
        Long webclipId;
        if(log.isDebugEnabled()) {
            log.debug("    webclipId from preference: "+id);
        }
        Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        boolean isNew=false;
        if (StringUtils.isBlank(id)) {
            webclipId = PortletDaoFactory.getWebClipDao().createWebclip(siteId, null);
            isNew=true;
        }
        else {
            try {
                webclipId = Long.parseLong(id);
            } catch (NumberFormatException e) {
                log.warn("Error parse webclipId: " + id+", will be created new WebclipBean");
                webclipId = PortletDaoFactory.getWebClipDao().createWebclip(siteId, null);
                isNew=true;
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("    isNew #1: "+isNew);
        }

        if (!isNew) {
            WebclipBean bean = PortletDaoFactory.getWebClipDao().getWebclip(siteId, webclipId);
            if(log.isDebugEnabled()) {
                log.debug("    WebclipBean: "+bean);
            }
            if (bean==null) {
                // create new webclip bean
                webclipId = PortletDaoFactory.getWebClipDao().createWebclip(siteId, null);
                isNew=true;
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("    isNew #2: "+isNew);
        }

        if (isNew) {
            try {
                preferences.setValue(WEBCLIP_ID_PREF, webclipId.toString());
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


    public void setUrl(String url) {
        if (log.isDebugEnabled()) {
            log.debug("Start setUrl(), url: " + url);
        }

        if (StringUtils.isNotBlank(url)) {
            PortletRequest request = FacesTools.getPortletRequest();
            PortletPreferences preferences = request.getPreferences();
            try {
                preferences.setValue(URL_PREF, url);
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

    public String getUrl() {
        PortletRequest request = FacesTools.getPortletRequest();
        PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(URL_PREF, null);
    }

    public WebclipBean getWebclip() {
        Long webclipId = getWebclipId();
        PortletRequest request = FacesTools.getPortletRequest();
        Long siteId = new Long( request.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        WebclipBean bean = PortletDaoFactory.getWebClipDao().getWebclip(siteId, webclipId);
        if (bean==null) {
            throw new IllegalStateException("webclip not found, webclipId: " + webclipId);
        }
        return bean;
    }

    public String saveWebclipData() {
        log.info("Start saveWebclipData()");
        return "webclip";
    }

    public String refreshWebclipData() {
        log.info("Start refreshWebclipData()");

        String url = getUrl();
        try {
            URL yahoo = new URL(url);
            URLConnection urlConnection = yahoo.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int count;
            while ((count=is.read(bytes)) != -1) {
                os.write(bytes, 0, count);
            }
            is.close();
            WebclipBean bean = getWebclip();
            bean.setWebclipData(os.toString(CharEncoding.ISO_8859_1));
            PortletDaoFactory.getWebClipDao().updateWebclip(bean);
        } catch (MalformedURLException me) {
            log.error("Error get content from URL: " + url);
        } catch (IOException ioe) {
            log.error("Error get content from URL: " + url);
        }

        return "webclip";
    }
}
