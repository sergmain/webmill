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
package org.riverock.webmill.portal.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
//import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.dao.HibernateUtils;

import java.util.*;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 18:00:16
 *         <p/>
 *         $Id$
 */
public class HibernateCatalogDaoImpl implements InternalCatalogDao {
    private final static Logger log = Logger.getLogger(HibernateCatalogDaoImpl.class);
    private static final MenuItemComparator MENU_ITEM_COMPARATOR = new MenuItemComparator();

    private final Observable observable = new Observable();

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogItemId) {
        if (siteId == null || locale==null || catalogItemId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLang.siteLanguageId and " +
                    "      siteLang.siteId=:siteId and siteLang.customLanguage=:customLanguage and " +
                    "      catalog.catalogId=:catalogId")
                .setLong("siteId", siteId)
                .setLong("catalogId", catalogItemId)
                .setString("customLanguage", locale.toString().toLowerCase())
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public Long getCatalogItemId(Long siteLanguageId, Long portletNameId, Long templateId) {
        if (log.isDebugEnabled()) {
            log.debug("InternalDaoCatalogImpl.getCatalogItemId()");
            log.debug("     siteLanguageId: " + siteLanguageId);
            log.debug("     portletNameId: " + portletNameId);
            log.debug("     templateId: " + templateId);
        }

        if (siteLanguageId == null || portletNameId==null || templateId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=:siteLanguageId and " +
                    "      catalog.portletId=:portletId and catalog.templateId=:templateId")
                .setLong("siteLanguageId", siteLanguageId)
                .setLong("portletId", portletNameId)
                .setLong("templateId", templateId)
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public Long getCatalogItemId(Long siteLanguageId, String pageUrl) {
        if (siteLanguageId==null || pageUrl==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=:siteLanguageId and catalog.url=:url")
                .setLong("siteLanguageId", siteLanguageId)
                .setString("url", pageUrl)
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        if (portletName==null || siteId==null || locale==null || templateName==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {

            String resultPortletName = portletName;
            if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
                resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
            }

            if (log.isDebugEnabled()) {
                log.debug("HibernateCatalogDaoImpl.getCatalogItemId()");
                log.debug("     siteId: " + siteId);
                log.debug("     locale: " + locale.toString().toLowerCase() );
                log.debug("     portletName: " + portletName);
                log.debug("     resultPortletName: " + resultPortletName);
                log.debug("     templateName: " + templateName);
            }

            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang, " +
                    "      org.riverock.webmill.portal.bean.PortletNameBean portlet, " +
                    "      org.riverock.webmill.portal.bean.TemplateBean template " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLang.siteLanguageId and " +
                    "      siteLang.siteId=:siteId and siteLang.customLanguage=:customLanguage and " +
                    "      catalog.portletId=portlet.portletId and " +
                    "      catalog.templateId=template.templateId and" +
                    "      portlet.portletName=:portletName and template.templateName=:templateName")
                .setLong("siteId", siteId)
                .setString("customLanguage", locale.toString().toLowerCase())
                .setString("portletName", resultPortletName)
                .setString("templateName", templateName)
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName, Long catalogId) {
        if (portletName==null || siteId==null || locale==null || templateName==null || catalogId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {

            String resultPortletName = portletName;
            if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
                resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
            }

            if (log.isDebugEnabled()) {
                log.debug("HibernateCatalogDaoImpl.getCatalogItemId()");
                log.debug("     siteId: " + siteId);
                log.debug("     locale: " + locale.toString().toLowerCase() );
                log.debug("     portletName: " + portletName);
                log.debug("     resultPortletName: " + resultPortletName);
                log.debug("     templateName: " + templateName);
                log.debug("     catalogId: " + catalogId);
            }

            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang, " +
                    "      org.riverock.webmill.portal.bean.PortletNameBean portlet, " +
                    "      org.riverock.webmill.portal.bean.TemplateBean template " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLang.siteLanguageId and " +
                    "      siteLang.siteId=:siteId and siteLang.customLanguage=:customLanguage and " +
                    "      catalog.portletId=portlet.portletId and " +
                    "      catalog.templateId=template.templateId and" +
                    "      portlet.portletName=:portletName and template.templateName=:templateName and " +
                    "      catalog.catalogId=:catalogId")
                .setLong("catalogId", catalogId)
                .setLong("siteId", siteId)
                .setString("customLanguage", locale.toString().toLowerCase())
                .setString("portletName", resultPortletName)
                .setString("templateName", templateName)
                .uniqueResult();

            return id;
        }
        finally {
            session.close();
        }
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageUrl) {
        if (siteId == null || locale==null || pageUrl==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Long id = (Long)session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLang.siteLanguageId and " +
                    "      siteLang.siteId=:siteId and lower(siteLang.customLanguage)=:customLanguage and " +
                    "      catalog.url=:url")
                .setLong("siteId", siteId)
                .setString("customLanguage", locale.toString().toLowerCase())
                .setString("url", pageUrl)
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public CatalogItem getCatalogItem(Long catalogId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCatalogItem(), catalogId: "+catalogId);
        }
        if (catalogId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            CatalogBean bean = (CatalogBean)session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                    "where catalog.catalogId=:catalogId")
                .setLong("catalogId", catalogId)
                .uniqueResult();

            if (log.isDebugEnabled() && bean!=null) {
                log.debug("Metadata: " + bean.getMetadata());
            }
            return bean;
        }
        finally {
            session.close();
        }
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<CatalogItem> list = session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                    "where catalog.catalogLanguageId=:catalogLanguageId")
                .setLong("catalogLanguageId", catalogLanguageId)
                .list();

            Collections.sort(list, MENU_ITEM_COMPARATOR);

            return list;
        }
        finally {
            session.close();
        }
    }

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId ) {
        if (catalogLanguageId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                    "where catalogLang.catalogLanguageId=:catalogLanguageId")
                .setLong("catalogLanguageId", catalogLanguageId)
                .uniqueResult();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List list = session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                    "where catalogLang.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .list();
            return list;
        }
        finally {
            session.close();
        }
    }

    private void reinitMenuCache(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return;
        }
        observable.notifyObservers(siteLanguageId);
