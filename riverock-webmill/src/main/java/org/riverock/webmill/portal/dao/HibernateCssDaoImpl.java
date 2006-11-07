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

import java.util.List;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.utils.HibernateUtils;

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
        if (log.isDebugEnabled()) {
            log.debug("Start getCssCurrent() for siteId "+siteId);
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.isCurrent=true and css.siteId = :site_id");
        query.setLong("site_id", siteId);
        CssBean css = (CssBean)query.uniqueResult();

        if (css!=null) {
            Blob blob = css.getCssBlob();
            if (blob!=null) {
                try {
                    css.setCss( new String(blob.getBytes(1, (int)blob.length())) );
                }
                catch (SQLException e) {
                    String es = "Error get CSS";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
        }
        session.getTransaction().commit();
        return css;
    }

    public List<Css> getCssList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<CssBean> cssList = query.list();
        for (CssBean css : cssList) {
            Blob blob = css.getCssBlob();
            if (blob!=null) {
                try {
                    css.setCss( new String(blob.getBytes(1, (int)blob.length())) );
                }
                catch (SQLException e) {
                    String es = "Error get CSS";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
                if (log.isDebugEnabled())  {
                    log.debug("Length of CSS is "+css.getCss()!=null?css.getCss().length():0);
                }
            }
        }
        session.getTransaction().commit();
        return (List)cssList;
    }

    public Css getCss(Long cssId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCss() for cssId "+cssId);
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select css from org.riverock.webmill.main.CssBean as css where css.cssId = :css_id");
        query.setLong("css_id", cssId);
        CssBean css = (CssBean)query.uniqueResult();
        if (css!=null) {
        Blob blob = css.getCssBlob();
            if (blob!=null) {
                try {
                    css.setCss( new String(blob.getBytes(1, (int)blob.length())) );
                }
                catch (SQLException e) {
                    String es = "Error get CSS";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
                if (log.isDebugEnabled())  {
                    log.debug("Length of CSS is "+css.getCss()!=null?css.getCss().length():0);
                }
            }
        }
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
        if (StringUtils.isNotBlank(css.getCss())) {
            bean.setCssBlob( Hibernate.createBlob(css.getCss().getBytes()));
        }
        session.save(bean);
        session.flush();

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
        if (bean!=null) {
            
            bean.setCssComment(css.getCssComment());
            bean.setCurrent(css.isCurrent());
            bean.setDate(css.getDate());
            bean.setSiteId(css.getSiteId());
            if (StringUtils.isNotBlank(css.getCss())) {
                bean.setCssBlob( Hibernate.createBlob(css.getCss().getBytes()));
            }
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
