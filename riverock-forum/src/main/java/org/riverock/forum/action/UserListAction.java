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
package org.riverock.forum.action;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.bean.UserListBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.UserListDAO;
import org.riverock.module.exception.ActionException;

public class UserListAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        int start = forumActionBean.getRequest().getInt( "start", new Integer(0)).intValue();

        String keyword = forumActionBean.getRequest().getString("keyword");
        keyword = (keyword == null ? "" : keyword.trim());

//        int range = ForumPortletv2.getRecordsPerPage(forumActionBean.getConfig());
        int range = 30;

        DAOFactory daof = DAOFactory.getDAOFactory();
        UserListDAO userListDAO = daof.getUserListDAO();
        UserListBean userListBean = userListDAO.execute(start, range, keyword);
        if (userListBean != null) {
            forumActionBean.getRequest().setAttribute("userListBean", userListBean);
            return "/forum/jsp/userList.jsp";
        } else {
            return ForumError.userListError(forumActionBean);
        }
    }
}