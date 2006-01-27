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
package org.riverock.webmill.port;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.template.PortalTemplateManager;
import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.dao.PortalDaoFactory;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.site.PortalTemplateManagerImpl;

/**
 * $Id$
 */
public final class PortalInfoImpl implements Serializable, PortalInfo {
    private static final long serialVersionUID = 438274672384237876L;

    private transient final static Logger log = Logger.getLogger(PortalInfoImpl.class);

    private transient static Map<Long, PortalInfoImpl> portatInfoMap = new HashMap<Long, PortalInfoImpl>();

    private transient SiteBean siteBean = new SiteBean();
    private transient List<SiteLanguageBean> siteLanguageList = null;

    private transient Locale defaultLocale = null;

    private transient XsltTransformerManager xsltTransformerManager = null;
    private transient PortalTemplateManager portalTemplateManager = null;

    private transient Map<String, Long> supportLanguageMap = null;
    private transient Map<String, MenuLanguage> languageMenuMap = null;

    private transient Long siteId = null;
    private transient Map<String, String> meta = null;

    public Map<String, String> getMetadata() {
        return meta;
    }

    public boolean isCurrentSite(Long siteLanguageId) {
        if (siteLanguageId == null)
            return false;

        Iterator<SiteLanguageBean> it = siteLanguageList.iterator();
        while (it.hasNext()) {
            SiteLanguageBean siteLanguageBean = it.next();
            if (siteLanguageId.equals(siteLanguageBean.getSiteLanguageId())) {
                return true;
            }
        }
        return false;
    }

    public Long getSupportLanguageId(Locale locale) {
        if (log.isDebugEnabled()) {
            log.debug("get idSupportLanguage for locale " + locale.toString());
            log.debug("siteLanguageList " + getSiteLanguageList());
        }

        if (getSiteLanguageList() == null)
            return null;

        if (supportLanguageMap == null) {
            supportLanguageMap = new HashMap<String, Long>();
            Iterator<SiteLanguageBean> iterator = siteLanguageList.iterator();
            while (iterator.hasNext()) {
                SiteLanguageBean bean = iterator.next();
                supportLanguageMap.put(bean.getCustomLanguage(), bean.getSiteLanguageId());
            }
        }

        Long obj = supportLanguageMap.get(locale.toString());
        if (log.isDebugEnabled())
            log.debug("result object " + obj);

        if (obj == null)
            return null;

        return obj;
    }

    protected void finalize() throws Throwable {
        if (log.isDebugEnabled())
            log.debug("#15.09.01 finalize");

        defaultLocale = null;
        xsltTransformerManager = null;
        portalTemplateManager = null;

        super.finalize();
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 30000;
    private static Object syncObject = new Object();

    // Todo replace synchronized with synchronized(syncObject)
    public synchronized static PortalInfoImpl getInstance(String serverName) {
        if (log.isDebugEnabled()) {
            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());
            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +
                ((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD));
        }

        Long id = null;
        id = SiteList.getIdSite(serverName);
        if (log.isDebugEnabled()) log.debug("ServerName:" + serverName + ", siteId: " + id);

        return getInstance(id);
    }

    public synchronized static PortalInfoImpl getInstance(Long siteId) {

        synchronized (syncObject) {

            PortalInfoImpl p = null;
            if (siteId != null)
                p = (PortalInfoImpl) portatInfoMap.get(siteId);

            if ((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD || p == null) {
                log.debug("#15.01.03 reinit cached value ");

                p = new PortalInfoImpl(siteId);
                portatInfoMap.put(siteId, p);
            }
            lastReadData = System.currentTimeMillis();
            return p;
        }
    }

    private PortalInfoImpl(Long siteId) {
        this.siteId = siteId;
        long mills = 0; // System.currentTimeMillis();
        siteBean = PortalDaoFactory.getPortalDao().getSiteBean( siteId );

        if (!StringTools.isEmpty(siteBean.getDefLanguage()) &&
            !StringTools.isEmpty(siteBean.getDefCountry())) {

            defaultLocale = new Locale(siteBean.getDefLanguage().toLowerCase(),
                siteBean.getDefCountry(),
                (siteBean.getDefVariant() == null ? "" : siteBean.getDefVariant()).toLowerCase());

            if (log.isDebugEnabled())
                log.debug("Main language 1.1: " + getDefaultLocale().toString());

        }
        else {
            defaultLocale = StringTools.getLocale(WebmillConfig.getMainLanguage());
            if (log.isDebugEnabled()) {
                log.debug("Language, WebmillConfig.getMainLanguage(): " + WebmillConfig.getMainLanguage());
                log.debug("Language, default locale: " + getDefaultLocale().toString());
            }
        }
        initPortalProperties();

        mills = 0;

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        xsltTransformerManager = PortalXsltList.getInstance(siteId);
        if (log.isInfoEnabled()) log.info("Init xsltTransformerManager for " + (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        portalTemplateManager = PortalTemplateManagerImpl.getInstance(siteId);
        if (log.isInfoEnabled()) log.info("Init portalTemplateManager for " + (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        siteLanguageList = PortalDaoFactory.getPortalDao().getSiteLanguageList( siteId );
        if (log.isInfoEnabled()) log.info("Init language listfor " + (System.currentTimeMillis() - mills) + " milliseconds");

        initMenu();
    }

    private void initPortalProperties() {
        meta = new HashMap<String, String>();
        Properties properties = new Properties();
        try {
            // Todo investigate what value must passed to ByteArrayInputStream
            properties.load( new ByteArrayInputStream( "".getBytes() ) );
        }
        catch (IOException e) {
            String es = "Error load properties from stream";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
        Iterator<Map.Entry<Object,Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            meta.put( entry.getKey().toString(), entry.getValue().toString() );
        }
        meta.put( ContainerConstants.PORTAL_PROP_ADMIN_EMAIL, siteBean.getAdminEmail() );
    }

    public PortalInfoImpl() {
    }

    public void reinit() {
        synchronized (syncObject) {
            portatInfoMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_) {
        synchronized (syncObject) {
            portatInfoMap.clear();
        }
        lastReadData = 0;
    }

    public Long getSiteId() {
        return siteId;
    }

    public Long getCompanyId() {
        return siteBean.getCompanyId();
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public XsltTransformerManager getXsltTransformerManager() {
        return this.xsltTransformerManager;
    }

    public PortalTemplateManager getPortalTemplateManager() {
        return portalTemplateManager;
    }

    private void initMenu() {
        if (log.isDebugEnabled()) log.debug("start get menu for site " + siteId);

        // Build menu
        SiteMenu sc = SiteMenu.getInstance(siteId);
        languageMenuMap = new HashMap<String, MenuLanguage>();
        Iterator<MenuLanguage> iterator = sc.getMenuLanguage().iterator();
        while (iterator.hasNext()) {
            MenuLanguage menuLanguageInterface = iterator.next();
            languageMenuMap.put(menuLanguageInterface.getLocaleStr(), menuLanguageInterface);
        }
    }

    public MenuLanguage getMenu(String locale) {
        MenuLanguage tempCat = null;
        if (locale != null)
            tempCat = languageMenuMap.get(locale);

        if (tempCat != null)
            return tempCat;
        else
            log.warn("Menu for locale " + locale + " not found");

        return null;
    }

    public SiteBean getSite() {
        return siteBean;
    }

    public List getSiteLanguageList() {
        return siteLanguageList;
    }
}
