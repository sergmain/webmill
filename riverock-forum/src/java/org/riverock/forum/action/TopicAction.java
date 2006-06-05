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
import org.riverock.module.exception.ActionException;
import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.ForumPortlet;
import org.riverock.forum.util.Constants;
import org.riverock.forum.bean.TopicBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.TopicDAO;

public class TopicAction implements Action {
    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Long t_id = forumActionBean.getRequest().getLong( Constants.NAME_FORUM_TOPIC_ID);
        if (t_id==null) {
            return ForumError.noSuchTopicError(forumActionBean);
        }
        int start = forumActionBean.getRequest().getInt( "start", new Integer(0)).intValue();

        DAOFactory daof = DAOFactory.getDAOFactory();
        TopicDAO topicDAO = daof.getTopicDAO();
        TopicBean topicBean = topicDAO.execute(
            t_id.intValue(), start,
            ForumPortlet.getMessagesPerPage(forumActionBean.getConfig()),
            moduleActionRequest.getUrlProvider(),
            forumActionBean.getForumId(),
            moduleActionRequest.getRequest().getUser()
        );
        if (topicBean != null) {
            forumActionBean.getRequest().setAttribute("topicBean", topicBean);
            return Constants.OK_EXECUTE_STATUS;
        }
        else {
            return ForumError.noSuchTopicError(forumActionBean);
        }
    }
}