package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.forum.bean.PostBean;
import org.riverock.forum.core.GetWmForumConcreteItem;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * PostDAO
 */
public class PostDAO {
    private static final Log log = LogFactory.getLog(PostDAO.class);

    public PostBean post(Long forumId, int f_id) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumConcreteId( adapter, forumId, new Integer( f_id ) )){
                return null;
            }

            WmForumConcreteItemType item = new GetWmForumConcreteItem(adapter, f_id).item;

            if (item==null) {
                return null;
            }
            PostBean postBean = new PostBean();
            postBean.setF_id(f_id);
            postBean.setF_name( item.getFName() );
            return postBean;
        }
        catch (Exception ex) {
            String es = "Error post postDAO";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public PostBean reply(Long forumId, int t_id) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumTopicId( adapter, forumId, new Integer( t_id ) )){
                return null;
            }

            String sql =
                "select t_f_id, f_name,t_name,t_locked " +
                "from   WM_FORUM_CONCRETE, WM_FORUM_TOPIC " +
                "where  t_f_id=f_id and t_id=? ";
            ps = adapter.prepareStatement(sql);
            ps.setInt(1, t_id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            else {
                PostBean postBean = new PostBean();
                postBean.setF_id(rs.getInt("t_f_id"));
                postBean.setF_name(rs.getString("f_name"));
                postBean.setT_id(t_id);
                postBean.setT_name(rs.getString("t_name"));
                postBean.setT_locked(rs.getInt("t_locked"));
                return postBean;
            }
        }
        catch (Exception ex) {
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
}