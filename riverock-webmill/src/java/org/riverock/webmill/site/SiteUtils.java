/*

 * org.riverock.webmill -- Portal framework implementation

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



/**

 * $Id$

 */

package org.riverock.webmill.site;



import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.common.tools.RsetTools;

import org.riverock.webmill.exception.PortalException;



import java.sql.ResultSet;

import java.sql.PreparedStatement;

import java.util.List;

import java.util.ArrayList;



/**

 * User: smaslyukov

 * Date: 09.08.2004

 * Time: 19:58:16

 * $Id$

 */

public class SiteUtils {



    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SiteUtils.class );



    public static String getGrantedSiteId(DatabaseAdapter adapter, String username)

        throws PortalException

    {

        List list = getGrantedSiteIdList(adapter, username);

        if (list.size()==0)

            return "NULL";



        String r = "";

        for (int i=0; i<list.size(); i++)

        {

            if (r.length()!=0)

                r += ", ";



            r += list.get(i).toString();

        }

        return r;

    }



    public static List getGrantedSiteIdList(DatabaseAdapter adapter, String serverName)

        throws PortalException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {

            String sql_ =

                "select ID_SITE " +

                "from SITE_VIRTUAL_HOST " +

                "where lower(NAME_VIRTUAL_HOST)=lower(?)";



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

            log.error("Exception get siteID", e);

            throw new PortalException(e.getMessage());

        }

        finally {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }





}

