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

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:56:54
 */
public interface InternalUserDao {
    public User getUser(Long portalUserId, AuthSession authSession);

    public User getUserByEMail(DatabaseAdapter db_, String eMail);

    public List<User> getUserList(AuthSession authSession);

    public Long addUser(User portalUserBean);
    public Long addUser(DatabaseAdapter db_, User portalUserBean);

    public void updateUser(User portalUserBean, AuthSession authSession);

    public void deleteUser(User portalUserBean, AuthSession authSession);
}
