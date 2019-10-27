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

import org.apache.log4j.Logger;

import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.util.Constants;
import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.dao.AdminForumDAO;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.HomeDAO;

public class AdminForumAction implements ActionInstance {
    private final static Logger log = Logger.getLogger(AdminForumAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        log.debug("in execute()");
        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;

        ModuleUser auth_ = forumActionBean.getRequest().getUser();

        DAOFactory daof = DAOFactory.getDAOFactory();
        AdminForumDAO adminForumDAO = daof.getAdminForumDAO();
        adminForumDAO.execute( auth_, forumActionBean, moduleActionRequest.getUrlProvider(), forumActionBean.getForumId() );

        HomeDAO homeDAO = daof.getHomeDAO();
        ForumBean homeBean = homeDAO.execute(
            moduleActionRequest.getUrlProvider(), forumActionBean.getForumId(), false
        );

        log.debug("before leave execute()");

        if (homeBean != null) {
            forumActionBean.getRequest().setAttribute("forumBean", homeBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.homeNoForumsError(forumActionBean);
        }
    }

}