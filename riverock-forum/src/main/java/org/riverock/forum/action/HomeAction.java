/*
 * org.riverock.forum - Forum portlet
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
package org.riverock.forum.action;

import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.util.Constants;
import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.HomeDAO;

public class HomeAction implements ActionInstance {

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        DAOFactory daof = DAOFactory.getDAOFactory();

        HomeDAO homeDAO = daof.getHomeDAO();
        ForumBean homeBean = homeDAO.execute(
            moduleActionRequest.getUrlProvider(), forumActionBean.getForumId(), true
        );
        if (homeBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", homeBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(forumActionBean);
        }
    }
}