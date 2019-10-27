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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.dao.CommitEditMessageDAO;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.util.Constants;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 15:05:52
 *         $Id: CommitEditMessageAction.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class CommitEditMessageAction  implements ActionInstance {
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