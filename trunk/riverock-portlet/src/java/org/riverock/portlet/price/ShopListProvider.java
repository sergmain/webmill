package org.riverock.portlet.price;

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.webmill.portlet.PortletGetList;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.member.ClassQueryItem;

import org.apache.log4j.Logger;

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

    public List getList( Long idSiteCtxLangCatalog, Long idContext ) {

        if (log.isDebugEnabled())
            log.debug( "Get list of Shop. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;

        List v = new ArrayList();
        try {
            db_ = DatabaseAdapter.getInstance( false );
            ps = db_.prepareStatement( "SELECT b.ID_SHOP, b.CODE_SHOP, b.NAME_SHOP " +
                "FROM site_ctx_lang_catalog a, PRICE_SHOP_TABLE b, site_support_language c " +
                "where a.ID_SITE_CTX_LANG_CATALOG=? and " +
                "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "c.ID_SITE=b.ID_SITE" );

            RsetTools.setLong( ps, 1, idSiteCtxLangCatalog );

            rs = ps.executeQuery();
            while (rs.next()) {
                Long id = RsetTools.getLong( rs, "ID_SHOP" );
                String name = "" + id + ", " +
                    RsetTools.getString( rs, "CODE_SHOP" ) + ", " +
                    RsetTools.getString( rs, "NAME_SHOP" );

                ClassQueryItem item =
                    new ClassQueryItem( id, StringTools.truncateString( name, 60 ) );

                if (idContext.equals( item.index ))
                    item.isSelected = true;

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