//        SiteMenu.invalidateCache(siteLanguageId);
//        PortalInfoImpl.invalidateCache(siteLanguageId);
    }

    public Long createCatalogItem(CatalogItem catalogItem) {
        if (catalogItem == null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        Long siteLanguageId;
        CatalogBean bean;
        try {
            session.beginTransaction();

            siteLanguageId = getCatalogLanguageItem(catalogItem.getCatalogLanguageId()).getSiteLanguageId();
            if (isUrlExist(catalogItem.getUrl(), siteLanguageId)) {
                log.debug("Url '"+catalogItem.getUrl()+"' for siteLanguageId: " + siteLanguageId+" exist");
                return null;
            }

            bean = new CatalogBean(catalogItem);
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        if (log.isDebugEnabled()) {
            log.debug("invalidate menu for siteLanguageId: " + siteLanguageId+", catalogLabguageId: " +catalogItem.getCatalogLanguageId());
        }
        reinitMenuCache(siteLanguageId);

        return bean.getCatalogId();
    }

    public void updateCatalogItem(CatalogItem catalogItem) {
        if (catalogItem==null) {
            return;
        }

        Long siteLanguageId = getCatalogLanguageItem(catalogItem.getCatalogLanguageId()).getSiteLanguageId();
        if (!isUpdatable(catalogItem.getUrl(), siteLanguageId, catalogItem.getCatalogId())) {
            log.debug("Url '"+catalogItem.getUrl()+"' for siteLanguageId: " + siteLanguageId+" and catalogId "+catalogItem.getCatalogId()+" can't assigned.");
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            CatalogBean bean = (CatalogBean)session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                    "where catalog.catalogId=:catalogId")
                .setLong("catalogId", catalogItem.getCatalogId())
                .uniqueResult();

            if (bean==null) {
                log.warn("Catalog item bean is null!");
                session.getTransaction().commit();
                return;
            }
            bean.setAuthor(catalogItem.getAuthor());
            bean.setKeyword(catalogItem.getKeyword());
            bean.setTitle(catalogItem.getTitle());
            bean.setUrl(catalogItem.getUrl());
            bean.setContextId(catalogItem.getContextId());
            bean.setCatalogLanguageId(catalogItem.getCatalogLanguageId());
            bean.setPortletId(catalogItem.getPortletId());
            bean.setTemplateId(catalogItem.getTemplateId());
            bean.setTopCatalogId(catalogItem.getTopCatalogId());
            bean.setKeyMessage(catalogItem.getKeyMessage());
            bean.setIncludeInSitemap(catalogItem.isIncludeInSitemap());

            if (log.isDebugEnabled()) {
                log.debug("contextId: " + catalogItem.getContextId());
                log.debug("metadata: " + catalogItem.getMetadata());
            }

            bean.setMetadata(catalogItem.getMetadata());
            bean.setOrderField(catalogItem.getOrderField());
            bean.setPortletRole(catalogItem.getPortletRole());

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache(siteLanguageId);
    }

    public void deleteCatalogItem(Long catalogId) {
        if (catalogId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        Long siteLanguageId;
        try {
            session.beginTransaction();

            siteLanguageId = null;
            CatalogBean bean = (CatalogBean)session.createQuery(
                "select catalog from org.riverock.webmill.portal.bean.CatalogBean catalog where catalog.catalogId=:catalogId")
                .setLong("catalogId", catalogId)
                .uniqueResult();
            if (bean!=null) {
                siteLanguageId=getCatalogLanguageItem(bean.getCatalogLanguageId()).getSiteLanguageId();
                session.delete(bean);
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache( siteLanguageId );

    }

    public Long createCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        if (catalogLanguageItem == null) {
            return null;
        }
        if (log.isDebugEnabled()) {
            log.debug("Item getIdSiteCtxLangCatalog(), value - "+catalogLanguageItem.getCatalogLanguageId());
            log.debug("Item getCatalogCode(), value - "+catalogLanguageItem.getCatalogCode());
            log.debug("Item getIsDefault(), value - "+catalogLanguageItem.getDefault());
            log.debug("Item getIdSiteSupportLanguage(), value - "+catalogLanguageItem.getSiteLanguageId());
        }

        Session session = HibernateUtils.getSession();
        CatalogLanguageBean bean;
        try {
            session.beginTransaction();
            bean = new CatalogLanguageBean(catalogLanguageItem);
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache(catalogLanguageItem.getSiteLanguageId());

        return bean.getCatalogLanguageId();
    }

    public void updateCatalogLanguageItem(CatalogLanguageItem catalogLanguageItem) {
        if (log.isDebugEnabled()) {
            log.debug("catalogLanguageItem: " + catalogLanguageItem);
        }
        if (catalogLanguageItem==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                    "where catalogLang.catalogLanguageId=:catalogLanguageId")
                .setLong("catalogLanguageId", catalogLanguageItem.getCatalogLanguageId())
                .uniqueResult();

            if (bean==null) {
                session.getTransaction().commit();
                return;
            }
            bean.setCatalogCode(catalogLanguageItem.getCatalogCode());
            bean.setDefault(catalogLanguageItem.getDefault());
            bean.setSiteLanguageId(catalogLanguageItem.getSiteLanguageId());

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache(catalogLanguageItem.getSiteLanguageId());
    }

    public void deleteCatalogLanguageItem(Long catalogLanguageId) {
        if (log.isDebugEnabled()) {
           log.debug("catalogLanguageId: " +catalogLanguageId);
        }
        if (catalogLanguageId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        Long siteLanguageId;
        try {
            session.beginTransaction();

            CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                    "where catalogLang.catalogLanguageId=:catalogLanguageId")
                .setLong("catalogLanguageId", catalogLanguageId)
                .uniqueResult();

            siteLanguageId = null;
            if (bean!=null) {
                siteLanguageId=bean.getSiteLanguageId();
                session.delete(bean);
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache(siteLanguageId);

    }

    public void deleteCatalogLanguageForSiteLanguage(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            // TODO rewrite hql
            List<CatalogBean> catalogItems = session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .list();
            for (CatalogBean catalogItem : catalogItems) {
                session.delete(catalogItem);
            }

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalogLang.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }

        reinitMenuCache(siteLanguageId);

    }

    public Long getSiteId(Long catalogLanguageId) {
        if (catalogLanguageId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        Long id;
        try {
            id = (Long)session.createQuery(
            "select siteLang.siteId " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang " +
                    "where catalogLang.catalogLanguageId=:catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLang.siteLanguageId ")
                .setLong("catalogLanguageId", catalogLanguageId)
                .uniqueResult();
            return id;
        }
        finally {
            session.close();
        }
    }

    public boolean isUrlExist(String url, Long siteLanguageId) {
        if (StringUtils.isBlank(url) || siteLanguageId==null) {
            return false;
        }

        String urlReal = url.trim().toLowerCase();
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List ids = session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=:siteLanguageId and lower(catalog.url)=:url")
                .setLong("siteLanguageId", siteLanguageId)
                .setString("url", urlReal)
                .list();

            return !ids.isEmpty();
        }
        finally {
            session.close();
        }
    }

    public void changeTemplateForCatalogLanguage(Long catalogLanguageId, Long templateId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            List<CatalogBean> items = session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                    "where catalog.catalogLanguageId=:catalogLanguageId")
                .setLong("catalogLanguageId", catalogLanguageId)
                .list();

            boolean isFound = false;
            for (CatalogBean item : items) {
                item.setTemplateId(templateId);
                isFound = true;
            }

            if (isFound) {
                CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
                    "select catalogLang " +
                        "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                        "where catalogLang.catalogLanguageId=:catalogLanguageId")
                    .setLong("catalogLanguageId", catalogLanguageId)
                    .uniqueResult();
                if (bean!=null) {
                    reinitMenuCache(bean.getSiteLanguageId());
                }
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public boolean isUpdatable(String url, Long siteLanguageId, Long catalogId) {
        if (StringUtils.isBlank(url)) {
            return true;
        }
        if (siteLanguageId==null || catalogId==null) {
            throw new RuntimeException("siteLanguageId and catalogId can not be null");
        }
        
        String urlReal = url.trim().toLowerCase();
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<Long> ids = session.createQuery(
                "select catalog.catalogId " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=:siteLanguageId and " +
                    "      lower(catalog.url)=:url")
                .setLong("siteLanguageId", siteLanguageId)
                .setString("url", urlReal)
                .list();

            boolean flag=true;
            for (Long id : ids) {
                if (!id.equals(catalogId)) {
                    flag = false;
                }
            }

            return flag;
        }
        finally {
            session.close();
        }
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        if (catalogLanguageCode==null || siteLanguageId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                    "where catalogLang.catalogCode=:catalogCode and catalogLang.siteLanguageId=:siteLanguageId")
                .setString("catalogCode", catalogLanguageCode)
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    final static class MenuItemComparator implements Comparator<CatalogItem> {
        public int compare(CatalogItem o1, CatalogItem o2) {

            if (o1==null && o2==null)
                return 0;
            if (o1==null)
                return 1;
            if (o2==null)
                return -1;

            // "order by a.ID_TOP_CTX_CATALOG ASC, a.ORDER_FIELD ASC, KEY_MESSAGE ASC ";
            if ( o1.getTopCatalogId().equals( o2 .getTopCatalogId()))
            {
                if ( o1.getOrderField()==null && o2.getOrderField()==null) {
                    return compareNames(o1, o2);
                }

                if ( o1.getOrderField()!=null && o2.getOrderField()==null )
                    return -1;

                if ( o1.getOrderField()==null && o2.getOrderField()!=null)
                    return 1;

                // KEY_MESSAGE
                if (o1.getOrderField().equals( o2.getOrderField()) ){
                    return compareNames(o1, o2);
                }
                else {
                    return o1.getOrderField().compareTo( o2.getOrderField() );
                }
            }
            else {
                return o1.getTopCatalogId().compareTo( o2.getTopCatalogId() );
            }
        }

        private static int compareNames(CatalogItem o1, CatalogItem o2) {
            if ( o1.getKeyMessage()==null && o2.getKeyMessage()==null)
                return 0;

            if ( o1.getKeyMessage()!=null && o2.getKeyMessage()==null )
                return -1;

            if ( o1.getKeyMessage()==null && o2.getKeyMessage()!=null)
                return 1;

            int i = o1.getKeyMessage().compareTo(o2.getKeyMessage());
            return i==0?0:(i>0?1:-1);
        }
    }
}
