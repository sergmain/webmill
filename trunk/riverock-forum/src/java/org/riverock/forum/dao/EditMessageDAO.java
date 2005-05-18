package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.forum.bean.MessageBean;
import org.riverock.forum.bean.PostBean;
import org.riverock.forum.core.GetWmForumMessageItem;
import org.riverock.forum.schema.core.WmForumMessageItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:47:53
 *         $Id$
 */
public class EditMessageDAO {
    private static final Log log = LogFactory.getLog(EditMessageDAO.class);

    public MessageBean get(Long forumId, Long topicId, Integer messageId) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumMessageId( adapter, forumId, messageId )){
                return null;
            }

            String sql =
                "select t_f_id, f_name,t_name,t_locked " +
                "from   WM_FORUM_CONCRETE, WM_FORUM_TOPIC " +
                "where  t_f_id=f_id and t_id=? ";
            ps = adapter.prepareStatement(sql);
            ps.setLong(1, topicId.longValue() );
            rs = ps.executeQuery();

            MessageBean messageBean = new MessageBean();
            if (!rs.next()) {
                return null;
            }
            else {
                messageBean.setForumName( rs.getString("f_name") );
                messageBean.setTopicName( rs.getString("t_name") );
            }

            WmForumMessageItemType item = new GetWmForumMessageItem(adapter, messageId.intValue() ).item;

            if (item==null) {
                return null;
            }

            messageBean.setContent( item.getMContent() );
            messageBean.setMessageId( item.getMId().intValue() );
            messageBean.setIconId( item.getMIconid().intValue() );
            return messageBean;
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
}
