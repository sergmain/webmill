package org.riverock.forum.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.forum.dao.CommonDAO;
import org.riverock.forum.schema.core.WmForumItemType;
import org.riverock.forum.core.GetWmForumItem;

/**
 * @author SMaslyukov
 *         Date: 14.04.2005
 *         Time: 19:42:03
 *         $Id$
 */
public final class CommonUtils {
    private final static Logger log = Logger.getLogger(CommonDAO.class);

    public static String getUserName(String firstName, String middleName, String lastName) {
        if (log.isDebugEnabled()) {
            log.debug("firstName: " + firstName+ ", middleName: " +middleName+ ", lastName: " + lastName );
        }

        String s = "";
        if (!StringTools.isEmpty(firstName)) {
            s += firstName;

            if (!StringTools.isEmpty(middleName) || !StringTools.isEmpty(lastName)) {
                s += " ";
            }
        }
        if (!StringTools.isEmpty(middleName)) {
            s += middleName;
            if (!StringTools.isEmpty(lastName)) {
                s += " ";
            }
        }
        if (!StringTools.isEmpty(lastName)) {
            s += lastName;
        }

        if (StringTools.isEmpty(s)) {
            return "unknown";
        }
        else {
            return StringTools.encodeXml(s);
        }
    }

    public static boolean checkForumConcreteId(DatabaseAdapter adapter, Long forumId, Integer f_id) {

        if (f_id==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CATEGORY a, WM_FORUM_CONCRETE b " +
                "where  a.FORUM_ID=? and a.FORUM_CATEGORY_ID=b.FORUM_CATEGORY_ID and " +
                "       b.F_ID=? ",
                new Object[]{ forumId, f_id }
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and f_id. forumId: "+forumId+", f_id: "+f_id;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumCategoryId(DatabaseAdapter adapter, Long forumId, Integer forumCategoryId) {

        if (forumCategoryId==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CATEGORY a " +
                "where  a.FORUM_ID=? and a.FORUM_CATEGORY_ID=? ",
                new Object[]{ forumId, forumCategoryId }
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and forumCategoryId. forumId: "+forumId+", forumCategoryId: "+forumCategoryId;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumTopicId(DatabaseAdapter adapter, Long forumId, Integer t_id) {

        if (t_id==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_TOPIC b, WM_FORUM_CATEGORY d " +
                "where  b.T_F_ID=a.F_ID and b.T_ID=? and " +
                "       a.FORUM_CATEGORY_ID=d.FORUM_CATEGORY_ID and d.FORUM_ID=?",
                new Object[]{ t_id, forumId }
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and t_id. forumId: "+forumId+", t_id: "+t_id;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumMessageId(DatabaseAdapter adapter, Long forumId, Integer messageId) {

        if (messageId==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_TOPIC b, WM_FORUM_CATEGORY d, " +
                "       WM_FORUM_MESSAGE e " +
                "where  b.T_F_ID=a.F_ID and b.T_ID=e.M_T_ID and " +
                "       a.FORUM_CATEGORY_ID=d.FORUM_CATEGORY_ID and d.FORUM_ID=? and " +
                "       e.M_ID=? ",
                new Object[]{ forumId, messageId }
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed check forumId and messageId. forumId: "+forumId+", messageId: "+messageId;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static WmForumItemType checkForumId(DatabaseAdapter adapter, Long forumId, String serverName) {
        if (forumId==null)
            return null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement(
                "select a.* " +
                "from   WM_FORUM a, SITE_VIRTUAL_HOST b " +
                "where  a.FORUM_ID=? and a.SITE_ID=b.ID_SITE and b.NAME_VIRTUAL_HOST=? "
            );
            RsetTools.setLong(ps, 1, forumId);
            ps.setString(2, serverName);

            rs = ps.executeQuery();
            if (rs.next()) {
                WmForumItemType forum = GetWmForumItem.fillBean(rs);
                return forum;
            }
            return null;
        }
        catch (Exception e) {
            String es = "error check forumId";
            log.error(es, e);
            return null;
        }
    }
}
