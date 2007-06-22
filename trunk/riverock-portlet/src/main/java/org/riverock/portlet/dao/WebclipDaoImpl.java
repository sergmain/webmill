/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.dao;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import org.riverock.common.exception.DatabaseException;
import org.riverock.portlet.tools.HibernateUtils;
import org.riverock.portlet.webclip.WebclipBean;
import org.riverock.portlet.webclip.manager.bean.WebclipStatisticBean;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 17:31:51
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class WebclipDaoImpl implements WebclipDao {
    private static Logger log = Logger.getLogger( WebclipDaoImpl.class );

    public WebclipBean getWebclip(Long siteId, Long webclipId) {
        return getWebclip(siteId, webclipId, true);
    }

    public WebclipBean getWebclip(Long siteId, Long webclipId, boolean isInitWebclipData) {
        if (webclipId==null || siteId==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            WebclipBean bean = (WebclipBean)session.createQuery(
                "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.webclipId = :webclipId and bean.siteId=:siteId")
                .setLong("webclipId", webclipId)
                .setLong("siteId", siteId)
                .uniqueResult();
            if (bean!=null) {
                if (isInitWebclipData) {
/*
                    if (bean.getWebclipData()==null) {
                        System.out.println("WebclipData is null");
                    }
*/
                    Blob blob = bean.getWebclipBlob();
                    if (blob!=null) {
                        try {
                            bean.setWebclipData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                        }
                        catch (UnsupportedEncodingException e) {
                            String es = "Error get Webclip";
                            log.error(es, e);
                            throw new DatabaseException(es, e);
                        }
                        catch (SQLException e) {
                            String es = "Error get Webclip";
                            log.error(es, e);
                            throw new DatabaseException(es, e);
                        }
                    }
                }
            }
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public List<Long> getAllForSite(Long siteId) {

        if (siteId==null) {
            return new ArrayList<Long>(0);
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            List<Long> ids = session.createQuery(
                "select bean.webclipId from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            session.getTransaction().commit();
            return ids;
        }
        finally {
            session.close();
        }
    }

    public Long createWebclip(Long siteId) {

        Session session = HibernateUtils.getSession();

        try {
            session.beginTransaction();

            WebclipBean bean = new WebclipBean();
            bean.setSiteId(siteId);
            bean.setWebclipBlob(null);
            bean.setLoadContent(false);
            bean.setProcessContent(false);

            session.save(bean);
            session.flush();

            session.getTransaction().commit();

            return bean.getWebclipId();
        }
        finally {
            session.close();
        }
    }

    public void updateWebclip(WebclipBean webclip, String resultContent) {
        Session session = HibernateUtils.getSession();

        try {
            session.beginTransaction();

            WebclipBean bean = (WebclipBean)session.createQuery(
                "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.webclipId = :webclipId and bean.siteId=:siteId")
                .setLong("webclipId", webclip.getWebclipId())
                .setLong("siteId", webclip.getSiteId())
                .uniqueResult();
            if (bean!=null) {
                if (StringUtils.isNotBlank(resultContent)) {
                    byte[] bytes;
                    try {
                        bytes = resultContent.getBytes(CharEncoding.UTF_8);
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error convert webclip data to array of bytes";
                        log.error(es, e);
                        throw new RuntimeException(es, e);
                    }
                    bean.setWebclipBlob( Hibernate.createBlob(bytes));
                }
                else {
                    bean.setWebclipBlob(null);
                }
                bean.setProcessContent(false);
            }
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void setOriginContent(WebclipBean webclip, byte[] bytes) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        WebclipBean bean = (WebclipBean)session.createQuery(
            "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                "where bean.webclipId = :webclipId and bean.siteId=:siteId")
            .setLong("webclipId", webclip.getWebclipId())
            .setLong("siteId", webclip.getSiteId())
            .uniqueResult();
        if (bean!=null) {
            bean.setZipOriginContent( Hibernate.createBlob(bytes) );
            bean.setLoadContent(false);
            bean.setProcessContent(true);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void deleteWebclip(Long siteId, Long weblipId) {
        if (weblipId==null || siteId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();

        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.webclipId = :webclipId and bean.siteId=:siteId")
                .setLong("webclipId", weblipId)
                .setLong("siteId", siteId)
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void markAllForReload(Long siteId) {
        Session session = HibernateUtils.getSession();

        try {
            session.beginTransaction();
            session.createQuery(
            "update org.riverock.portlet.webclip.WebclipBean as bean set bean.isLoadContent=1 " +
                    "where bean.siteId=:siteId")
                .setLong("siteId", siteId)
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void markAllForProcess(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "update org.riverock.portlet.webclip.WebclipBean as bean set bean.isProcessContent=1 " +
                    "where bean.siteId=:siteId ")
                .setLong("siteId", siteId)
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public WebclipStatisticBean getStatistic(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            WebclipStatisticBean stat = new WebclipStatisticBean();
            stat.setTotalCount( getTotalCount(siteId, session) ); 
            stat.setForReloadCount(
                (Long)session.createQuery(
                    "select count(*) " +
                        "from org.riverock.portlet.webclip.WebclipBean as bean " +
                        "where bean.siteId=:siteId and bean.isLoadContent=1 ")
                    .setLong("siteId", siteId)
                    .uniqueResult()
            );
            stat.setForProcessCount(
                (Long)session.createQuery(
                    "select count(*) " +
                        "from org.riverock.portlet.webclip.WebclipBean as bean " +
                        "where bean.siteId=:siteId and bean.isProcessContent=1 ")
                    .setLong("siteId", siteId)
                    .uniqueResult()
            );
            session.getTransaction().commit();
            return stat;
        }
        finally {
            session.close();
        }
    }

    public long getTotalCount(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Long count = getTotalCount(siteId, session);
            session.getTransaction().commit();
            return count;
        }
        finally {
            session.close();
        }
    }

    private long getTotalCount(Long siteId, Session session) {
        return (Long)session.createQuery(
            "select count(*) " +
                "from org.riverock.portlet.webclip.WebclipBean as bean " +
                "where bean.siteId=:siteId")
            .setLong("siteId", siteId)
            .uniqueResult();
    }

    public long getNotIndexedCount(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Long l = (Long)session.createQuery(
                "select count(*) " +
                    "from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.siteId=:siteId and isIndexed=false")
                .setLong("siteId", siteId)
                .uniqueResult();

            session.getTransaction().commit();
            return l;
        }
        finally {
            session.close();
        }
    }

    public void markAsIndexed(Long siteId, Long webclipId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "update org.riverock.portlet.webclip.WebclipBean as bean set bean.isIndexed=true " +
                    "where bean.siteId=:siteId and bean.webclipId=:webclipId ")
                .setLong("siteId", siteId)
                .setLong("webclipId", webclipId)
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void markAllForIndexing(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            session.createQuery(
            "update org.riverock.portlet.webclip.WebclipBean as bean set bean.isIndexed=false " +
                    "where bean.siteId=:siteId  ")
                .setLong("siteId", siteId)
                .executeUpdate();

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public WebclipBean getFirstForIndexing(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            WebclipBean bean = (WebclipBean)session.createQuery(
                "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.webclipId = :webclipId and bean.siteId=:siteId")
                .setLong("siteId", siteId)
                .setFirstResult(1)
                .uniqueResult();
            if (bean!=null) {
                Blob blob = bean.getWebclipBlob();
                if (blob!=null) {
                    try {
                        bean.setWebclipData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get Webclip";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get Webclip";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }
}

