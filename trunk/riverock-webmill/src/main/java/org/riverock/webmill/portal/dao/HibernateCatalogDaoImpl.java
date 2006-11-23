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

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 18:00:16
 *         <p/>
 *         $Id$
 */
public class HibernateCatalogDaoImpl implements InternalCatalogDao {
    private final static Logger log = Logger.getLogger(HibernateCatalogDaoImpl.class);

    public Long getCatalogItemId(Long siteId, Locale locale, Long catalogItemId) {
        if (siteId == null || locale==null || catalogItemId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
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
        session.getTransaction().commit();
        return id;
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

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
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
        session.getTransaction().commit();
        return id;
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName) {
        if (portletName==null || siteId==null || locale==null || templateName==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

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
        session.getTransaction().commit();
        return id;
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String portletName, String templateName, Long catalogId) {
        if (portletName==null || siteId==null || locale==null || templateName==null || catalogId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

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
        session.getTransaction().commit();
        return id;
    }

    public Long getCatalogItemId(Long siteId, Locale locale, String pageName) {
        if (siteId == null || locale==null || pageName==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Long id = (Long)session.createQuery(
            "select catalog.catalogId " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang " +
                "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                "      catalogLang.siteLanguageId=siteLang.siteLanguageId and " +
                "      siteLang.siteId=:siteId and siteLang.customLanguage=:customLanguage and " +
                "      catalog.url=:url")
            .setLong("siteId", siteId)
            .setString("customLanguage", locale.toString().toLowerCase())
            .setString("url", pageName)
            .uniqueResult();
        session.getTransaction().commit();
        return id;
    }

    public CatalogItem getCatalogItem(Long catalogId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCatalogItem(), catalogId: "+catalogId);
        }
        if (catalogId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CatalogBean bean = (CatalogBean)session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                "where catalog.catalogId=:catalogId")
            .setLong("catalogId", catalogId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<CatalogItem> getCatalogItemList(Long catalogLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<CatalogItem> list = session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                "where catalog.catalogLanguageId=:catalogLanguageId")
            .setLong("catalogLanguageId", catalogLanguageId)
            .list();

        session.getTransaction().commit();

        Collections.sort(list, new MenuItemComparator());

        return list;
    }

    public CatalogLanguageItem getCatalogLanguageItem(Long catalogLanguageId ) {
        if (catalogLanguageId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                "where catalogLang.catalogLanguageId=:catalogLanguageId")
            .setLong("catalogLanguageId", catalogLanguageId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<CatalogLanguageItem> getCatalogLanguageItemList(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List list = session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                "where catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long createCatalogItem(CatalogItem catalogItem) {
        if (catalogItem == null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CatalogBean bean = new CatalogBean(catalogItem);
        session.save(bean);

        session.flush();
        session.getTransaction().commit();
        return bean.getCatalogId();
    }

    public void updateCatalogItem(CatalogItem catalogItem) {
        if (catalogItem==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CatalogBean bean = (CatalogBean)session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                "where catalog.catalogId=:catalogId")
            .setLong("catalogId", catalogItem.getCatalogId())
            .uniqueResult();

        if (bean==null) {
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
        bean.setMetadata(catalogItem.getMetadata());
        bean.setOrderField(catalogItem.getOrderField());
        bean.setPortletRole(catalogItem.getPortletRole());

        session.getTransaction().commit();
    }

    public void deleteCatalogItem(Long catalogId) {
        if (catalogId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CatalogBean bean = (CatalogBean)session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog " +
                "where catalog.catalogId=:catalogId")
            .setLong("catalogId", catalogId)
            .uniqueResult();

        if (bean==null) {
            session.getTransaction().commit();
            return;
        }
        session.delete(bean);
        session.getTransaction().commit();
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
        session.beginTransaction();
        CatalogLanguageBean bean = new CatalogLanguageBean(catalogLanguageItem);
        session.save(bean);

        session.flush();
        session.getTransaction().commit();
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

        session.getTransaction().commit();
    }

    public void deleteCatalogLanguageItem(Long catalogLanguageId) {
        if (log.isDebugEnabled()) {
           log.debug("catalogLanguageId: " +catalogLanguageId);
        }
        if (catalogLanguageId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                "where catalogLang.catalogLanguageId=:catalogLanguageId")
            .setLong("catalogLanguageId", catalogLanguageId)
            .uniqueResult();

        if (bean==null) {
            session.getTransaction().commit();
            return;
        }
        session.delete(bean);
        session.getTransaction().commit();
    }

    public void deleteCatalogLanguageForSiteLanguage(Long siteLanguageId) {
        if (siteLanguageId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

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

        List<CatalogLanguageBean> catalogLanguageBeans = session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                "where catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (CatalogLanguageBean catalogLanguageBean : catalogLanguageBeans) {
            session.delete(catalogLanguageBean);
        }

        session.getTransaction().commit();
    }

    public CatalogLanguageItem getCatalogLanguageItem(String catalogLanguageCode, Long siteLanguageId) {
        if (catalogLanguageCode==null || siteLanguageId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        CatalogLanguageBean bean = (CatalogLanguageBean)session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean as catalogLang " +
                "where catalogLang.catalogCode=:catalogCode and catalogLang.siteLanguageId=:siteLanguageId")
            .setString("catalogCode", catalogLanguageCode)
            .setLong("siteLanguageId", siteLanguageId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    private final static class MenuItemComparator implements Comparator<CatalogItem> {
        public int compare(CatalogItem o1, CatalogItem o2) {

            if (o1==null && o2==null)
                return 0;
            if (o1==null)
                return 1;
            if (o2==null)
                return -1;

            // "order by a.ID_TOP_CTX_CATALOG ASC, a.ORDER_FIELD ASC ";
            if ( o1.getTopCatalogId().equals( o2 .getTopCatalogId()))
            {
                if ( o1.getOrderField()==null && o2.getOrderField()==null)
                    return 0;

                if ( o1.getOrderField()!=null && o2.getOrderField()==null )
                    return -1;

                if ( o1.getOrderField()==null && o2.getOrderField()!=null)
                    return 1;

                return o1.getOrderField().compareTo( o2.getOrderField() );
            }
            else
                return o1.getTopCatalogId().compareTo( o2.getTopCatalogId() );
        }
    }
}
