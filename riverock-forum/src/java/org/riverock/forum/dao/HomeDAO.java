package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.forum.bean.ForumBean;
import org.riverock.forum.bean.ForumCategoryBean;
import org.riverock.forum.bean.ForumConcreteBean;
import org.riverock.forum.core.GetWmForumCategoryWithForumIdList;
import org.riverock.forum.core.GetWmForumItem;
import org.riverock.forum.schema.core.WmForumCategoryItemType;
import org.riverock.forum.schema.core.WmForumCategoryListType;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.util.Constants;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;
import org.riverock.module.web.url.UrlProvider;

/**
 * HomeDAO interface
 */
public class HomeDAO {
    private final static Logger log = Logger.getLogger(HomeDAO.class);

    private static String sqlStart =
        "select a.f_id, a.f_name, a.f_info, a.f_u_id, " +
        "       a.f_topics, a.f_messages, a.f_u_id2, " +
        "       a.f_lasttime, a.IS_DELETED, " +
        "       b.FIRST_NAME, b.MIDDLE_NAME, b.LAST_NAME, " +
        "       c.FIRST_NAME FIRST_NAME2 , c.MIDDLE_NAME MIDDLE_NAME2, c.LAST_NAME LAST_NAME2 " +
        "from   WM_FORUM_CONCRETE a, WM_LIST_USER b, WM_LIST_USER c " +
        "where  a.F_U_ID=b.ID_USER and a.F_U_ID2=c.ID_USER and a.FORUM_CATEGORY_ID=? ";

    private static String sqlFilterDeleted =
        "       and a.IS_DELETED=0 ";

    private static String sqlOrder =
        "order by F_ORDER ASC";

    public ForumBean execute(UrlProvider urlProvider, Long forumId, boolean isFilterDeleted) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmForumCategoryListType categories = new GetWmForumCategoryWithForumIdList(adapter, forumId).item;
            WmForumItemType forum = new GetWmForumItem(adapter, forumId).item;
            if (forum==null) {
                return null;
            }
            ForumBean forumBean = new ForumBean();
            forumBean.setForumName( forum.getForumName() );
            forumBean.setForumId( forumId );
            int topicSum = 0;
            int messageSum = 0;
            List categoriesList = new LinkedList();
            forumBean.setForumCategories( categoriesList );
            for (int i=0; i<categories.getWmForumCategoryCount(); i++) {
                WmForumCategoryItemType item = categories.getWmForumCategory(i);

                ForumCategoryBean categoryBean = new ForumCategoryBean();
                categoryBean.setForumCategoryId( item.getForumCategoryId() );
                categoryBean.setCategoryName( item.getForumCategoryName() );
                categoryBean.setDeleted( item.getIsDeleted().booleanValue() );
                categoriesList.add(categoryBean);

                String sql = sqlStart;
                if (isFilterDeleted) {
                    sql += sqlFilterDeleted;
                }
                sql += sqlOrder;

                ps = adapter.prepareStatement( sql );
                ps.setInt(1, item.getForumCategoryId().intValue() );
                rs = ps.executeQuery();;


                List forums = new LinkedList();
                categoryBean.setForums( forums );
                while (rs.next()) {
                    ForumConcreteBean forumConcrete = new ForumConcreteBean();
                    forums.add( forumConcrete );

                    forumConcrete.setDeleted(
                        RsetTools.getInt(rs, "IS_DELETED", new Integer(0)).intValue()==1
                    );
                    forumConcrete.setF_id(rs.getInt("f_id"));
                    forumConcrete.setF_name(rs.getString("f_name"));
                    forumConcrete.setF_info(rs.getString("f_info"));
                    forumConcrete.setModeratorId(rs.getInt("f_u_id"));
                    forumConcrete.setF_topics(rs.getInt("f_topics"));
                    forumConcrete.setF_messages(rs.getInt("f_messages"));
                    forumConcrete.setLastPorterId(rs.getInt("f_u_id2"));
                    forumConcrete.setModeratorName(
                        StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                    );
                    String firstName = RsetTools.getString(rs, "FIRST_NAME2");
                    String middleName = RsetTools.getString(rs, "MIDDLE_NAME2");
                    String lastName = RsetTools.getString(rs, "LAST_NAME2");
                    if (log.isDebugEnabled()) {
                        log.debug("first: " + firstName + ", middle: " + middleName + ", last; " + lastName );
                    } 
                    forumConcrete.setLastPosterName(
                        StringTools.getUserName( firstName, middleName, lastName )
                    );

                    if (forumConcrete.getF_topics()!=0) {
                        forumConcrete.setF_lasttime(rs.getTimestamp("f_lasttime"));
                    }
                    else {
                        forumConcrete.setF_lasttime( null );
                    }
                    String urlString =
                        urlProvider.getUrl( Constants.WM_FORUM_PORTLET_NAME, Constants.FORUM_ACTION ) +
                        Constants.NAME_FORUM_ID+'='+forumId+'&'+
                        Constants.NAME_FORUM_CONCRETE_ID+'='+forumConcrete.getF_id();

                    forumConcrete.setForumUrl( urlString );
                    topicSum += rs.getInt("f_topics");
                    messageSum += rs.getInt("f_messages");
                }
            }

            //topicsSum
            forumBean.setTopicSum(topicSum);
            //messagesSum
            forumBean.setMessageSum(messageSum);
            //usersSum
            Long countRecord = DatabaseManager.getLongValue(
                adapter,
                "select count(*) count_records from WM_FORUM_USER",
                new Object[]{}
            );
            forumBean.setUserSum( ""+(countRecord!=null?countRecord.intValue() :0) );

            return forumBean;
        }
        catch (Exception e) {
            String es = "Error execute homeDAO";
            log.error(es, e);
            throw new ActionException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }
}