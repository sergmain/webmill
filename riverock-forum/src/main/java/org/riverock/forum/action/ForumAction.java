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

import org.riverock.forum.bean.ForumConcreteBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.ForumDAO;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumPortlet;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.util.Constants;
import org.riverock.module.exception.ActionException;

public class ForumAction implements Action {

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Integer f_id = forumActionBean.getRequest().getInt(Constants.NAME_FORUM_CONCRETE_ID);
        if (f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }
        int start = forumActionBean.getRequest().getInt("start", 0);
        String keyword = forumActionBean.getRequest().getString("keyword", "");
        keyword = keyword.trim();

        DAOFactory daof = DAOFactory.getDAOFactory();
        ForumDAO forumDAO = daof.getForumDAO();
        ForumConcreteBean forumConcreteBean = forumDAO.execute(
            f_id,
            ForumPortlet.getMessagesPerPage(forumActionBean.getConfig()),
            ForumPortlet.getTopicsPerPage(forumActionBean.getConfig()),
            start, keyword,
            forumActionBean.getForumId(),
            moduleActionRequest.getUrlProvider()
        );
        if (forumConcreteBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", forumConcreteBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.noSuchForumError(forumActionBean);
        }
    }
}