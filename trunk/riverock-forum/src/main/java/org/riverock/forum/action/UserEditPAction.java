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

import java.io.IOException;

import org.apache.log4j.Logger;

import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.UserEditPDAO;
import org.riverock.module.exception.ActionException;

public class UserEditPAction implements Action {
    private final static Logger log = Logger.getLogger(UserEditPAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        int u_id = 0;//

        String u_password = forumActionBean.getRequest().getString("u_password");
        String u_email = forumActionBean.getRequest().getString("u_email");
        String u_address = forumActionBean.getRequest().getString("u_address");
        String u_sign = forumActionBean.getRequest().getString("u_sign");
        int u_avatar_id = 0;
//        int u_avatar_id = ForumStringUtils.parseInt(request.getParameter("u_avatar_id"));
        String u_lastip = forumActionBean.getRequest().getRemoteAddr();

        DAOFactory daof = DAOFactory.getDAOFactory();
        UserEditPDAO userEditDAO = daof.getUserEditPDAO();
        userEditDAO.execute(u_id, u_avatar_id, u_password, u_email, u_address, u_sign, u_lastip);
        if (u_id != 0) {
            final String url = "user.do?u_id=" + u_id;
            try {
                forumActionBean.getResponse().sendRedirect(url);
            }
            catch (IOException e) {
                String es = "Error sendRedirect to " + url;
                log.error(es, e);
                throw new ActionException(es, e);
            }
            return null;
        }
        else {
            return ForumError.userEditError(forumActionBean);
        }
    }
}