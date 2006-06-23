/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.dao.PortalUserDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:19:26
 */
@SuppressWarnings({"UnusedAssignment"})
public class PortalUserDaoImpl implements PortalUserDao {
    private AuthSession authSession = null;

    PortalUserDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public List<User> getUserList() {
        return InternalDaoFactory.getInternalUserDao().getUserList(authSession);
    }

    public Long addUser(User user) {
        return InternalDaoFactory.getInternalUserDao().addUser(user);
    }

    public void updateUser(User user) {
        InternalDaoFactory.getInternalUserDao().updateUser(user, authSession);
    }

    public void deleteUser(User user) {
        InternalDaoFactory.getInternalUserDao().deleteUser(user, authSession);
    }

    public User getUser(Long userId) {
        return InternalDaoFactory.getInternalUserDao().getUser(userId, authSession);
    }
}
