package org.riverock.webmill.portal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.schema.core.MainUserMetadataItemType;
import org.riverock.webmill.core.GetMainUserMetadataItem;
import org.riverock.webmill.portlet.PageElement;
import org.riverock.webmill.exception.PortalException;

/**
 * @author SMaslyukov
 *         Date: 19.03.2005
 *         Time: 19:31:50
 *         $Id$
 */
public final class PortalUserMetadata {

    private final static Logger log = Logger.getLogger( PortalUserMetadata.class );

    private final static String sql =
            "select a.* " +
            "from   MAIN_USER_METADATA a, SITE_VIRTUAL_HOST b, AUTH_USER c " +
            "where  a.ID_SITE=b.ID_SITE and b.NAME_VIRTUAL_HOST=? and a.ID_USER=c.ID_USER and " +
            "       c.USER_LOGIN=? and META=?";

    public final static MainUserMetadataItemType getMetadata(
        DatabaseAdapter adapter, String userLogin, String serverName, String metadataName)
        throws PortalException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement( sql );
            ps.setString(1, serverName);
            ps.setString(2, userLogin );
            ps.setString(3, metadataName);

            rs = ps.executeQuery();
            if (rs.next()) {
                MainUserMetadataItemType meta = GetMainUserMetadataItem.fillBean( rs );
                return meta;
            }
            return null;
        } catch (Exception e) {
            String es = "Error get metadata from DB";
            log.error(es, e);
            throw new PortalException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
/*
    public final static void setMetadata(
        DatabaseAdapter adapter, String userLogin, String serverName, String metadataName, 
        MainUserMetadataItemType meta) throws PortalException {


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement( sql );
            ps.setString(1, serverName);
            ps.setString(2, userLogin );
            ps.setString(3, metadataName);

            rs = ps.executeQuery();
            if (rs.next()) {
                MainUserMetadataItemType meta = GetMainUserMetadataItem.fillBean( rs );
                return meta;
            }
            return null;
        } catch (Exception e) {
            String es = "Error get metadata from DB";
            log.error(es, e);
            throw new PortalException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
*/
}
