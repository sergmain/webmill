/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * User: smaslyukov
 * Date: 09.08.2004
 * Time: 19:58:16
 * $Id$
 */
public final class SiteUtils {

    private final static Logger log = Logger.getLogger( SiteUtils.class );

    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getGrantedSiteId(DatabaseAdapter adapter, String username)
        throws PortletException
    {
        List list = getGrantedSiteIdList(adapter, username);
        if (list.size()==0)
            return "NULL";

        Iterator it = list.iterator();
        String r = "";
        while (it.hasNext()) {
            if (r.length()!=0) {
                r += ", ";
            }
            r += it.next().toString();
        }
        return r;
    }

    public static List getGrantedSiteIdList(DatabaseAdapter adapter, String serverName)
        throws PortletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select ID_SITE " +
                "from   WM_PORTAL_VIRTUAL_HOST " +
                "where  lower(NAME_VIRTUAL_HOST)=lower(?)";

            ps = adapter.prepareStatement(sql_);
            ps.setString(1, serverName);

            rs = ps.executeQuery();

            List list = new ArrayList();
            while(rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_SITE" );
                if (id==null)
                    continue;
                list.add( id );
            }
            return list;
        }
        catch(Exception e)
        {
            final String es = "Exception get siteID";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
}
