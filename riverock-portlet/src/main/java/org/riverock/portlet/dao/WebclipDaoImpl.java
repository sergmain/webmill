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

import java.nio.charset.StandardCharsets;
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
import org.hibernate.StatelessSession;

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
    private final static Logger log = Logger.getLogger( WebclipDaoImpl.class );

    public WebclipBean getWebclip(Long siteId, Long webclipId) {
        return getWebclip(siteId, webclipId, true);
    }

    /**
     *
     * @param siteId siteId
     * @param webclipId webcliId
     * @param isInitWebclipData is init data of webclip or not
     * @return webclip bean
     */
    public WebclipBean getWebclip(Long siteId, Long webclipId, boolean isInitWebclipData) {
        if (webclipId==null || siteId==null) {
            return null;
        }
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
            WebclipBean bean = (WebclipBean)session.createQuery(
                "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.webclipId = :webclipId and bean.siteId=:siteId")
                .setLong("webclipId", webclipId)
                .setLong("siteId", siteId)
                .uniqueResult();
            if (bean!=null) {
                if (isInitWebclipData) {
                    Blob blob = bean.getWebclipBlob();
                    if (blob!=null) {
                        try {
                            bean.setWebclipData( new String(blob.getBytes(1, (int)blob.length()), StandardCharsets.UTF_8) );
                        }
                        catch (SQLException e) {
                            String es = "Error get Webclip";
                            log.error(es, e);
                            throw new DatabaseException(es, e);
                        }
                    }
                }
            }

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
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
            List<Long> ids = session.createQuery(
                "select bean.webclipId from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();

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
            session.clear();
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
                    bytes = resultContent.getBytes(StandardCharsets.UTF_8);
                    Blob blob = Hibernate.getLobCreator(session).createBlob(bytes);
                    bean.setWebclipBlob(blob);
//                    bean.setWebclipBlob( Hibernate.getLobCreator(session).createBlob(bytes));
                }
                else {
                    bean.setWebclipBlob(null);
                }
                bean.setProcessContent(false);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void setOriginContent(WebclipBean webclip, byte[] bytes) {
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
                bean.setZipOriginContent( Hibernate.getLobCreator(session).createBlob(bytes) );
                bean.setLoadContent(false);
                bean.setProcessContent(true);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
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

            session.flush();
            session.clear();
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

            session.flush();
            session.clear();
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public WebclipStatisticBean getStatistic(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
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
            return stat;
        }
        finally {
            session.close();
        }
    }

    public long getTotalCount(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
            Long count = getTotalCount(siteId, session);
            return count;
        }
        finally {
            session.close();
        }
    }

    private long getTotalCount(Long siteId, StatelessSession session) {
        return (Long)session.createQuery(
            "select count(*) " +
                "from org.riverock.portlet.webclip.WebclipBean as bean " +
                "where bean.siteId=:siteId")
            .setLong("siteId", siteId)
            .uniqueResult();
    }

    public long getNotIndexedCount(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
            Long l = (Long)session.createQuery(
                "select count(*) " +
                    "from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.siteId=:siteId and isIndexed=false")
                .setLong("siteId", siteId)
                .uniqueResult();

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

            session.flush();
            session.clear();
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public WebclipBean getFirstForIndexing(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelSession();
        try {
            WebclipBean bean = (WebclipBean)session.createQuery(
                "select bean from org.riverock.portlet.webclip.WebclipBean as bean " +
                    "where bean.siteId=:siteId and bean.indexed=false")
                .setLong("siteId", siteId)
                .setFirstResult(1)
                .uniqueResult();
            if (bean!=null) {
                Blob blob = bean.getWebclipBlob();
                if (blob!=null) {
                    try {
                        bean.setWebclipData( new String(blob.getBytes(1, (int)blob.length()), StandardCharsets.UTF_8) );
                    }
                    catch (SQLException e) {
                        String es = "Error get Webclip";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return bean;
        }
        finally {
            session.close();
        }
    }
}

