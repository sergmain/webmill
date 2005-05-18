package org.riverock.forum.util;

import org.riverock.forum.core.InsertWmForumCategoryItem;
import org.riverock.forum.core.InsertWmForumConcreteItem;
import org.riverock.forum.dao.CommonDAO;
import org.riverock.forum.dao.PostPDAO;
import org.riverock.forum.schema.core.WmForumCategoryItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.startup.StartupApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author SMaslyukov
 *         Date: 16.05.2005
 *         Time: 19:46:57
 *         $Id$
 */
public class UpgradeForumFromV1ToV2 {
    private static class SiteBean {
        private long id;
        private long firmId;
        private String name;
        private List forums = null;
    }

    private static class ForumBean {
        private long id;
        private String name;
        private List topics = null;
    }

    private static class ForumTopicBean {
        private long id;
        private String name;
        private List messages = null;
    }

    private static class ForumMessageBean {
        private long id;
        private String name;
        private String text;
        private Date postDate = null;
        private String fio;
        private String email;
    }


    public static void main(String s[]) throws Exception {

        
        StartupApplication.init();
        System.out.println("GenericConfig.getGenericDebugDir() = " + GenericConfig.getGenericDebugDir());


        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance( true, s[0] );
            List sites = getSitesList( adapter );

            Iterator it = sites.iterator();
            while (it.hasNext()) {
                SiteBean site = (SiteBean) it.next();

                long userId = createUser( adapter, site );
                long forumId = createForum( adapter, site );
                long forumCategoryId = createForumCategory( adapter, forumId );

                System.out.println("Create forum for site: " + site.name );
                System.out.println("  userId = " + userId);
                System.out.println("  forumId = " + forumId);
                System.out.println("  forumCategoryId = " + forumCategoryId);

                Iterator iterator = site.forums.iterator();
                while (iterator.hasNext()) {
                    ForumBean forumBean = (ForumBean) iterator.next();

                    System.out.println("    Create concrete forum: " + forumBean.name);
                    long forumConcreteId = createForumConcrete( adapter, forumCategoryId, forumBean, userId );
                    Iterator topicIterator = forumBean.topics.iterator();
                    while(topicIterator.hasNext()) {
                        ForumTopicBean topicBean = (ForumTopicBean)topicIterator.next();
                        System.out.println("      process topic " + topicBean.name);
                        Iterator messageIterator = topicBean.messages.iterator();
                        Integer topicId = null;
                        while(messageIterator.hasNext()) {
                            ForumMessageBean messageBean = (ForumMessageBean)messageIterator.next();
                            if (topicId==null) {
                                topicId = PostPDAO.post(
                                    adapter,
                                    (int)forumConcreteId, new Long(userId),
                                    messageBean.name + " ("+ messageBean.fio +")",
                                    messageBean.text,
                                    "127.0.0.1", 0,
                                    new Long(forumId)
                                );
                            }
                            else {
                                PostPDAO.reply(
                                    adapter,
                                    topicId.intValue(), new Long(userId),
                                    messageBean.text,
                                    "127.0.0.1", 0,
                                    new Long(forumId)
                                );
                            }
                        }
                    }
                }
            }
            adapter.commit();
        }
        finally {
            DatabaseManager.close( adapter );
            adapter = null;
        }

    }

    private static long createForumConcrete(DatabaseAdapter adapter, long forumCategoryId, ForumBean forumBean, long userId) throws Exception {

        int forumConcreteID = CommonDAO.getForumConcreteID( adapter );

        WmForumConcreteItemType item = new WmForumConcreteItemType();
        item.setFName( "Old forum: '"+forumBean.name );
        item.setFInfo( "Old forum: '"+forumBean.name );
        item.setFId( new Integer(forumConcreteID) );
        item.setForumCategoryId( new Integer((int)forumCategoryId));

        // hack - set moderator and last poster to admin userID
        item.setFUId( new Integer((int)userId) );
        item.setFUId2( new Integer((int)userId) );

        InsertWmForumConcreteItem.process( adapter, item );

        return forumConcreteID;
    }

    private static long createForumCategory(DatabaseAdapter adapter, long forumId) throws Exception {

        int forumCategoryID = CommonDAO.getForumCategoryID( adapter );

        WmForumCategoryItemType item = new WmForumCategoryItemType();
        item.setForumCategoryName( "Old forums" );
        item.setForumId( new Integer((int)forumId) );
        item.setForumCategoryId( new Integer(forumCategoryID) );
        item.setIsUseLocale( Boolean.FALSE );
        InsertWmForumCategoryItem.process( adapter, item );

        return forumCategoryID;
    }

    private static long createForum(DatabaseAdapter adapter, SiteBean site) throws Exception {

        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM" );
        seq.setTableName( "WM_FORUM" );
        seq.setColumnName( "FORUM_ID" );
        long forumId = adapter.getSequenceNextValue( seq );

        DatabaseManager.runSQL(
            adapter,
            "insert into WM_FORUM " +
            "(FORUM_ID, IS_USE_LOCALE, SITE_ID, FORUM_NAME) " +
            "values" +
            "(?, 0, ?, ?)",
            new Object[] {new Long(forumId), new Long(site.id), "Forum for site "+site.name },
            new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR }
        );
        return forumId;
    }

    private static long createUser(DatabaseAdapter adapter, SiteBean site) throws Exception {

        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_MAIN_USER_INFO" );
        seq.setTableName( "MAIN_USER_INFO" );
        seq.setColumnName( "ID_USER" );
        long userId = adapter.getSequenceNextValue( seq );

        DatabaseManager.runSQL(
            adapter,
            "insert into MAIN_USER_INFO " +
            "(ID_USER, ID_FIRM, FIRST_NAME, DATE_START_WORK, IS_DELETED) " +
            "values" +
            "(?, ?, 'Old forum user', ?, 0)",
            new Object[] {new Long(userId), new Long(site.firmId), new Timestamp(System.currentTimeMillis()) },
            new int[] { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP }
        );
        return userId;
    }

    private static List getSitesList(DatabaseAdapter adapter) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            ps = adapter.prepareStatement(
                "select DISTINCT a.ID_SITE, a.NAME_SITE, a.ID_FIRM " +
                "from   SITE_LIST_SITE a, MAIN_FORUM b " +
                "where  a.ID_SITE=b.ID_SITE "
            );
            rs = ps.executeQuery();

            while (rs.next()) {
                SiteBean site = new SiteBean();
                site.id = rs.getLong("ID_SITE");
                site.firmId = rs.getLong("ID_FIRM");
                site.name = rs.getString("NAME_SITE");
                site.forums = getForumsList(adapter, site.id);
                list.add( site );
            }
        }
        finally {
            DatabaseManager.close( rs, ps );
            ps = null;
            rs = null;
        }
        return list;
    }

    private static List getForumsList(DatabaseAdapter adapter, long siteId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            ps = adapter.prepareStatement(
                "select a.* from MAIN_FORUM a " +
                "where a.ID_SITE=? "
            );
            ps.setLong(1, siteId );
            rs = ps.executeQuery();

            while (rs.next()) {
                ForumBean forumBean = new ForumBean();
                forumBean.id = rs.getLong("ID_FORUM");
                forumBean.name = rs.getString("NAME_FORUM");
                forumBean.topics = getForumTopicsList(adapter, forumBean.id);
                list.add( forumBean );
            }
        }
        finally {
            DatabaseManager.close( rs, ps );
            ps = null;
            rs = null;
        }
        return list;
    }

    private static List getForumTopicsList(DatabaseAdapter adapter, long forumId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            ps = adapter.prepareStatement(
                "select a.* from MAIN_FORUM_THREADS a " +
                "where a.ID_FORUM=? and a.ID_MAIN=0 "
            );
            ps.setLong(1, forumId );
            rs = ps.executeQuery();

            while (rs.next()) {
                ForumTopicBean topicBean = new ForumTopicBean();
                topicBean.id = rs.getLong("ID_THREAD");
                topicBean.name = rs.getString("HEADER");
                topicBean.messages = getForumMesagesList( adapter, topicBean.id );
                list.add( topicBean );
            }
        }
        finally {
            DatabaseManager.close( rs, ps );
            ps = null;
            rs = null;
        }
        return list;
    }

    private static List getForumMesagesList(DatabaseAdapter adapter, long topicId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            ps = adapter.prepareStatement(
                "select a.* from MAIN_FORUM_THREADS a " +
                "where  a.ID_THREAD=? " +
                "order by a.DATE_POST ASC "
            );
            ps.setLong(1, topicId );
            rs = ps.executeQuery();

            while (rs.next()) {
                ForumMessageBean messageBean = new ForumMessageBean();
                messageBean.id = rs.getLong("ID");
                messageBean.name = rs.getString("HEADER");
                messageBean.postDate = rs.getTimestamp("DATE_POST");
                messageBean.fio = rs.getString("FIO");
                messageBean.email = rs.getString("EMAIL");
                messageBean.text = getMessageText( adapter, messageBean.id );
                list.add( messageBean );
            }
        }
        finally {
            DatabaseManager.close( rs, ps );
            ps = null;
            rs = null;
        }
        return list;
    }

    private static String getMessageText(DatabaseAdapter adapter, long messageId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
        try {
            ps = adapter.prepareStatement(
                "select a.* from MAIN_FORUM_TEXT a " +
                "where  a.ID=? " +
                "order by ID_MAIN_FORUM_TEXT asc"
            );
            ps.setLong(1, messageId );
            rs = ps.executeQuery();

            while (rs.next()) {
                sb.append( rs.getString("MESSAGE_TEXT") );
            }
        }
        finally {
            DatabaseManager.close( rs, ps );
            ps = null;
            rs = null;
        }
        return sb.toString();
    }
}
