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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.forum.bean.ForumConcreteBean;
import org.riverock.forum.bean.Topic;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.url.UrlProvider;

/**
 * ForumDAO interface
 */
public class ForumDAO {
    private final static Logger log = Logger.getLogger( ForumDAO.class );

    final static String sql =
        "select a.f_name, a.f_u_id, a.f_info, a.f_topics, a.f_messages, " +
        "       b.FIRST_NAME, b.MIDDLE_NAME, b.LAST_NAME " +
        "from   WM_FORUM_CONCRETE a, WM_LIST_USER b " +
        "where  a.F_U_ID=b.ID_USER and a.F_ID=?";

    public ForumConcreteBean execute(long f_id, int messagesPerPage, int topicsPerPage, int start, String keyword, Long forumId, UrlProvider urlProvider)
        throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            if (!CommonUtils.checkForumConcreteId( adapter, forumId, f_id )){
                return null;
            }

            ForumConcreteBean forumConcreteBean = new ForumConcreteBean();
            forumConcreteBean.setKeyword(keyword);
            StringBuilder urlString =
                urlProvider.getUrlStringBuilder( Constants.WM_FORUM_PORTLET_NAME, Constants.POST_ACTION ).
                append(Constants.NAME_FORUM_ID).append('=').append(forumId);

            forumConcreteBean.setUrlToPostThread(
                urlString.toString()+'&'+
                Constants.NAME_FORUM_CONCRETE_ID+'='+f_id
            );


            ps = adapter.prepareStatement( sql );
            ps.setLong(1, f_id);
            rs = ps.executeQuery();

            if (rs.next()) {
                forumConcreteBean.setF_id(f_id);
                forumConcreteBean.setF_name(rs.getString("f_name"));
                forumConcreteBean.setModeratorId(rs.getInt("f_u_id"));
                forumConcreteBean.setModeratorName(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                );
                forumConcreteBean.setF_info(rs.getString("f_info"));
                forumConcreteBean.setF_topics(rs.getInt("f_topics"));
                forumConcreteBean.setF_messages(rs.getInt("f_messages"));
            }

            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

            //------------------------
            String condition = " T_F_ID=? " ;
            if (StringUtils.isNotBlank(keyword)) {
                condition += " and T_NAME like ? ";
            }

            final String keywordSearch = "'%"+keyword+"%'";
            Long countRecord = DatabaseManager.getLongValue(
                adapter,
                "SELECT count(*) count_records " +
                "FROM   WM_FORUM_TOPIC  " +
                "WHERE  " + condition,
                (StringUtils.isNotBlank(keyword))
                ?new Object[]{f_id, keywordSearch}
                :new Object[]{f_id}
            );

            String sql1 =
                "select a.t_order, a.t_locked, a.t_iconid, a.t_id, a.t_name, a.t_u_id, " +
                "       a.t_replies, a.t_views, a.t_lasttime, a.t_u_id2, " +
                "       b.FIRST_NAME, b.MIDDLE_NAME, b.LAST_NAME, " +
                "       c.FIRST_NAME FIRST_NAME2 , c.MIDDLE_NAME MIDDLE_NAME2, c.LAST_NAME LAST_NAME2 " +
                "from   WM_FORUM_TOPIC a, WM_LIST_USER b, WM_LIST_USER c  " +
                "where  a.T_U_ID=b.ID_USER AND a.T_U_ID2=c.ID_USER AND " + condition + " " +
                "order by a.t_order DESC, a.t_lasttime DESC";
            ps = adapter.prepareStatement( sql1 );
            ps.setLong(1, f_id);
            if (StringUtils.isNotBlank(keyword)) {
                ps.setString(2, keywordSearch);
            }
            rs = ps.executeQuery();

            forumConcreteBean.setStart(start);
            forumConcreteBean.setTopicsPerPage(topicsPerPage);
            forumConcreteBean.setMessagesPerPage(messagesPerPage);
            forumConcreteBean.setCount(countRecord!=null?countRecord.intValue() :0);

            int countPages = forumConcreteBean.getCount()/topicsPerPage;
            if ((forumConcreteBean.getCount()%topicsPerPage)!=0) {
                countPages++;
            }
            forumConcreteBean.setCountPages( countPages );

            List<Topic> topicList = new ArrayList<Topic>(topicsPerPage+3);

            int j = 0;
            boolean isNext = true;
            while (isNext && j++ < start) {
                isNext = rs.next();
            }

            isNext = rs.next();

            for (int i = 0; isNext && i<topicsPerPage && !rs.isAfterLast(); i++) {
                Topic topic = new Topic();
                topic.setT_order(rs.getInt("t_order"));
                topic.setT_locked(rs.getInt("t_locked"));
                topic.setT_iconid(rs.getInt("t_iconid"));
                topic.setT_id(rs.getInt("t_id"));
                topic.setT_name(rs.getString("t_name"));
                topic.setT_u_id(rs.getInt("t_u_id"));
                topic.setT_replies(rs.getInt("t_replies"));
                topic.setT_views(rs.getString("t_views"));
                topic.setT_lasttime( rs.getTimestamp("t_lasttime") );
                topic.setT_u_id2(rs.getInt("t_u_id2"));
                topic.setU_name(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                );
                topic.setU_name2(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME2"), RsetTools.getString(rs, "MIDDLE_NAME2"), RsetTools.getString(rs, "LAST_NAME2") )
                );
                countPages = topic.getT_replies()/messagesPerPage;
                if ((topic.getT_replies()%messagesPerPage)!=0) {
                    countPages++;
                }
                topic.setCountPages( countPages );

                topicList.add(topic);

                rs.next();
            }
            forumConcreteBean.setTopics(topicList);

            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

            forumConcreteBean.setForums( CommonDAO.getForumsSmall(adapter, forumId) );
            return forumConcreteBean;
        } catch (Exception e) {
            String es = "Error execute forumDAO";
            log.error(es, e);
            throw new ActionException(es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }
}