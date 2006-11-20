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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.portal.bean.*;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 17:35:33
 *         <p/>
 *         $Id$
 */
public class HibernateSiteDaoImpl implements InternalSiteDao {
    private final static Logger log = Logger.getLogger(HibernateSiteDaoImpl.class);

    public List<Site> getSites() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select site from org.riverock.webmill.portal.bean.SiteBean as site");
        List<SiteBean> siteList = query.list();
        session.getTransaction().commit();
        return (List)siteList;
    }

    public List<Site> getSites(AuthSession authSession) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List siteList = session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                "where site.companyId in (:companyIds)")
            .setParameterList("ompanyIds", authSession.getGrantedCompanyIdList())
            .list();
        session.getTransaction().commit();
        return (List)siteList;
    }

    public Site getSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
            "where site.siteId = :site_id");
        query.setLong("site_id", siteId);
        SiteBean site = (SiteBean)query.uniqueResult();
        session.getTransaction().commit();
        return site;
    }

    public Site getSite(String siteName) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
            "where site.siteName = :site_name");
        query.setString("site_name", siteName);
        SiteBean site = (SiteBean)query.uniqueResult();
        session.getTransaction().commit();
        return site;
    }

    public Long createSite(Site site) {
        return createSite(site, null);
    }

    public Long createSite(Site site, List<String> hosts) {
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
        session.beginTransaction();
        SiteBean bean = new SiteBean(site);
        session.save(bean);

        if (hosts!=null) {
            for (String s : hosts) {
                VirtualHost virtualHost = new VirtualHostBean(null, bean.getSiteId(), s );
                session.save(virtualHost);
            }
        }

        session.flush();
        session.getTransaction().commit();
        return bean.getSiteId();
    }

    public void updateSite(Site site) {
        updateSite(site, null);
    }

    public void updateSite(Site site, List<String> hosts) {
        Session session = HibernateUtils.getSession();
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
        session.getTransaction().commit();
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
                for (String host : hosts) {
                    if (virtualHost.getHost().equalsIgnoreCase(host)) {
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
            for (String host : hosts) {
                boolean isPresent=false;
                for (VirtualHost virtualHost : list) {
                    if (virtualHost.getHost().equalsIgnoreCase(host)) {
                        isPresent=true;
                        break;
                    }
                }
                if (!isPresent) {
                    VirtualHost hostBean = new VirtualHostBean(null, site.getSiteId(), host );
                    session.save(hostBean);
                }
            }
        }
    }

    public void deleteSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        List<NewsBean> newsBeans = session.createQuery(
            "select news " +
                "from  org.riverock.webmill.portal.bean.NewsBean news, " +
                "      org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                "      org.riverock.webmill.portal.bean.SiteBean site " +
                "where news.newsGroupId=newsGroup.newsGroupId and " +
                "      newsGroup.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();

        for (NewsBean newsBean : newsBeans) {
            session.delete(newsBean);
        }

        List<NewsGroupBean> groupBeans = session.createQuery(
            "select newsGroup " +
                "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                "      org.riverock.webmill.portal.bean.SiteBean site " +
                "where newsGroup.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();

        for (NewsGroupBean newsGroupBean : groupBeans) {
            session.delete(newsGroupBean);
        }

        List<ArticleBean> articleBeans = session.createQuery(
            "select article " +
                "from org.riverock.webmill.portal.bean.ArticleBean as article, " +
                "     org.riverock.webmill.portal.bean.SiteBean site " +
                "where article.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
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

        List<CssBean> cssList = session.createQuery(
            "select css from org.riverock.webmill.main.CssBean as css where css.siteId = :site_id")
            .setLong("site_id", siteId)
            .list();
        for (CssBean css : cssList) {
            session.delete(css);
        }

        List<PortalXsltBean> xsltList = session.createQuery(
            "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt, " +
                " org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where xslt.siteLanguageId = siteLanguage.siteLanguageId and siteLanguage.siteId = :site_id")
            .setLong("site_id", siteId)
            .list();
        for (PortalXsltBean portalXsltBean : xsltList) {
            session.delete(portalXsltBean);
        }

        VirtualHostBean virtualHostBean = (VirtualHostBean) session.createQuery(
            "select host from org.riverock.webmill.portal.bean.VirtualHostBean as host " +
            "where host.siteId = :site_id")
            .setLong("site_id", siteId)
            .uniqueResult();
        session.delete(virtualHostBean);

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

        List<SiteLanguageBean> list = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteId = :site_id")
            .setLong("site_id", siteId)
            .list();
        for (SiteLanguageBean siteLanguageBean : list) {
            session.delete(siteLanguageBean);
        }

        SiteBean siteBean = (SiteBean)session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
                "where site.siteId = :site_id")
            .setLong("site_id", siteId)
            .uniqueResult();
        session.delete(siteBean);

        session.getTransaction().commit();
    }
}
