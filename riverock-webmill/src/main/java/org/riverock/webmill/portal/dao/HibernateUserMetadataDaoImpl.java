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

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.webmill.portal.bean.UserMetadataItemBean;
import org.riverock.webmill.portal.bean.UserBean;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 14:52:21
 *         <p/>
 *         $Id$
 */
public class HibernateUserMetadataDaoImpl implements InternalUserMetadataDao {
    private final static Logger log = Logger.getLogger(HibernateUserMetadataDaoImpl.class);

    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {

            UserMetadataItemBean bean = (UserMetadataItemBean)session.createQuery(
                "select meta " +
                    "from  org.riverock.webmill.portal.bean.UserMetadataItemBean as meta," +
                    "      org.riverock.webmill.portal.bean.AuthInfoImpl auth " +
                    "where auth.userLogin=:userLogin and meta.metadataName=:metadataName and meta.siteId=:siteId and meta.userId=auth.userId")
                .setString("userLogin", userLogin)
                .setString("metadataName", metadataName)
                .setLong("siteId", siteId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UserBean user = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user, " +
                "       org.riverock.webmill.portal.bean.AuthInfoImpl auth " +
                "where  user.userId = auth.userId and auth.userLogin=:userLogin ")
                .setString("userLogin", userLogin)
                .uniqueResult();

            if (user==null) {
                if (log.isDebugEnabled()) {
                    log.info("Login not exist: "+userLogin);
                }
                return;
            }

            UserMetadataItemBean bean = (UserMetadataItemBean)session.createQuery(
                "select meta from org.riverock.webmill.portal.bean.UserMetadataItemBean as meta " +
                "where  meta.metadataName=:metadataName and meta.siteId=:siteId and meta.userId=:userId")
                .setString("metadataName", metadataName)
                .setLong("siteId", siteId)
                .setLong("userId", user.getUserId())
                .uniqueResult();

            if (bean==null) {
                bean = new UserMetadataItemBean();
                bean.setSiteId(siteId);
                bean.setMetadataName(metadataName);
                bean.setIntValue(intValue);
                bean.setUserId(user.getUserId());
            }
            else {
                bean.setIntValue(intValue);
                bean.setStringValue(null);
                bean.setDateValue(null);
            }
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UserBean user = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user, " +
                "       org.riverock.webmill.portal.bean.AuthInfoImpl auth " +
                "where  user.userId = auth.userId and auth.userLogin=:userLogin ")
                .setString("userLogin", userLogin)
                .uniqueResult();

            if (user==null) {
                if (log.isDebugEnabled()) {
                    log.info("Login not exist: "+userLogin);
                }
                return;
            }

            UserMetadataItemBean bean = (UserMetadataItemBean)session.createQuery(
                "select meta from org.riverock.webmill.portal.bean.UserMetadataItemBean as meta " +
                "where  meta.metadataName=:metadataName and meta.siteId=:siteId and meta.userId=:userId")
                .setString("metadataName", metadataName)
                .setLong("siteId", siteId)
                .setLong("userId", user.getUserId())
                .uniqueResult();

            if (bean==null) {
                bean = new UserMetadataItemBean();
                bean.setSiteId(siteId);
                bean.setMetadataName(metadataName);
                bean.setStringValue(stringValue);
                bean.setUserId(user.getUserId());
            }
            else {
                bean.setStringValue(stringValue);
                bean.setIntValue(null);
                bean.setDateValue(null);
            }
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UserBean user = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user, " +
                "       org.riverock.webmill.portal.bean.AuthInfoImpl auth " +
                "where  user.userId = auth.userId and auth.userLogin=:userLogin ")
                .setString("userLogin", userLogin)
                .uniqueResult();

            if (user==null) {
                if (log.isDebugEnabled()) {
                    log.info("Login not exist: "+userLogin);
                }
                return;
            }

            UserMetadataItemBean bean = (UserMetadataItemBean)session.createQuery(
                "select meta from org.riverock.webmill.portal.bean.UserMetadataItemBean as meta " +
                "where  meta.metadataName=:metadataName and meta.siteId=:siteId and meta.userId=:userId")
                .setString("metadataName", metadataName)
                .setLong("siteId", siteId)
                .setLong("userId", user.getUserId())
                .uniqueResult();

            if (bean==null) {
                bean = new UserMetadataItemBean();
                bean.setSiteId(siteId);
                bean.setMetadataName(metadataName);
                bean.setDateValue(dateValue);
                bean.setUserId(user.getUserId());
            }
            else {
                bean.setDateValue(dateValue);
                bean.setIntValue(null);
                bean.setStringValue(null);
            }
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
