package org.riverock.forum.util;

import java.util.List;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;

/**
 * @author SMaslyukov
 *         Date: 16.04.2005
 *         Time: 17:47:12
 *         $Id$
 */
public class ForumListPerSite implements PortletGetList {
    private static Logger log = Logger.getLogger( ForumListPerSite.class );

    public ForumListPerSite(){}

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        if (log.isDebugEnabled())
            log.debug("Get list of ForumInstance. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);


        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List v = new LinkedList();
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(
                    "select b.FORUM_ID, b.FORUM_NAME, b.SITE_ID "+
                    "from   WM_PORTAL_CATALOG_LANGUAGE a, WM_FORUM b, WM_PORTAL_SITE_LANGUAGE c "+
                    "where  a.ID_SITE_CTX_LANG_CATALOG=? and "+
                    "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+
                    "       c.ID_SITE=b.SITE_ID"
            );

            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while (rs.next())
            {
                Long id = RsetTools.getLong(rs, "FORUM_ID");
                String name = "" + id + ", " + RsetTools.getString(rs, "FORUM_NAME");

                ClassQueryItem item =
                        new ClassQueryItemImpl(id, StringTools.truncateString(name, 60) );

                if (item.getIndex().equals(idContext))
                    item.setSelected( true );

                v.add( item );
            }
            return v;

        }
        catch(Exception e)
        {
            log.error("Get list of ForumInstance. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);
            return null;
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}