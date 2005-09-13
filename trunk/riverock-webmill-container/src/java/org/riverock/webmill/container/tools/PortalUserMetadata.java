/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.webmill.container.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.portlet.PortletException;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.container.schema.core.MainUserMetadataItemType;
import org.riverock.webmill.container.core.GetMainUserMetadataItem;

/**
 * @author SMaslyukov
 *         Date: 19.03.2005
 *         Time: 19:31:50
 *         $Id$
 */
public final class PortalUserMetadata {

//    private final static Log log = LogFactory.getLog( PortalUserMetadata.class );

    private final static String sql =
            "select a.* " +
            "from   MAIN_USER_METADATA a, SITE_VIRTUAL_HOST b, AUTH_USER c " +
            "where  a.ID_SITE=b.ID_SITE and b.NAME_VIRTUAL_HOST=? and a.ID_USER=c.ID_USER and " +
            "       c.USER_LOGIN=? and META=?";

    public final static MainUserMetadataItemType getMetadata(
        DatabaseAdapter adapter, String userLogin, String serverName, String metadataName)
        throws PortletException {

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
//            log.error(es, e);
            throw new PortletException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

/*
    public final static void setMetadata(
        DatabaseAdapter adapter, String userLogin, String serverName, String metadataName, 
        MainMainUserMetadataItemTypeType meta) throws PortalException {


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement( sql );
            ps.setString(1, serverName);
            ps.setString(2, userLogin );
            ps.setString(3, metadataName);

            rs = ps.executeQuery();
            if (rs.next()) {
                MainMainUserMetadataItemTypeType meta = GetMainMainUserMetadataItemType.fillBean( rs );
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
