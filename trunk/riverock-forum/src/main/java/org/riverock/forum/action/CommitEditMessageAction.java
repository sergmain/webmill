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

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.dao.CommitEditMessageDAO;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 15:05:52
 *         $Id$
 */
public class CommitEditMessageAction  implements Action {
    private final static Logger log = Logger.getLogger(PostPAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        ModuleUser auth_ = forumActionBean.getRequest().getUser();

        Long u_id = auth_.getId();
        int tm_iconid = forumActionBean.getRequest().getInt("tm_iconid", 0);

        Integer t_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_TOPIC_ID );
        Integer m_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_MESSAGE_ID );

        if (t_id==null || m_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        String content = forumActionBean.getRequest().getString("content");

        String u_lastip = forumActionBean.getRequest().getRemoteAddr();

        if (StringUtils.isEmpty(content)) {
            return ForumError.blankError(forumActionBean);
        }
        DAOFactory daof = DAOFactory.getDAOFactory();
        CommitEditMessageDAO commitEditMessageDAO = daof.getCommitEditMessageDAO();
        commitEditMessageDAO.commit( m_id, u_id, content, u_lastip, tm_iconid, forumActionBean.getForumId() );

        String urlString =
            moduleActionRequest.getUrlProvider().getUrl( Constants.WM_FORUM_PORTLET_NAME, Constants.TOPIC_ACTION ) + '&' +
            Constants.NAME_FORUM_ID + '=' + forumActionBean.getForumId() + '&' +
            Constants.NAME_FORUM_TOPIC_ID + '=' + t_id;

        if (log.isDebugEnabled()) {
            log.debug("redirecto to: " + urlString );
        }

        try {
            forumActionBean.getResponse().sendRedirect(urlString);
        }
        catch (IOException e) {
            String es = "Error sendRedirect to "+ urlString;
            log.error( es, e);
            throw new ActionException( es, e);
        }
        return null;
    }
}