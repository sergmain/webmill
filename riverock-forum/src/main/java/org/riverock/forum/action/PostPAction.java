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
import java.util.Map;

import javax.portlet.ActionRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.forum.ForumActionBean;
import org.riverock.forum.ForumError;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.PostPDAO;
import org.riverock.forum.util.Constants;
import org.riverock.forum.util.ForumStringUtils;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.user.ModuleUser;

public class PostPAction implements ActionInstance {
    private final static Logger log = Logger.getLogger(PostPAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        ModuleUser auth_ = forumActionBean.getRequest().getUser();
        if (auth_==null) {
            return ForumError.notLoggedError(forumActionBean);
        }

        Long userId = auth_.getId();
        boolean reply = ForumStringUtils.parseBoolean(forumActionBean.getRequest().getString(Constants.NAME_FORUM_REPLY));
        int tm_iconid = forumActionBean.getRequest().getInt( "tm_iconid", 0);

        Integer f_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_CONCRETE_ID );
        Integer t_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_TOPIC_ID );
        if (reply && t_id==null ) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        if (!reply && f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        String subject = forumActionBean.getRequest().getString("subject");
        String content = forumActionBean.getRequest().getString("content");

        if ( log.isDebugEnabled() ) {

            for (Object o : ((ActionRequest) forumActionBean.getRequest().getOriginRequest()).getParameterMap().entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                log.debug("key: " + entry.getKey() + ", value: " + entry.getValue());
            }
            log.debug( "content #1: " + ((ActionRequest)forumActionBean.getRequest().getOriginRequest()).getParameter("content"));
            log.debug( "content #2: " + forumActionBean.getRequest().getParameter("content"));
            log.debug( "content #3: " + forumActionBean.getRequest().getString("content"));
            log.debug( "content #4: " + content );
        }

        String u_lastip = forumActionBean.getRequest().getRemoteAddr();

        if (StringUtils.isEmpty(content) || (!reply && StringUtils.isEmpty(subject))) {
            return ForumError.blankError(forumActionBean);
        }
        DAOFactory daof = DAOFactory.getDAOFactory();
        PostPDAO postPDAO = daof.getPostPDAO();
        String urlString =
            moduleActionRequest.getUrlProvider().getUrl( Constants.WM_FORUM_PORTLET_NAME, Constants.TOPIC_ACTION ) +
            Constants.NAME_FORUM_ID+'=' +
            forumActionBean.getForumId() + '&' +
            Constants.NAME_FORUM_TOPIC_ID + '=';

        if (reply) {
            urlString = urlString + t_id + '&'+Constants.NAME_FORUM_MESSAGE_ID+'=' + postPDAO.reply(t_id, userId, content, u_lastip, tm_iconid, forumActionBean.getForumId());
        } else {
            urlString = urlString + postPDAO.post(f_id, userId, subject, content, u_lastip, tm_iconid, forumActionBean.getForumId());
        }

        if (log.isDebugEnabled()) {
            log.debug("redirect to: " + urlString );
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