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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.*;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 17:35:33
 *         <p/>
 *         $Id$
 */
public class HibernateSiteDaoImpl implements InternalSiteDao {
    private final static Logger log = Logger.getLogger(HibernateSiteDaoImpl.class);

    private final Observable observable = new Observable();

    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    public List<Site> getSites() {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery("select site from org.riverock.webmill.portal.bean.SiteBean as site");
            List<SiteBean> siteList = query.list();
            return (List)siteList;
        }
        finally {
            session.close();
        }
    }

    public List<Site> getSites(AuthSession authSession) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List siteList = session.createQuery(
                "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                    "where site.companyId in (:companyIds)")
                .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
                .list();
            return (List)siteList;
        }
        finally {
            session.close();
        }
    }

    public Site getSite(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                "where site.siteId = :site_id");
            query.setLong("site_id", siteId);
            SiteBean site = (SiteBean)query.uniqueResult();
            return site;
        }
        finally {
            session.close();
        }
    }

    public Site getSite(String siteName) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                "where site.siteName = :site_name");
            query.setString("site_name", siteName);
            SiteBean site = (SiteBean)query.uniqueResult();
            return site;
        }
        finally {
            session.close();
        }
    }

    public Long createSite(Site site) {
        return createSite(site, null);
    }

    public Long createSite(Site site, List<VirtualHost> hosts) {
        if (log.isDebugEnabled()) {
            log.debug("site: " + site);
            if (site!=null) {
                log.debug("    language: " + site.getDefLanguage());
                log.debug("    country: " + site.getDefCountry());
                log.debug("    variant: " + site.getDefVariant());
                log.debug("    companyId: " + site.getCompanyId());
            }
        }

        Session session = HibernateUtils.getSession();
        SiteBean siteBean;
        try {
            session.beginTransaction();
            siteBean = new SiteBean(site);
            session.save(siteBean);

            if (hosts!=null) {
                for (VirtualHost virtualHost : hosts) {
                    VirtualHostBean host = new VirtualHostBean(null, siteBean.getSiteId(), virtualHost.getHost(), virtualHost.isDefaultHost() );
                    session.save(host);
                }
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
            return siteBean.getSiteId();
        }
        finally {
            session.close();
        }
    }

    public void updateSite(Site site) {
        updateSite(site, null);
    }

    public void updateSite(Site site, List<VirtualHost> hosts) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery(
                "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                "where site.siteId = :site_id");
            query.setLong("site_id", site.getSiteId());
            SiteBean bean = (SiteBean)query.uniqueResult();
            if (bean!=null) {
                bean.setAdminEmail(site.getAdminEmail());
                bean.setCssFile(site.getCssFile());
                bean.setDefCountry(site.getDefCountry());
                bean.setDefLanguage(site.getDefLanguage());
                bean.setDefVariant(site.getDefVariant());
                bean.setCompanyId(site.getCompanyId());
                bean.setCssDynamic(site.getCssDynamic());
                bean.setRegisterAllowed(site.getRegisterAllowed());
                bean.setSiteName(site.getSiteName());
                bean.setProperties(site.getProperties());
            }
            if (bean!=null && hosts!=null) {
                List<VirtualHostBean> list = session.createQuery(
                    "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
                    "where host.siteId = :site_id")
                    .setLong("site_id", site.getSiteId())
                    .list();

                if (log.isDebugEnabled()) {
                    log.debug("current hosts in DB: " + list);
                    for (VirtualHost virtualHost : list) {
                        log.debug("    host: " + virtualHost.getHost()+", id; " + virtualHost.getId());
                    }
                }

                for (VirtualHost virtualHost : list) {
                    boolean isPresent=false;
                    for (VirtualHost host : hosts) {
                        if (virtualHost.getHost().equalsIgnoreCase(host.getHost())) {
                            isPresent=true;
                            break;
                        }
                    }
                    if (!isPresent) {
                        VirtualHostBean virtualHostBean = (VirtualHostBean) session.createQuery(
                            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
                            "where host.id = :id")
                            .setLong("id", virtualHost.getId())
                            .uniqueResult();
                        session.delete(virtualHostBean);
                    }
                }
                for (VirtualHost host : hosts) {
                    VirtualHostBean virtualHostTemp=null;
                    for (VirtualHostBean virtualHost : list) {
                        if (virtualHost.getHost().equalsIgnoreCase(host.getHost())) {
                            virtualHostTemp=virtualHost;
                            break;
                        }
                    }
                    if (virtualHostTemp==null) {
                        VirtualHost hostBean = new VirtualHostBean(null, site.getSiteId(), host.getHost(), host.isDefaultHost() );
                        session.save(hostBean);
                    }
                    else if (virtualHostTemp.isDefaultHost()!=host.isDefaultHost()) {
                        virtualHostTemp.setDefaultHost(host.isDefaultHost());
                        session.save(virtualHostTemp);
                    }
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

    public void deleteSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            List<NewsBean> newsBeans = session.createQuery(
                "select news " +
                    "from  org.riverock.webmill.portal.bean.NewsBean news, " +
                    "      org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where news.newsGroupId=newsGroup.newsGroupId and " +
                    "      newsGroup.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();

            for (NewsBean newsBean : newsBeans) {
                session.delete(newsBean);
            }

            List<NewsGroupBean> groupBeans = session.createQuery(
                "select newsGroup " +
                    "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where newsGroup.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();

            for (NewsGroupBean newsGroupBean : groupBeans) {
                session.delete(newsGroupBean);
            }

            List<ArticleBean> articleBeans = session.createQuery(
                "select article " +
                    "from  org.riverock.webmill.portal.bean.ArticleBean as article, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where article.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();

            for (ArticleBean articleBean : articleBeans) {
                session.delete(articleBean);
            }

            List<TemplateBean> templateBeans = session.createQuery(
                "select template " +
                    "from  org.riverock.webmill.portal.bean.TemplateBean as template," +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLanguage " +
                    "where template.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (TemplateBean templateBean : templateBeans) {
                session.delete(templateBean);
            }

            session.createQuery(
            "delete org.riverock.webmill.main.CssBean as css where css.siteId = :site_id")
                .setLong("site_id", siteId)
                .executeUpdate();

            List<PortalXsltBean> xsltList = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt, " +
                    " org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where xslt.siteLanguageId = siteLanguage.siteLanguageId and siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (PortalXsltBean portalXsltBean : xsltList) {
                session.delete(portalXsltBean);
            }

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.VirtualHostBean as host " +
                "where host.siteId = :site_id")
                .setLong("site_id", siteId)
                .executeUpdate();

            List<CatalogBean> catalogItems = session.createQuery(
                "select catalog " +
                    "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                    "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                    "      catalogLang.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (CatalogBean catalogItem : catalogItems) {
                session.delete(catalogItem);
            }

            List<CatalogLanguageBean> catalogLanguageBeans = session.createQuery(
                "select catalogLang " +
                    "from  org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where catalogLang.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (CatalogLanguageBean catalogLanguageBean : catalogLanguageBeans) {
                session.delete(catalogLanguageBean);
            }

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.SiteLanguageBean siteLanguage " +
                "where siteLanguage.siteId = :site_id")
                .setLong("site_id", siteId)
                .executeUpdate();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.SiteBean site where site.siteId = :site_id")
                .setLong("site_id", siteId)
                .executeUpdate();


            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            observable.notifyObservers();
//            SiteList.destroy();
            session.close();
        }
    }
}
