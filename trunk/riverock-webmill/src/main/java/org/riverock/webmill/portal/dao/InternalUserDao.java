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

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:56:54
 */
public interface InternalUserDao {
    public User getUser(Long portalUserId, AuthSession authSession);

    public List<User> getUserByEMail(String eMail);

    public List<User> getUserList(AuthSession authSession);

    public Long addUser(User portalUserBean);

    public void updateUser(User portalUserBean, AuthSession authSession);

    public void deleteUser(User portalUserBean, AuthSession authSession);

    /**
     * Get list of all users without restriction. Deleted users (isDeleted==true) not included in list.
     * @return list of all users in DB
     */
    public List<User> getUserList_notRestricted();

    /**
     * Get user without restriction. Deleted user (isDeleted==true) will be returned as null.
     * @param userId PK of user
     * @return user
     */
    public User getUser_notRestricted(Long userId);

    /**
     * update user without restriction.
     * @param user user for update
     */
    public void updateUser_notRestricted(User user);

}
