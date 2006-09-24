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