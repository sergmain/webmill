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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.UserBean;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 15:54:36
 *         <p/>
 *         $Id$
 */
public class HibernateUserDaoImpl implements InternalUserDao {

    public User getUser(Long portalUserId, AuthSession authSession) {
        if (portalUserId == null || authSession==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            UserBean bean = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.userId=:userId and user.companyId in (:companyIds)")
                .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
                .setLong("userId", portalUserId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    /**
     * deleted user have not an e-mail
     * 
     * @param eMail
     * @return
     */
    public List<User> getUserByEMail(String eMail) {
        if (StringUtils.isBlank(eMail)) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<UserBean> bean = session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  lower(user.email)=:email")
                .setString("email", eMail.toLowerCase())
                .list();

            return (List)bean;
        }
        finally {
            session.close();
        }
    }

    public List<User> getUserList(AuthSession authSession) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List list = session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.isDeleted=false and user.companyId in (:companyIds)")
                .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
                .list();

            return list;
        }
        finally {
            session.close();
        }
    }

    public Long addUser(User portalUserBean) {
        if (portalUserBean == null) {
            throw new IllegalStateException("portalUserBean is null");
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            UserBean bean = new UserBean(portalUserBean);
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
            return bean.getUserId();
        }
        finally {
            session.close();
        }
    }

    public void updateUser(User portalUserBean, AuthSession authSession) {
        if (authSession==null || portalUserBean==null || portalUserBean.getUserId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UserBean bean = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.userId=:userId and user.isDeleted=false and user.companyId in (:companyIds)")
                .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
                .setLong("userId", portalUserBean.getUserId())
                .uniqueResult();

            if (bean==null) {
                session.getTransaction().commit();
                return;
            }
            bean.setFirstName(portalUserBean.getFirstName());
            bean.setMiddleName(portalUserBean.getMiddleName());
            bean.setLastName(portalUserBean.getLastName());
            bean.setAddress(portalUserBean.getAddress());
            bean.setPhone(portalUserBean.getPhone());
            bean.setEmail(portalUserBean.getEmail());

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteUser(User portalUserBean, AuthSession authSession) {

        if (authSession==null || portalUserBean==null || portalUserBean.getUserId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            UserBean bean = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.userId=:userId and user.companyId in (:companyIds)")
                .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
                .setLong("userId", portalUserBean.getUserId())
                .uniqueResult();

            if (bean==null) {
                return;
            }

            InternalDaoFactory.getInternalAuthDao().deleteAuthInfo(session, bean.getUserId());
            
            bean.setEmail(null);
            bean.setDeleted(true);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    /**
     * Get list of all users without restriction. Deleted users (isDeleted==true) not included in list.
     * @return list of all users in DB
     */
    public List<User> getUserList_notRestricted() {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List list = session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.isDeleted=false")
                .list();

            return list;
        }
        finally {
            session.close();
        }
    }

    /**
     * Get user without restriction. Deleted user (isDeleted==true) will be returned as null.
     *
     * @param userId PK of user
     * @return user
     */
    public User getUser_notRestricted(Long userId) {
        if (userId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            UserBean bean = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.userId=:userId ")
                .setLong("userId", userId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    /**
     * update user without restriction.
     *
     * @param user user for update
     */
    public void updateUser_notRestricted(User user) {
        if (user==null || user.getUserId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UserBean bean = (UserBean)session.createQuery(
                "select user from org.riverock.webmill.portal.bean.UserBean as user " +
                "where  user.userId=:userId and user.isDeleted=false")
                .setLong("userId", user.getUserId())
                .uniqueResult();

            if (bean==null) {
                session.getTransaction().commit();
                return;
            }
            bean.setFirstName(user.getFirstName());
            bean.setMiddleName(user.getMiddleName());
            bean.setLastName(user.getLastName());
            bean.setAddress(user.getAddress());
            bean.setPhone(user.getPhone());
            bean.setEmail(user.getEmail());

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
