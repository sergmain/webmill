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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.portal.bean.VirtualHostBean;
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
        Query query = session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
             "where site.companyId in ("+authSession.getGrantedCompanyId()+")"
        );
        List<SiteBean> siteList = query.list();
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
        session.flush();
        session.getTransaction().commit();

        if (hosts!=null) {
            for (String s : hosts) {
                VirtualHost host = new VirtualHostBean(null, bean.getSiteId(), s );
                InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(host);
            }
        }
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
            List<VirtualHost> list = InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(site.getSiteId());
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
                    InternalDaoFactory.getInternalVirtualHostDao().deleteVirtualHost(virtualHost);
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
                    InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(hostBean);
                }
            }
        }
    }

    public void deleteSite(Long siteId) {

        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            InternalDaoFactory.getInternalCmsDao().deleteArticleForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalCmsDao().deleteNewsForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalTemplateDao().deleteTemplateForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalCssDao().deleteCssForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalXsltDao().deleteXsltForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalVirtualHostDao().deleteVirtualHostForSite(dbDyn, siteId);
            InternalDaoFactory.getInternalSiteLanguageDao().deleteSiteLanguageForSite(dbDyn, siteId);

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error delete site";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
            dbDyn = null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select site from org.riverock.webmill.portal.bean.SiteBean as site " +
            "where site.siteId = :site_id");
        query.setLong("site_id", siteId);
        SiteBean bean = (SiteBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }
}
