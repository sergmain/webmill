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
package org.riverock.forum.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.forum.core.*;
import org.riverock.module.exception.ActionException;
import org.riverock.forum.schema.core.WmForumMessageItemType;
import org.riverock.forum.schema.core.WmForumTopicItemType;
import org.riverock.forum.schema.core.WmForumUserItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.exception.PersistenceException;

/**
 * PostPDAO interface
 */
public class PostPDAO {
    private static Logger log = Logger.getLogger(PostPDAO.class);

    public Long post(long f_id, Long userId, String subject, String content,
        String u_lastip, int tm_iconid, Long forumId) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Long id = post(adapter, f_id, userId, subject, content, u_lastip, tm_iconid, forumId);
            adapter.commit();
            return id;
        } catch (Exception ex) {
            try {
                if (adapter!=null) {
                    adapter.rollback();
                }
            } catch (SQLException sqle) {
                // catch rollback error
            }
            String es = "Error post topic";
            log.error(es, ex);
            throw new ActionException(es, ex);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public static Long post(DatabaseAdapter adapter, Long f_id, Long userId, String subject, String content,
        String u_lastip, long tm_iconid, Long forumId) throws SQLException, PersistenceException {

        if (!CommonUtils.checkForumConcreteId(adapter, forumId, f_id)) {
            return null;
        }

        Date currentDate = new Timestamp(System.currentTimeMillis());
//        Integer userIdAsInt = userId;

        //1
        final Long tId =CommonDAO.getTopicID(adapter);
        WmForumTopicItemType topic = new WmForumTopicItemType();
        topic.setTOrder(0L);
        topic.setTLocked(0L);
        topic.setTId(tId);
        topic.setTFId(f_id);
        topic.setTName(subject);
        topic.setTUId(userId);
        topic.setTReplies(0L);
        topic.setTViews(0L);
        topic.setTLasttime(currentDate);
        topic.setTUId2(userId);
        topic.setTIconid(tm_iconid);
        InsertWmForumTopicItem.process(adapter, topic);

        //2
        WmForumMessageItemType message = new WmForumMessageItemType();
        message.setMId(CommonDAO.getMessageID(adapter));
        message.setMIconid(tm_iconid);
        message.setMTId(tId);
        message.setMContent(content);
        message.setMUId(userId);
        message.setMTime(currentDate);
        InsertWmForumMessageItem.process(adapter, message);

        //3
        GetWmForumUserItem userFactory = new GetWmForumUserItem(adapter, userId);
        if (userFactory.isFound) {
            WmForumUserItemType user = userFactory.item;
            user.setUPost(user.getUPost() + 1);
            user.setULasttime(currentDate);
            user.setULastip(u_lastip);
            UpdateWmForumUserItem.process(adapter, user);
        } else {
            WmForumUserItemType user = new WmForumUserItemType();
            user.setUId(userId);
            user.setUPost(1L);
            user.setULasttime(currentDate);
            user.setULastip(u_lastip);
            user.setUAvatarId(0L);
            user.setUSign("");
            InsertWmForumUserItem.process(adapter, user);
        }

        //4
        GetWmForumConcreteItem forumFactory = new GetWmForumConcreteItem(adapter, f_id);
        if (forumFactory.isFound) {
            WmForumConcreteItemType forum = forumFactory.item;
            forum.setFTopics(forum.getFTopics() + 1);
            forum.setFMessages(forum.getFTopics() + 1);
            forum.setFUId2(userId);
            forum.setFLasttime(currentDate);
            UpdateWmForumConcreteItem.process(adapter, forum);
        }

        return tId;
    }

    public Long reply(int t_id, Long u_id, String content, String u_lastip, int tm_iconid, Long forumId)
        throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Long mId = reply(adapter, t_id, u_id, content, u_lastip, tm_iconid, forumId);
            adapter.commit();
            return mId;
        } catch (Exception ex) {
            try {
                if (adapter!=null) {
                    adapter.rollback();
                }
            } catch (SQLException sqle) {
                // catch rollback error
            }
            String es = "Error reply on topic";
            log.error(es, ex);
            throw new ActionException(es, ex);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public static Long reply(DatabaseAdapter adapter, long tId, Long userId, String content, String u_lastip, long tm_iconid, Long forumId) throws SQLException, PersistenceException {

        if (!CommonUtils.checkForumTopicId(adapter, forumId, tId)) {
            return null;
        }

        final Date currentDate = new Timestamp(System.currentTimeMillis());

        //1
        DatabaseManager.runSQL(adapter,
            "update WM_FORUM_TOPIC " +
            "set T_REPLIES=T_REPLIES+1, T_LASTTIME=?, T_U_ID2=? where T_ID=? ",
            new Object[]{currentDate, userId, tId},
            new int[]{Types.TIMESTAMP, Types.INTEGER, Types.INTEGER});

        //2
        WmForumMessageItemType message = new WmForumMessageItemType();
        final Long mId = CommonDAO.getMessageID(adapter);
        message.setMId(mId);
        message.setMIconid(tm_iconid);
        message.setMTId(tId);
        message.setMContent(content);
        message.setMUId(userId);
        message.setMTime(currentDate);
        InsertWmForumMessageItem.process(adapter, message);

        //3
        DatabaseManager.runSQL(adapter,
            "update WM_FORUM_USER " +
            "set    U_POST=U_POST+1, U_LASTTIME=?, U_LASTIP=? " +
            "where U_ID=? ",
            new Object[]{currentDate, u_lastip, userId},
            new int[]{Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER});

        //4
        Long forumConcreteId = DatabaseManager.getLongValue(adapter,
            "select T_F_ID from WM_FORUM_TOPIC where T_ID=?",
            new Object[]{tId});

        if (forumConcreteId != null) {
            DatabaseManager.runSQL(adapter,
                "update WM_FORUM_CONCRETE " +
                "set    F_MESSAGES=F_MESSAGES+1, F_U_ID2=?, F_LASTTIME=? " +
                "where  F_ID=? ",
                new Object[]{userId, currentDate, forumConcreteId},
                new int[]{Types.INTEGER, Types.TIMESTAMP, Types.INTEGER});
        }
        return mId;
    }
}