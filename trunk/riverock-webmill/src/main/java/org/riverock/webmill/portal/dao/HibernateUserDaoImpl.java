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

import org.hibernate.Session;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.bean.UserBean;
import org.riverock.webmill.utils.HibernateUtils;

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

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserBean bean = (UserBean)session.createQuery(
            "select user from org.riverock.webmill.portal.bean.UserBean as user " +
            "where  user.userId=:userId and user.companyId in (:companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
            .setLong("userId", portalUserId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public User getUserByEMail(String eMail) {
        if (eMail == null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserBean bean = (UserBean)session.createQuery(
            "select user from org.riverock.webmill.portal.bean.UserBean as user " +
            "where  user.email=:email")
            .setString("email", eMail)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<User> getUserList(AuthSession authSession) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List list = session.createQuery(
            "select user from org.riverock.webmill.portal.bean.UserBean as user " +
            "where  user.isDeleted=false and user.companyId in (:companyIds)")
            .setParameterList("companyIds", authSession.getGrantedCompanyIdList())
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long addUser(User portalUserBean) {
        if (portalUserBean == null) {
            throw new IllegalStateException("portalUserBean is null");
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserBean bean = new UserBean(portalUserBean);
        session.save(bean);

        session.flush();
        session.getTransaction().commit();
        return bean.getUserId();
    }

    public void updateUser(User portalUserBean, AuthSession authSession) {
        if (authSession==null || portalUserBean==null || portalUserBean.getUserId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
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

        session.getTransaction().commit();
    }

    public void deleteUser(User portalUserBean, AuthSession authSession) {

        if (authSession==null || portalUserBean==null || portalUserBean.getUserId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
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
        bean.setDeleted(true);

        session.getTransaction().commit();
    }

    /**
     * Get list of all users without restriction. Deleted users (isDeleted==true) not included in list.
     * @return list of all users in DB
     */
    public List<User> getUserList_notRestricted() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List list = session.createQuery(
            "select user from org.riverock.webmill.portal.bean.UserBean as user " +
            "where  user.isDeleted=false")
            .list();
        session.getTransaction().commit();
        return list;
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

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserBean bean = (UserBean)session.createQuery(
            "select user from org.riverock.webmill.portal.bean.UserBean as user " +
            "where  user.userId=:userId ")
            .setLong("userId", userId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
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

        session.getTransaction().commit();
    }
}
