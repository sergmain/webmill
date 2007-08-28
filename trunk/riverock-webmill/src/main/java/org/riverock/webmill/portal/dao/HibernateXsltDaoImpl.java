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

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.common.exception.DatabaseException;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 07.11.2006
 *         Time: 17:45:44
 *         <p/>
 *         $Id$
 */
public class HibernateXsltDaoImpl implements InternalXsltDao {
    private final static Logger log = Logger.getLogger(HibernateXsltDaoImpl.class);

    /**
     * key is language of site
     *
     * @param siteId Long
     * @return Map<String, Xslt>
     */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select customLanguage, xslt from " +
                    "      org.riverock.webmill.portal.bean.PortalXsltBean as xslt," +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where xslt.isCurrent=true and xslt.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage=:site_id")
                .setLong("site_id", siteId);

            Map<String, Xslt> map = new HashMap<String, Xslt>();
            for (Object o : query.list()) {
                Object[] tuple = (Object[]) o;
                String language = StringTools.getLocale((String)tuple[0]).toString();
                PortalXsltBean xslt = (PortalXsltBean)tuple[1];
                Blob blob = xslt.getXsltBlob();
                if (blob!=null) {
                    try {
                        xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    if (log.isDebugEnabled())  {
                        log.debug("Length of XSLT is "+xslt.getXsltData()!=null?xslt.getXsltData().length():0);
                    }
                }
                map.put(language, xslt);
            }
            return map;
        }
        finally {
            session.close();
        }
    }

    /**
     * key is language of site
     *
     * @ param siteId Long
     * @return Map<String, Xslt>
     */
