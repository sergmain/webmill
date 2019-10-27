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
import org.riverock.forum.ForumPortlet;
import org.riverock.forum.bean.PostBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.PostDAO;
import org.riverock.forum.util.Constants;
import org.riverock.forum.util.ForumStringUtils;
import org.riverock.module.action.ActionInstance;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.session.ModuleSession;

public class PostAction implements ActionInstance {
    private final static Logger log = Logger.getLogger(PostAction.class);

    public String execute(ModuleActionRequest moduleActionRequest) throws ActionException {

        ForumActionBean forumActionBean = (ForumActionBean)moduleActionRequest;
        Integer f_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_CONCRETE_ID );
        Integer t_id = forumActionBean.getRequest().getInt( Constants.NAME_FORUM_TOPIC_ID );
        boolean reply = ForumStringUtils.parseBoolean(forumActionBean.getRequest().getString(Constants.NAME_FORUM_REPLY));
        if (reply && t_id==null ) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        if (!reply && f_id==null) {
            return ForumError.noSuchForumError(forumActionBean);
        }

        DAOFactory daof = DAOFactory.getDAOFactory();
        PostDAO postDAO = daof.getPostDAO();

        ModuleSession sess = forumActionBean.getRequest().getSession(false);

        Long lastPortTime = (Long)sess.getAttribute(Constants.LAST_POST_TIME);
        if (log.isDebugEnabled()) {
            log.debug("strLPT: " + lastPortTime);
        }
        if ( lastPortTime!=null ) {
            int floodTime = ForumPortlet.getFloodTime( forumActionBean.getConfig() );
            if ((System.currentTimeMillis()- lastPortTime) < floodTime * 1000) {
                return ForumError.floodError(forumActionBean);
            }
        }
        sess.setAttribute(Constants.LAST_POST_TIME, System.currentTimeMillis() );

        if (log.isDebugEnabled()) {
            log.debug("reply: " + reply);
            log.debug("t_id: " + t_id);
        }
        PostBean postBean;
        if (reply) {
            postBean = postDAO.reply(forumActionBean.getForumId(), t_id);
            if (postBean != null && postBean.getT_locked() > 0) {
                return ForumError.topicLockedError(forumActionBean);
            }
        } else {
            postBean = postDAO.post(forumActionBean.getForumId(), f_id);
        }

        if (postBean != null) {
            postBean.setForumId( forumActionBean.getForumId() );

            if (log.isDebugEnabled()) {
                log.debug("postBean: " + postBean);
            }

            postBean.setReply(reply);
            forumActionBean.getRequest().setAttribute("postBean", postBean);
            return Constants.OK_EXECUTE_STATUS;
        } else {
            return ForumError.postError(forumActionBean);
        }
    }

}