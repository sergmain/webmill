/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
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
 *
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