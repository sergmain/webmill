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

import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.bean.MessageBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.EditMessageDAO;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;

public class EditMessageAction implements Action {
    private final static Logger log = Logger.getLogger(EditMessageAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Long topicId = forumActionBean.getRequest().getLong( Constants.NAME_FORUM_TOPIC_ID );
        Integer messageId = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_MESSAGE_ID );

        if (topicId==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }
        if (messageId==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        DAOFactory daof = DAOFactory.getDAOFactory();
        EditMessageDAO editMessageDAO = daof.getEditMessageDAO();

        MessageBean messageBean = null;
        messageBean = editMessageDAO.get( forumActionBean.getForumId(), topicId, messageId );
        messageBean.setForumId( forumActionBean.getForumId() );
        messageBean.setTopicId( topicId );
        StringBuilder urlString =
             moduleActionRequest.getUrlProvider().getUrlStringBuilder( Constants.WM_FORUM_PORTLET_NAME, Constants.POST_ACTION ).
            append(Constants.NAME_FORUM_ID).append('=').append(forumActionBean.getForumId());

        messageBean.setEditMessageUrl(
            urlString.toString()+'&'+
            Constants.NAME_FORUM_MESSAGE_ID+'='+messageBean.getMessageId()
        );

        if (log.isDebugEnabled()) {
            log.debug("messageBean: " + messageBean);
        }

        if (messageBean != null) {
            forumActionBean.getRequest().setAttribute("messageBean", messageBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.editMessageError(forumActionBean);
        }
    }

}