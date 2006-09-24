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
import org.riverock.forum.ForumPortlet;
import org.riverock.forum.bean.PostBean;
import org.riverock.forum.dao.DAOFactory;
import org.riverock.forum.dao.PostDAO;
import org.riverock.forum.util.Constants;
import org.riverock.forum.util.ForumStringUtils;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.session.ModuleSession;

public class PostAction implements Action {
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