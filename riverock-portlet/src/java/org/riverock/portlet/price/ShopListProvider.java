package org.riverock.portlet.price;

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: SergeMaslyukov
 * Date: 14.12.2004
 * Time: 13:27:22
 * $Id$
 * <p/>
 * This class used for select shop and bind it to menu item
 */
public class ShopListProvider implements PortletGetList {

    private static Logger log = Logger.getLogger( ShopPage.class );

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {

        if (log.isDebugEnabled())
            log.debug( "Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( 
                "SELECT b.ID_SHOP, b.CODE_SHOP, b.NAME_SHOP " +
                "FROM   WM_PORTAL_CATALOG_LANGUAGE a, WM_PRICE_SHOP_LIST b, WM_PORTAL_SITE_LANGUAGE c " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=? and " +
                "       a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=b.ID_SITE" 
            );

            RsetTools.setLong( ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while (rs.next()) {
                Long id = RsetTools.getLong( rs, "ID_SHOP" );
                String name = "" + id + ", " +
                    RsetTools.getString( rs, "CODE_SHOP" ) + ", " +
                    RsetTools.getString( rs, "NAME_SHOP" );

                ClassQueryItem item =
                    new ClassQueryItemImpl( id, StringTools.truncateString( name, 60 ) );

                if (idContext!=null && idContext.equals( item.getIndex() )) {
                    item.setSelected(true);
                }

                v.add( item );
            }
            return v;

        }
        catch (Exception e) {
            log.error( "Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e );
            return null;
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