/*
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                "where xslt.isCurrent=true and xslt.siteLanguageId=:site_language_id");

            List<SiteLanguage> siteLanguages = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);
            Map<String, Xslt> map = new HashMap<String, Xslt>();
            for (SiteLanguage siteLanguage : siteLanguages) {
                query.setLong("site_language_id", siteLanguage.getSiteLanguageId());
                List<PortalXsltBean> xsltBeans = (List<PortalXsltBean>)query.list();
                for (PortalXsltBean xslt : xsltBeans) {
                    Blob blob = xslt.getXsltBlob();
                    if (blob!=null) {
                        try {
                            xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                        }
                        catch (UnsupportedEncodingException e) {
                            String es = "Error get XSLT";
                            log.error(es, e);
                            throw new DatabaseException(es, e);
                        }
                        catch (SQLException e) {
                            String es = "Error get XSLT";
                            log.error(es, e);
                            throw new DatabaseException(es, e);
                        }
                        if (log.isDebugEnabled())  {
                            log.debug("Length of XSLT is "+xslt.getXsltData()!=null?xslt.getXsltData().length():0);
                        }
                    }
                    String lang = StringTools.getLocale(siteLanguage.getCustomLanguage()).toString();
                    map.put(lang, xslt);
                }
            }
            return map;
        }
        finally {
            session.close();
        }
    }
*/

    public Xslt getCurrentXslt(Long siteLanguageId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getCurrentXslt() for siteLanguageId "+siteLanguageId);
        }
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                    "where xslt.isCurrent=true and xslt.siteLanguageId = :site_language_id ");
            query.setLong("site_language_id", siteLanguageId);
            PortalXsltBean xslt = (PortalXsltBean)query.uniqueResult();

            if (xslt!=null) {
                Blob blob = xslt.getXsltBlob();
                if (blob!=null) {
                    try {
                        xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (SQLException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return xslt;
        }
        finally {
            session.close();
        }
    }

    public Xslt getXslt(Long xsltId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getXslt() for xsltId "+xsltId);
        }
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                    "where xslt.id=:ID_SITE_XSLT"
            );
            query.setLong("ID_SITE_XSLT", xsltId);
            PortalXsltBean xslt = (PortalXsltBean)query.uniqueResult();

            if (xslt!=null) {
                Blob blob = xslt.getXsltBlob();
                if (blob!=null) {
                    try {
                        xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return xslt;
        }
        finally {
            session.close();
        }
    }

    public Xslt getXslt(String xsltName, Long siteLanguageId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getXslt() for xsltName "+xsltName+" and siteLanguageId " +siteLanguageId);
        }
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt where xslt.name=:xslt_name and xslt.siteLanguageId=:site_language_id "
            );
            query.setString("xslt_name", xsltName);
            query.setLong("site_language_id", siteLanguageId);
            PortalXsltBean xslt = (PortalXsltBean)query.uniqueResult();

            if (xslt!=null) {
                Blob blob = xslt.getXsltBlob();
                if (blob!=null) {
                    try {
                        xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return xslt;
        }
        finally {
            session.close();
        }
    }

    public Long createXslt(Xslt xslt) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            if (xslt.isCurrent()) {
                clearCurrentFlag(xslt.getSiteLanguageId(), session);
            }

            PortalXsltBean bean = new PortalXsltBean();
            bean.setName(xslt.getName());
            bean.setCurrent(xslt.isCurrent());
            bean.setSiteLanguageId(xslt.getSiteLanguageId());
            if (StringUtils.isNotBlank(xslt.getXsltData())) {
                try {
                    bean.setXsltBlob( Hibernate.createBlob(xslt.getXsltData().getBytes(CharEncoding.UTF_8)));
                }
                catch (UnsupportedEncodingException e) {
                    String es = "Error create XSLT";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
            else {
                bean.setXsltBlob(null);
            }
            session.save(bean);
            
            session.flush();
            session.clear();
            session.getTransaction().commit();

            return bean.getId();
        }
        finally {
            session.close();
        }
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                "where xslt.siteLanguageId = :site_language_id");
            query.setLong("site_language_id", siteLanguageId);
            List<PortalXsltBean> xsltBeans = query.list();
            for (PortalXsltBean xslt : xsltBeans) {
                Blob blob = xslt.getXsltBlob();
                if (blob!=null) {
                    try {
                        xslt.setXsltData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    if (log.isDebugEnabled())  {
                        log.debug("Length of XSLT is "+xslt.getXsltData()!=null?xslt.getXsltData().length():0);
                    }
                }
            }
            return (List)xsltBeans;
        }
        finally {
            session.close();
        }
    }

    public void deleteXsltForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                "delete org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                    "where xslt.siteLanguageId in (" +
                    "select siteLabguage.siteLanguageId " +
                    "from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                    "where siteLanguage.siteId=:siteId)");

            query.setLong("siteId", siteId).executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

/*
    public void deleteXsltForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                "delete org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                    "where xslt.siteLanguageId = :site_language_id");

            List<SiteLanguage> list = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);
            for (SiteLanguage siteLanguage : list) {
                query.setLong("site_language_id", siteLanguage.getSiteLanguageId())
                    .executeUpdate();
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
*/

    public void deleteXsltForSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "delete org.riverock.webmill.portal.bean.PortalXsltBean xslt " +
                    "where xslt.siteLanguageId = :site_language_id")
                .setLong("site_language_id", siteLanguageId)
                .executeUpdate();
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void updateXslt(Xslt xslt) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            if (xslt.isCurrent()) {
                clearCurrentFlag(xslt.getSiteLanguageId(), session);
            }

            Query query = session.createQuery(
                "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                    "where xslt.id = :xslt_id");
            query.setLong("xslt_id", xslt.getId());
            PortalXsltBean bean = (PortalXsltBean)query.uniqueResult();
            if (bean!=null) {
                bean.setName(xslt.getName());
                bean.setCurrent(xslt.isCurrent());
                bean.setSiteLanguageId(xslt.getSiteLanguageId());
                if (StringUtils.isNotBlank(xslt.getXsltData())) {
                    try {
                        bean.setXsltBlob( Hibernate.createBlob(xslt.getXsltData().getBytes(CharEncoding.UTF_8)));
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error update XSLT";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
                else {
                    bean.setXsltBlob(null);
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

    public void deleteXslt(Long xsltId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.PortalXsltBean as xslt where xslt.id=:xslt_id and xslt.isCurrent=false ")
                .setLong("xslt_id", xsltId)
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private void clearCurrentFlag(Long siteLanguageId, Session session) {
        Query query = session.createQuery("select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
            "where xslt.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguageId);
        List<PortalXsltBean> cssList = query.list();
        for (PortalXsltBean xslt : cssList) {
            if (xslt.isCurrent()) {
                xslt.setCurrent(false);
            }
        }
    }
}
