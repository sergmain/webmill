/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.webmill.main.CssBean;
import org.hibernate.Session;
import org.hibernate.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.sql.Blob;

/**
 * User: SergeMaslyukov
 * Date: 08.10.2006
 * Time: 17:49:19
 * <p/>
 * $Id$
 */
public class HibernateCssDaoImpl implements InternalCssDao {
    private static Logger log = Logger.getLogger(HibernateCssDaoImpl.class);

    public Css getCssCurrent(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.isCurrent=true and css.siteId = :site_id");
        query.setLong("site_id", siteId);
        Css css = (Css)query.uniqueResult();
        session.getTransaction().commit();
        return css;
    }

    public List<Css> getCssList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<Css> cssList = query.list();
        session.getTransaction().commit();
        return cssList;
    }

    public Css getCss(Long cssId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.cssId = :css_id");
        query.setLong("css_id", cssId);
        Css css = (Css)query.uniqueResult();
        session.getTransaction().commit();
        return css;
    }

    public Long createCss(Css css) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        clearCurrentFlag(css.getSiteId(), session);

        CssBean bean = new CssBean();
        bean.setCssComment(css.getCssComment());
        bean.setCurrent(css.isCurrent());
        bean.setDate(css.getDate());
        bean.setSiteId(css.getSiteId());
        session.save(bean);

        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.cssId = :css_id");
        query.setLong("css_id", bean.getCssId());
        bean = (CssBean)query.uniqueResult();

        try {
            if (StringUtils.isNotBlank(css.getCss())) {
                Blob blob = bean.getCssBlob();
                if (blob==null) {
                    throw new RuntimeException("Blob object is null");
                }
                blob.setBytes(1, css.getCss().getBytes());
            }
        }
        catch (Exception e) {
            String es = "Error in createCss()";
            log.error(es, e);
            throw new RuntimeException(es, e);
        }

        session.getTransaction().commit();

        return bean.getCssId();
    }

    public void updateCss(Css css) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        if (css.isCurrent()) {
            clearCurrentFlag(css.getSiteId(), session);
        }

        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.cssId = :css_id");
        query.setLong("css_id", css.getCssId());
        CssBean bean = (CssBean)query.uniqueResult();

        bean.setCssComment(css.getCssComment());
        bean.setCurrent(css.isCurrent());
        bean.setDate(css.getDate());
        bean.setSiteId(css.getSiteId());
        try {
            Blob blob = bean.getCssBlob();
            blob.setBytes(1, css.getCss().getBytes());
        }
        catch (Exception e) {
            String es = "Error in updateCss()";
            log.error(es, e);
            throw new RuntimeException(es, e);
        }
        session.getTransaction().commit();
    }

    public void deleteCss(Long cssId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.cssId = :css_id");
        query.setLong("css_id", cssId);
        CssBean bean = (CssBean)query.uniqueResult();
        if (!bean.isCurrent()) {
            session.delete(bean);
        }

        session.getTransaction().commit();
    }

    public void deleteCssForSite(DatabaseAdapter adapter, Long siteId) {
        throw new RuntimeException("Will not be implemented");
    }

    public void deleteCssForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.siteId = :site_id");
        query.setLong("site_id", siteId);
        List cssList = query.list();
        session.delete(cssList);
        session.getTransaction().commit();
    }

    private void clearCurrentFlag(Long siteId, Session session) {
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<CssBean> cssList = query.list();
        for (CssBean css : cssList) {
            if (css.isCurrent()) {
                css.setCurrent(false);
            }
        }
    }
}
