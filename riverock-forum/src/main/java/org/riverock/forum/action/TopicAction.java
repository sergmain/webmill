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