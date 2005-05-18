package org.riverock.forum.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
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

    public Integer post(int f_id, Long userId, String subject, String content,
        String u_lastip, int tm_iconid, Long forumId) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Integer id = post(adapter, f_id, userId, subject, content, u_lastip, tm_iconid, forumId);
            adapter.commit();
            return id;
        } catch (Exception ex) {
            try {
                adapter.rollback();
            } catch (SQLException sqle) {
            }
            String es = "Error post topic";
            log.error(es, ex);
            throw new ActionException(es, ex);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public static Integer post(DatabaseAdapter adapter, int f_id, Long userId, String subject, String content,
        String u_lastip, int tm_iconid, Long forumId) throws SQLException, PersistenceException {

        if (!CommonUtils.checkForumConcreteId(adapter, forumId, new Integer(f_id))) {
            return null;
        }

        Date currentDate = new Timestamp(System.currentTimeMillis());
        Integer userIdAsInt = new Integer(userId.intValue());

        //1
        final Integer tId = new Integer(CommonDAO.getTopicID(adapter));
        WmForumTopicItemType topic = new WmForumTopicItemType();
        topic.setTOrder(new Integer(0));
        topic.setTLocked(new Integer(0));
        topic.setTId(tId);
        topic.setTFId(new Integer(f_id));
        topic.setTName(subject);
        topic.setTUId(userIdAsInt);
        topic.setTReplies(new Integer(0));
        topic.setTViews(new Integer(0));
        topic.setTLasttime(currentDate);
        topic.setTUId2(userIdAsInt);
        topic.setTIconid(new Integer(tm_iconid));
        InsertWmForumTopicItem.process(adapter, topic);

        //2
        WmForumMessageItemType message = new WmForumMessageItemType();
        message.setMId(new Integer(CommonDAO.getMessageID(adapter)));
        message.setMIconid(new Integer(tm_iconid));
        message.setMTId(tId);
        message.setMContent(content);
        message.setMUId(userIdAsInt);
        message.setMTime(currentDate);
        InsertWmForumMessageItem.process(adapter, message);

        //3
        GetWmForumUserItem userFactory = new GetWmForumUserItem(adapter, userId);
        if (userFactory.isFound) {
            WmForumUserItemType user = userFactory.item;
            user.setUPost(new Integer(user.getUPost().intValue() + 1));
            user.setULasttime(currentDate);
            user.setULastip(u_lastip);
            UpdateWmForumUserItem.process(adapter, user);
        } else {
            WmForumUserItemType user = new WmForumUserItemType();
            user.setUId(userIdAsInt);
            user.setUPost(new Integer(1));
            user.setULasttime(currentDate);
            user.setULastip(u_lastip);
            user.setUAvatarId(new Integer(0));
            user.setUSign("");
            InsertWmForumUserItem.process(adapter, user);
        }

        //4
        GetWmForumConcreteItem forumFactory = new GetWmForumConcreteItem(adapter, new Long(f_id));
        if (forumFactory.isFound) {
            WmForumConcreteItemType forum = forumFactory.item;
            forum.setFTopics(new Integer(forum.getFTopics().intValue() + 1));
            forum.setFMessages(new Integer(forum.getFTopics().intValue() + 1));
            forum.setFUId2(userIdAsInt);
            forum.setFLasttime(currentDate);
            UpdateWmForumConcreteItem.process(adapter, forum);
        }

        return tId;
    }

    public Integer reply(int t_id, Long u_id, String content, String u_lastip, int tm_iconid, Long forumId)
        throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Integer mId = reply(adapter, t_id, u_id, content, u_lastip, tm_iconid, forumId);
            adapter.commit();
            return mId;
        } catch (Exception ex) {
            try {
                adapter.rollback();
            } catch (SQLException sqle) {
            }
            String es = "Error reply on topic";
            log.error(es, ex);
            throw new ActionException(es, ex);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public static Integer reply(DatabaseAdapter adapter, int t_id, Long u_id, String content, String u_lastip, int tm_iconid, Long forumId) throws SQLException, DatabaseException, PersistenceException {

        if (!CommonUtils.checkForumTopicId(adapter, forumId, new Integer(t_id))) {
            return null;
        }

        final Date currentDate = new Timestamp(System.currentTimeMillis());
        final Integer userId = new Integer(u_id.intValue());
        final Integer tId = new Integer(t_id);

        //1
        DatabaseManager.runSQL(adapter,
            "update WM_FORUM_TOPIC " +
            "set T_REPLIES=T_REPLIES+1, T_LASTTIME=?, T_U_ID2=? where T_ID=? ",
            new Object[]{currentDate, userId, tId},
            new int[]{Types.TIMESTAMP, Types.INTEGER, Types.INTEGER});

        //2
        WmForumMessageItemType message = new WmForumMessageItemType();
        final Integer mId = new Integer(CommonDAO.getMessageID(adapter));
        message.setMId(mId);
        message.setMIconid(new Integer(tm_iconid));
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