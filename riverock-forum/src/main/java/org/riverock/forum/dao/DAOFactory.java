/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.forum.dao;

public class DAOFactory {

    public static DAOFactory getDAOFactory() {
        return new DAOFactory();
    }

    public HomeDAO getHomeDAO() {
        return new HomeDAO();
    }

    public ForumDAO getForumDAO() {
        return new ForumDAO();
    }

    public TopicDAO getTopicDAO() {
        return new TopicDAO();
    }

    public PostDAO getPostDAO() {
        return new PostDAO();
    }

    public PostPDAO getPostPDAO() {
        return new PostPDAO();
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    public UserEditPDAO getUserEditPDAO() {
        return new UserEditPDAO();
    }

    public UserListDAO getUserListDAO() {
        return new UserListDAO();
    }

    public AdminForumDAO getAdminForumDAO() {
        return new AdminForumDAO();
    }

    public ForumListManagerDAO getForumListManagerDAO() {
        return new ForumListManagerDAO();
    }

    public EditMessageDAO getEditMessageDAO() {
        return new EditMessageDAO();
    }

    public CommitEditMessageDAO getCommitEditMessageDAO() {
        return new CommitEditMessageDAO();
    }

    public TopLevelForumDAO getTopLevelForumDAO() {
        return new TopLevelForumDAO();
    }

    public ForumIntegrityDao getForumIntegrityDao() {
        return new ForumIntegrityDao();
    }
}