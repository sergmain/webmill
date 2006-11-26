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
package org.riverock.webmill.port;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.template.PortalTemplateManager;
import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.site.PortalTemplateManagerImpl;

/**
 * $Id$
 */
public final class PortalInfoImpl implements Serializable, PortalInfo {
    private static final long serialVersionUID = 438274672384237876L;

    private transient final static Logger log = Logger.getLogger(PortalInfoImpl.class);

    private transient static Map<Long, PortalInfoImpl> portatInfoMap = new ConcurrentHashMap<Long, PortalInfoImpl>();

    private transient Site siteBean = new SiteBean();
    private transient List<SiteLanguage> siteLanguageList = null;

    private transient Locale defaultLocale = null;

    private transient XsltTransformerManager xsltTransformerManager = null;
    private transient PortalTemplateManager portalTemplateManager = null;

    private transient Map<String, Long> supportLanguageMap = null;
    private transient Map<String, MenuLanguage> languageMenuMap = null;

    private transient Long siteId = null;
    private transient Map<String, String> portalProperties = null;

    public Map<String, String> getPortalProperties() {
        return portalProperties;
    }

    public boolean isCurrentSite(Long siteLanguageId) {
        if (siteLanguageId == null)
            return false;

        for (SiteLanguage siteLanguage : siteLanguageList) {
            if (siteLanguageId.equals(siteLanguage.getSiteLanguageId())) {
                return true;
            }
        }
        return false;
    }

    public Long getSiteLanguageId(Locale locale) {
        if (log.isDebugEnabled()) {
            log.debug("get idSupportLanguage for locale " + locale.toString());
            log.debug("siteLanguageList " + getSiteLanguageList());
        }

        if (getSiteLanguageList() == null)
            return null;

        if (supportLanguageMap == null) {
            supportLanguageMap = new HashMap<String, Long>();
            for (SiteLanguage siteLanguage : siteLanguageList) {
                supportLanguageMap.put(siteLanguage.getCustomLanguage(), siteLanguage.getSiteLanguageId());
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
    private final static Object syncObject = new Object();

    // Todo replace synchronized with synchronized(syncObject)
    public synchronized static PortalInfoImpl getInstance(String serverName) {
        Long id = SiteList.getSiteId(serverName);
        if (log.isDebugEnabled())
            log.debug("ServerName:" + serverName + ", siteId: " + id);

        return getInstance(id);
    }

    public synchronized static PortalInfoImpl getInstance(Long siteId) {

        synchronized (syncObject) {

            PortalInfoImpl p = null;
            if (siteId != null)
                p = portatInfoMap.get(siteId);

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
        long mills; // System.currentTimeMillis();
        siteBean = InternalDaoFactory.getInternalSiteDao().getSite( siteId );

        if (StringUtils.isNotBlank(siteBean.getDefLanguage())) {

            defaultLocale = new Locale(siteBean.getDefLanguage().toLowerCase(),
                siteBean.getDefCountry() == null ? "" : siteBean.getDefCountry().toLowerCase(),
                siteBean.getDefVariant() == null ? "" : siteBean.getDefVariant().toLowerCase()
            );

            if (log.isDebugEnabled())
                log.debug("Main language 1.1: " + getDefaultLocale().toString());

        }
        else {
            defaultLocale = Locale.ENGLISH;
            if (log.isDebugEnabled()) {
                log.debug("PortalInfoImpl, set default locale to Locale.ENGLISH(en)");
            }
        }
        initPortalProperties();

        mills = 0;

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        xsltTransformerManager = new XsltTransformerManagerImpl(siteId);
        if (log.isInfoEnabled()) log.info("Init xsltTransformerManager for " + (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        portalTemplateManager = PortalTemplateManagerImpl.getInstance(siteId);
        if (log.isInfoEnabled()) log.info("Init portalTemplateManager for " + (System.currentTimeMillis() - mills) + " milliseconds");

        if (log.isInfoEnabled()) mills = System.currentTimeMillis();
        siteLanguageList = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList( siteId );
        if (log.isInfoEnabled()) log.info("Init language list for " + (System.currentTimeMillis() - mills) + " milliseconds");

        initMenu();
    }

    private void initPortalProperties() {
        portalProperties = new HashMap<String, String>();
        if (StringUtils.isNotBlank(siteBean.getProperties())) {
            Properties properties = new Properties();
            try {
                properties.load( new ByteArrayInputStream(siteBean.getProperties().getBytes()) );
            }
            catch (IOException e) {
                String es = "Error load properties from stream";
                log.error(es, e);
                throw new IllegalStateException( es, e);
            }
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                portalProperties.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        portalProperties.put( ContainerConstants.PORTAL_PROP_ADMIN_EMAIL, siteBean.getAdminEmail() );
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
        for (MenuLanguage menuLanguageInterface : sc.getMenuLanguage()) {
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

    public Site getSite() {
        return siteBean;
    }

    public List<SiteLanguage> getSiteLanguageList() {
        return siteLanguageList;
    }
}
