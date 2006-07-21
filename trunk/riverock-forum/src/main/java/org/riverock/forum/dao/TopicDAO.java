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
package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.url.UrlProvider;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.forum.bean.Message;
import org.riverock.forum.bean.TopicBean;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;

/**
 * TopicDAO
 */
public class TopicDAO {
    private final static Logger log = Logger.getLogger(TopicDAO.class);

    public TopicBean execute(long t_id, int start, int messagesPerPage, UrlProvider urlProvider, Long forumId, ModuleUser moduleUser) throws ActionException {
        if (log.isDebugEnabled()) {
            log.debug("t_id: "+t_id);
            log.debug("start: "+start);
            log.debug("messagesPerPage: "+messagesPerPage);
            log.debug("forumId: "+forumId);
            log.debug("urlProvider: "+urlProvider);
            log.debug("moduleUser: "+moduleUser);
        }
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumTopicId( adapter, forumId, t_id )){
                log.error("Check for topicId was failed");
                return null;
            }

            if(log.isDebugEnabled()) {
                log.debug("Check of topicId and forumId is successful");
            }

            TopicBean topicBean = new TopicBean();
            sql =
                "select b.T_F_ID, a.F_NAME, a.F_INFO, a.F_U_ID, a.F_TOPICS, " +
                "       a.F_MESSAGES, b.T_NAME, b.T_REPLIES, b.T_VIEWS, b.T_ORDER, b.T_LOCKED, " +
                "       c.FIRST_NAME, c.MIDDLE_NAME, c.LAST_NAME " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_TOPIC b, WM_LIST_USER c " +
                "where  b.T_F_ID=a.F_ID and a.F_U_ID=c.ID_USER  and b.T_ID=? ";
            ps = adapter.prepareStatement(sql);
            ps.setLong(1, t_id);
            rs = ps.executeQuery();

            if (rs.next()) {
                topicBean.setF_id(rs.getInt("T_F_ID"));
                topicBean.setF_name(rs.getString("F_NAME"));
                topicBean.setF_info(rs.getString("F_INFO"));
                topicBean.setF_u_id(rs.getInt("F_U_ID"));
                topicBean.setModeratorName(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                );
                topicBean.setF_topics(rs.getInt("F_TOPICS"));
                topicBean.setF_messages(rs.getInt("F_MESSAGES"));
                topicBean.setT_id(t_id);
                topicBean.setT_name(rs.getString("T_NAME"));
                topicBean.setT_replies(rs.getInt("T_REPLIES"));
                topicBean.setT_views(rs.getString("T_VIEWS"));
                topicBean.setT_order(rs.getInt("T_ORDER"));
                topicBean.setT_locked(rs.getInt("T_LOCKED"));

                StringBuilder urlString =
                    urlProvider.getUrlStringBuilder( Constants.WM_FORUM_PORTLET_NAME, Constants.POST_ACTION ).
                    append(Constants.NAME_FORUM_ID).append('=').append(forumId);

                topicBean.setPostTopicUrl(
                    urlString.toString()+'&'+
                    Constants.NAME_FORUM_CONCRETE_ID+'='+topicBean.getF_id()
                );
                topicBean.setReplyTopicUrl(
                    urlString.toString()+'&'+
                    Constants.NAME_FORUM_TOPIC_ID+'='+topicBean.getT_id()
                );
                topicBean.setMessagesPerPage( messagesPerPage );
                int countPages = (topicBean.getT_replies()/messagesPerPage);
                if ((topicBean.getT_replies()%messagesPerPage)!=0) {
                    countPages++;
                }
                if (log.isDebugEnabled()) {
                    log.debug("messagesPerPage: " + messagesPerPage);
                    log.debug("topicBean.getT_replies(): " + topicBean.getT_replies());
                    log.debug("countPages: " + countPages);
                }
                topicBean.setCountPages( countPages );

            }
            else {
                if(log.isDebugEnabled()) {
                    log.debug("Create main topic bean failed");
                }
            }
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

            //------------------------
            sql =
                "select a.m_id, a.m_iconid, a.m_content, a.m_time, a.m_u_id, " +
                "       c.FIRST_NAME, c.MIDDLE_NAME, c.LAST_NAME, c.ADDRESS, c.DATE_START_WORK, " +
                "       b.u_post, b.u_sign, b.u_avatar_id " +
                "from   WM_FORUM_MESSAGE a, WM_FORUM_USER b, WM_LIST_USER c " +
                "where  a.M_U_ID=b.U_ID and a.m_u_id=c.ID_USER and a.M_T_ID=? " +
                "order by a.M_TIME ASC";

            ps = adapter.prepareStatement(sql);
            ps.setLong(1, t_id);
            rs = ps.executeQuery();

            int j = 0;
            boolean isNext = true;
            while (isNext && j++ < start) {
                isNext = rs.next();
            }

            isNext = rs.next();

            List<Message> messageList = new ArrayList<Message>( messagesPerPage+3 );
            boolean isFound = false;
            for (int i = 0; isNext && i<messagesPerPage && !rs.isAfterLast(); i++) {
                isFound = true;
                Message message = new Message();
                message.setM_id(rs.getInt("m_id"));
                message.setM_iconid(rs.getInt("m_iconid"));
                message.setM_content(rs.getString("m_content"));
                message.setM_time(rs.getTimestamp("m_time"));
                message.setM_u_id(rs.getInt("m_u_id"));
                message.setU_name(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                );
                message.setU_regtime(rs.getTimestamp("DATE_START_WORK"));
                message.setU_address(rs.getString("ADDRESS"));
                message.setU_post(rs.getInt("u_post"));
                message.setU_sign( rs.getString("u_sign") );
                message.setU_avatar_id( rs.getInt("u_avatar_id") );

                if (moduleUser!=null && moduleUser.getId()==null) {
                    throw new IllegalStateException("User unknown");
                }

                if (moduleUser!=null && moduleUser.getId().intValue()==message.getM_u_id()) {
                    message.setEdited( true );
                }

                setUserRoleDescription( message );

                messageList.add(message);

                rs.next();
            }

            if (!isFound) {
                if(log.isDebugEnabled()) {
                    log.debug("record not found");
                }
                return null;
            }

            topicBean.setMessages(messageList);

            //view
            DatabaseManager.runSQL(adapter,
                "update WM_FORUM_TOPIC " +
                "set T_VIEWS=T_VIEWS+1 " +
                "where T_ID=? ",
                new Object[]{ t_id },
                new int[]{Types.INTEGER});
//                dba.runSql("UPDATE lb_topic SET t_views=t_views+1 WHERE t_id=" + t_id);

            topicBean.setForums(CommonDAO.getForumsSmall(adapter, forumId));
            adapter.commit();
            return topicBean;
        }
        catch (Exception ex) {
            try {
                if (adapter!=null) {
                    adapter.rollback();
                }
            } catch (SQLException sqle) {
                // catch rollback error
            }
            String es = "Error execute topicDAO";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    private void setUserRoleDescription(Message message) {
        message.setR_name("");
    }

}