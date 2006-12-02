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