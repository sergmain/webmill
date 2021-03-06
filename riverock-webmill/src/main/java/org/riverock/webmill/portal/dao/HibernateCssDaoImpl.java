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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.sql.Blob;
import java.sql.SQLException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.common.exception.DatabaseException;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.portal.bean.CssBean;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 08.10.2006
 * Time: 17:49:19
 * <p/>
 * $Id$
 */
public class HibernateCssDaoImpl implements InternalCssDao {
    private final static Logger log = Logger.getLogger(HibernateCssDaoImpl.class);

    public Css getCssCurrent(Long siteId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCssCurrent() for siteId "+siteId);
        }
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery("select css from org.riverock.webmill.portal.bean.CssBean as css where css.isCurrent=true and css.siteId = :site_id");
            query.setLong("site_id", siteId);
            CssBean css = (CssBean)query.uniqueResult();

            if (css!=null) {
                Blob blob = css.getCssBlob();
                if (blob!=null) {
                    try {
                        css.setCss( new String(blob.getBytes(1, (int)blob.length()), StandardCharsets.UTF_8) );
                    }
                    catch (SQLException e) {
                        String es = "Error get CSS";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return css;
        }
        finally {
            session.close();
        }
    }

    public List<Css> getCssList(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery("select css from org.riverock.webmill.portal.bean.CssBean as css where css.siteId = :site_id");
            query.setLong("site_id", siteId);
            List<CssBean> cssList = query.list();
            for (CssBean css : cssList) {
                Blob blob = css.getCssBlob();
                if (blob!=null) {
                    try {
                        css.setCss( new String(blob.getBytes(1, (int)blob.length()), StandardCharsets.UTF_8) );
                    }
                    catch (SQLException e) {
                        String es = "Error get CSS";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    if (log.isDebugEnabled())  {
                        log.debug("Length of CSS is "+(css.getCss()!=null?css.getCss().length():0));
                    }
                }
            }
            return (List)cssList;
        }
        finally {
            session.close();
        }
    }

    public Css getCss(Long cssId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCss() for cssId "+cssId);
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery("select css from org.riverock.webmill.portal.bean.CssBean as css where css.cssId = :css_id");
            query.setLong("css_id", cssId);
            CssBean css = (CssBean)query.uniqueResult();
            if (css!=null) {
                Blob blob = css.getCssBlob();
                if (blob!=null) {
                    try {
                        css.setCss( new String(blob.getBytes(1, (int)blob.length()), StandardCharsets.UTF_8) );
                    }
                    catch (SQLException e) {
                        String es = "Error get CSS";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    if (log.isDebugEnabled())  {
                        log.debug("Length of CSS is "+(css.getCss()!=null?css.getCss().length():0));
                    }
                }
            }
            return css;
        }
        finally {
            session.close();
        }
    }

    public Long createCss(Css css) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            if (css.isCurrent()) {
                clearCurrentFlag(css.getSiteId(), session);
            }

            CssBean bean = new CssBean();
            bean.setCssComment(css.getCssComment());
            bean.setCurrent(css.isCurrent());
            bean.setDate(css.getDate());
            bean.setSiteId(css.getSiteId());
            if (StringUtils.isNotBlank(css.getCss())) {
                bean.setCssBlob( Hibernate.getLobCreator(session).createBlob(css.getCss().getBytes(StandardCharsets.UTF_8)));
            }
            else {
                bean.setCssBlob(null);
            }
            session.save(bean);
            session.flush();
            session.clear();

            session.getTransaction().commit();

            return bean.getCssId();
        }
        finally {
            session.close();
        }
    }

    public void updateCss(Css css) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            if (css.isCurrent()) {
                clearCurrentFlag(css.getSiteId(), session);
            }

            Query query = session.createQuery("select css from org.riverock.webmill.portal.bean.CssBean as css where css.cssId = :css_id");
            query.setLong("css_id", css.getCssId());
            CssBean bean = (CssBean)query.uniqueResult();
            if (bean!=null) {

                bean.setCssComment(css.getCssComment());
                bean.setCurrent(css.isCurrent());
                bean.setDate(css.getDate());
                bean.setSiteId(css.getSiteId());
                if (StringUtils.isNotBlank(css.getCss())) {
                    bean.setCssBlob( Hibernate.getLobCreator(session).createBlob(css.getCss().getBytes(StandardCharsets.UTF_8)));
                }
                else {
                    bean.setCssBlob(null);
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

    public void deleteCss(Long cssId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery
            ("delete org.riverock.webmill.portal.bean.CssBean css where css.cssId = :css_id and css.isCurrent=false ")
                .setLong("css_id", cssId)
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteCssForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery("delete org.riverock.webmill.portal.bean.CssBean css where css.siteId = :site_id")
                .setLong("site_id", siteId)
                .executeUpdate();
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private void clearCurrentFlag(Long siteId, Session session) {
        Query query = session.createQuery("select css from org.riverock.webmill.portal.bean.CssBean as css where css.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<CssBean> cssList = query.list();
        for (CssBean css : cssList) {
            if (css.isCurrent()) {
                css.setCurrent(false);
            }
        }
    }
}
