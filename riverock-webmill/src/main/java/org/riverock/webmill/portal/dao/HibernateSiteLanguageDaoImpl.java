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

import org.hibernate.Query;
import org.hibernate.Session;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 17:35:14
 *         <p/>
 *         $Id$
 */
public class HibernateSiteLanguageDaoImpl implements InternalSiteLanguageDao {
    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<SiteLanguageBean> cssList = query.list();
        session.getTransaction().commit();
        return (List)cssList;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguageId);
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteId = :site_id and siteLanguage.customLanguage = :custom_language ");
        query.setLong("site_id", siteId);
        query.setString("custom_language", languageLocale);
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        SiteLanguageBean bean = new SiteLanguageBean(siteLanguage);
        session.save(bean);
        session.flush();
        session.getTransaction().commit();
        return bean.getSiteLanguageId();
    }

    public void updateSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguage.getSiteLanguageId());
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        if (bean!=null) {
            bean.setCustomLanguage(siteLanguage.getCustomLanguage());
            bean.setNameCustomLanguage(siteLanguage.getNameCustomLanguage());
            bean.setSiteId(siteLanguage.getSiteId());
        }
        session.getTransaction().commit();
    }

    public void deleteSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguageId);
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        session.delete(bean);

        session.getTransaction().commit();
    }

    public void deleteSiteLanguageForSite(DatabaseAdapter adapter, Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<SiteLanguageBean> list = query.list();
        for (SiteLanguageBean bean : list) {
            session.delete(bean);
        }
        session.getTransaction().commit();
    }
}
